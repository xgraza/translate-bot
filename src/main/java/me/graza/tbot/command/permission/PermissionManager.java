package me.graza.tbot.command.permission;

import me.graza.tbot.command.Command;
import me.graza.tbot.command.permission.handler.AdminPermissionHandler;
import me.graza.tbot.command.permission.handler.OwnerPermissionHandler;
import me.graza.tbot.command.permission.handler.PermissionHandler;
import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.util.*;

/**
 * @author graza
 * @since 08/15/23
 */
public class PermissionManager {

  /**
   * A map containing the permission and its handler
   */
  private final Map<Permission, PermissionHandler> permissionHandlerMap = new HashMap<>();

  /**
   * The local discord API link
   */
  private final DiscordApi api;

  /**
   * Creates a permission manager
   * @param api the discord API link
   */
  public PermissionManager(DiscordApi api) {
    this.api = api;

    // register handlers
    permissionHandlerMap.put(Permission.OWNER, new OwnerPermissionHandler());
    permissionHandlerMap.put(Permission.ADMIN, new AdminPermissionHandler());
  }

  /**
   * Checks for permissions and then returns the missing permissions if any
   * @param command the command executed
   * @param interaction the slash command interaction
   * @return a set of missing permissions, non-null
   */
  public Set<Permission> check(Command command, SlashCommandInteraction interaction) {
    Permission[] permissions = command.meta().permissions();
    Set<Permission> missing = new HashSet<>();

    for (Permission permission : permissions) {
      PermissionHandler handler = permissionHandlerMap.get(permission);
      if (handler != null && !handler.check(command, interaction)) {
        missing.add(permission);
      }
    }

    return missing;
  }
}
