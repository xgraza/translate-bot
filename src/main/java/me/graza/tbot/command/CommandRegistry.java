package me.graza.tbot.command;

import me.graza.tbot.TranslateBot;
import me.graza.tbot.command.impl.owner.EvaluateCommand;
import me.graza.tbot.command.permission.Permission;
import me.graza.tbot.command.permission.PermissionManager;
import me.graza.tbot.util.Reflections;
import me.graza.tbot.util.formatting.CodeblockBuilder;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author graza
 * @since 08/15/23
 */
public class CommandRegistry {

  /**
   * The package where commands are held
   */
  private static final String COMMANDS_PACKAGE = "me.graza.tbot.command";

  /**
   * A map of the command name and its instance
   */
  private final Map<String, Command> commandMap = new LinkedHashMap<>();

  private final PermissionManager permissions;

  /**
   * Creates this registry
   * @param api the discord API instance to the bot
   * @throws IOException if reflections fail
   */
  public CommandRegistry(DiscordApi api) throws IOException {
    Reflections.classesInPackage(COMMANDS_PACKAGE, Command.class)
      .forEach((clazz) -> {
        if (clazz == Command.class) return;

        try {
          Command command = (Command) clazz.getConstructors()[0].newInstance();
          command.setSlashCommand(SlashCommand.with(
            command.meta().name(), command.meta().description())
            .setOptions(command.options())
            .createGlobal(api)
            .join());

          commandMap.put(command.meta().name(), command);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
        }
      });

    TranslateBot.LOGGER.info("Found {} commands in {}",
      commandMap.size(), COMMANDS_PACKAGE);

    permissions = new PermissionManager(api);

    api.addSlashCommandCreateListener((e) -> {
      SlashCommandInteraction interaction = e.getSlashCommandInteraction();
      Command command = get(interaction.getCommandName());
      if (command == null) return;

      Set<Permission> missing = permissions.check(command, interaction);
      if (!missing.isEmpty()) {
        interaction.createImmediateResponder()
          .setContent(new StringBuilder()
            .append("You are missing the following permission(s) to execute this command: ")
            .append(missing.stream()
              .map((x) -> "`" + x.name() + "`")
              .collect(Collectors.joining(", ")))
            .toString())
          .respond();
        return;
      }

      try {
        command.execute(new Context(interaction, api));
      } catch (Exception ex) {

        EmbedBuilder embed = new EmbedBuilder()
          .setColor(new Color(190, 89, 89))
          .setAuthor("We ran into an issue!", null, interaction.getUser().getAvatar());

        StringBuilder builder = new StringBuilder("Exception:")
          .append("\n");

        String stackTrace = EvaluateCommand.formatException(ex);
        stackTrace = stackTrace.substring(0, Math.min(1950, stackTrace.length()));

        builder.append(new CodeblockBuilder()
          .start("java")
          .append(stackTrace)
          .build());

        embed.setDescription(builder.toString())
          .setFooter("Please report this to @xgraza");

        interaction.createImmediateResponder()
          .addEmbed(embed)
          .respond();
      }
    });
  }

  /**
   * Gets a command by name
   * @param name the name
   * @return the command instance or null
   * @param <T> the type of command
   */
  public <T extends Command> T get(String name) {
    return (T) commandMap.getOrDefault(name, null);
  }

  /**
   * Returns the commands in a Collection
   * @return the commands in a Collection
   */
  public Collection<Command> values() {
    return commandMap.values();
  }

  public PermissionManager permissions() {
    return permissions;
  }
}
