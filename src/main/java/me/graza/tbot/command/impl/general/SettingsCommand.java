package me.graza.tbot.command.impl.general;

import me.graza.tbot.TranslateBot;
import me.graza.tbot.command.Command;
import me.graza.tbot.command.CommandMeta;
import me.graza.tbot.command.Context;
import me.graza.tbot.command.permission.Permission;
import me.graza.tbot.database.entities.GuildSettings;
import me.graza.tbot.util.formatting.CodeblockBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOption;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author graza
 * @since 08/16/23
 */
@CommandMeta(name = "settings",
  description = "Configures the bot per guild",
  permissions = Permission.ADMIN)
public class SettingsCommand extends Command {
  @Override
  public void execute(Context ctx) {
    String guildId = String.valueOf(ctx.interaction().getServer().get().getId());
    GuildSettings settings = TranslateBot.instance.dbDriver
      .guildSettings().get(guildId);
    if (settings == null) return;

    if (ctx.interaction().getArguments().isEmpty()) {
      EmbedBuilder builder = new EmbedBuilder()
        .setAuthor("\uD83E\uDC52 Settings for " + ctx.interaction().getServer().get().getName())
        .setColor(new Color(102, 164, 224));

      CodeblockBuilder codeblock = new CodeblockBuilder();
      codeblock.start("prolog");

      for (String key : settings.keys()) {
        if (key.equals("guildId")) continue;
        codeblock.append(key)
          .append(" :: ");

        Object value = settings.get(key, null);
        if (value != null) {
          codeblock.append(value.toString());
        } else {
          codeblock.append("N/A");
        }

        codeblock.append("\n");
      }

      builder.setDescription(codeblock.build());

      ctx.interaction().createImmediateResponder()
        .addEmbed(builder)
        .respond();
      return;
    }

    Optional<SlashCommandInteractionOption> settingOptional = ctx.interaction().getArgumentByName("setting");
    if (settingOptional.isPresent()) {
      String key = settingOptional.get().getStringValue().orElse(null);
      if (key == null || key.isEmpty()) {
        ctx.interaction().createImmediateResponder()
          .setContent("Unrecognized setting name")
          .respond();

        return;
      }

      Object obj = settings.get(key, null);

      Optional<SlashCommandInteractionOption> valueOptional = ctx.interaction().getArgumentByName("value");
      if (valueOptional.isEmpty()) {

        String message = "";

        if (obj != null) {
          message = key + " -> " + obj.toString();
        } else {
          message = "No value exists for " + key;
        }

        ctx.interaction().createImmediateResponder()
          .setContent(message)
          .respond();

        return;
      }

      String val = valueOptional.get().getStringValue().orElse(null);
      if (val == null || val.isEmpty()) {
        ctx.interaction().createImmediateResponder()
          .setContent("Provide a new setting value")
          .respond();
        return;
      }

      if (obj instanceof Enum<?>) {
        try {
          Enum<?> enumVal = Enum.valueOf(((Enum<?>) obj).getDeclaringClass(),
            val.toUpperCase());

          TranslateBot.instance.dbDriver.guildSettings().set(guildId, key, enumVal);

          ctx.interaction().createImmediateResponder()
            .setContent("Set value of `" + key + "` to " + enumVal.name())
            .respond();
        } catch (Exception e) {
          ctx.interaction().createImmediateResponder()
            .setContent("No value found with that name")
            .respond();
        }
      }
    }
  }

  @Override
  public List<SlashCommandOption> options() {
    return Arrays.asList(
      SlashCommandOption.createStringOption(
        "setting", "The setting name to change", false),
      SlashCommandOption.createStringOption(
        "value", "The new setting value", false));
  }
}
