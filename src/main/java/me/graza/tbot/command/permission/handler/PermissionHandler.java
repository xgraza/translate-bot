package me.graza.tbot.command.permission.handler;

import me.graza.tbot.command.Command;
import org.javacord.api.interaction.SlashCommandInteraction;

/**
 * @author graza
 * @since 08/15/23
 */
public interface PermissionHandler {

  /**
   * Checks if this permission can be bypassed
   * @param command the command executed
   * @param interaction the slash command interaction
   * @return if the permission check can be bypassed
   */
  boolean check(Command command, SlashCommandInteraction interaction);
}
