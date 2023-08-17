package me.graza.tbot.listener;

import me.graza.tbot.TranslateBot;
import me.graza.tbot.translate.Language;
import me.graza.tbot.translate.Translations;
import me.graza.tbot.translate.services.TranslateService;
import me.graza.tbot.translate.services.TranslationResult;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;

/**
 * @author graza
 * @since 08/16/23
 */
public class ReactionEventHandler implements ReactionAddListener {
  @Override
  public void onReactionAdd(ReactionAddEvent event) {
    Emoji emoji = event.getEmoji();
    if (!emoji.isUnicodeEmoji()) return;

    String unicodeEmoji = emoji.asUnicodeEmoji().orElse(null);
    if (unicodeEmoji == null) return;

    Language language = Language.unicode(unicodeEmoji);
    if (language == null) return;

    Message message = event.getMessage().orElse(null);
    if (message == null) message = event.requestMessage().join();
    if (message == null || message.isPrivateMessage() || message.isTts()) return;

    TranslateService service = TranslateBot.instance.dbDriver.guildSettings().get(
      String.valueOf(event.getServer().get().getId()),
      "translateService",
      null);

    if (service != null) {

      // fuck lambdas
      Message finalMessage = message;
      TranslateBot.EXECUTOR.execute(() -> {
        TranslationResult result = Translations.translateTo(service,
          language, finalMessage.getReadableContent());
        if (result != null) {
          finalMessage.reply(result.content()).join();
        }
      });
    }
  }
}
