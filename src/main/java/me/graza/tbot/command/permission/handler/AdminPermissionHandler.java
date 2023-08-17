package me.graza.tbot.command.permission.handler;

import me.graza.tbot.command.Command;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.util.List;

/**
 * @author graza
 * @since 08/16/23
 */
public class AdminPermissionHandler implements PermissionHandler {
  @Override
  public boolean check(Command command, SlashCommandInteraction interaction) {

    if (OwnerPermissionHandler.ownerIds.contains(interaction.getUser().getId())) return true;

    List<Role> roles = interaction.getUser().getRoles(
      interaction.getServer().get());

    for (Role role : roles) {
      if (role.getAllowedPermissions().contains(PermissionType.ADMINISTRATOR)) {
        return true;
      }
    }

    return false;
  }
}
