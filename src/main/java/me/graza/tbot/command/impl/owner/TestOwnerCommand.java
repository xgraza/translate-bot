package me.graza.tbot.command.impl.owner;

import me.graza.tbot.command.Command;
import me.graza.tbot.command.CommandMeta;
import me.graza.tbot.command.Context;
import me.graza.tbot.command.permission.Permission;

/**
 * @author graza
 * @since 08/15/23
 */
@CommandMeta(name = "test", permissions = {Permission.OWNER})
public class TestOwnerCommand extends Command {
  @Override
  public void execute(Context ctx) {
    ctx.interaction().createImmediateResponder()
      .setContent("You are a owner, hihihihi!")
      .respond();
  }
}
