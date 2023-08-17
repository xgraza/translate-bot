package me.graza.tbot.command.impl.owner;

import me.graza.tbot.TranslateBot;
import me.graza.tbot.command.Command;
import me.graza.tbot.command.CommandMeta;
import me.graza.tbot.command.Context;
import me.graza.tbot.command.permission.Permission;

/**
 * @author graza
 * @since 06/16/23
 */
@CommandMeta(name = "shutdown",
  description = "Shuts down the bot",
  permissions = Permission.OWNER)
public class ShutdownCommand extends Command {
  @Override
  public void execute(Context ctx) {
    ctx.interaction().createImmediateResponder()
      .setContent("ok bye :wave:")
      .respond();

    TranslateBot.instance.shutdown();
  }
}
