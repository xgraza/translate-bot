package me.graza.tbot.command.permission.handler;

import me.graza.tbot.command.Command;
import me.graza.tbot.config.ConfigurationReader;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gavin
 * @since 08/15/23
 */
public class OwnerPermissionHandler implements PermissionHandler {
  public static final List<Long> ownerIds = new ArrayList<>();

  public OwnerPermissionHandler() {
    String[] owners = ConfigurationReader.get("owners").split(",");
    for (String ownerId : owners) {
      ownerIds.add(Long.parseLong(ownerId));
    }
  }

  @Override
  public boolean check(Command command, SlashCommandInteraction interaction) {
    return ownerIds.contains(interaction.getUser().getId());
  }
}
