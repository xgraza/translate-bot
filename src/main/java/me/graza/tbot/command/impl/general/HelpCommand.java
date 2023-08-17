package me.graza.tbot.command.impl.general;

import me.graza.tbot.TranslateBot;
import me.graza.tbot.command.Command;
import me.graza.tbot.command.CommandMeta;
import me.graza.tbot.command.Context;
import me.graza.tbot.command.permission.Permission;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandOption;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author graza
 * @since 08/17/23
 */
@CommandMeta(name = "help",
  description = "Sends an embed containing all of the commands")
public class HelpCommand extends Command {

  private static final Color EMBED_COLOR = new Color(102, 164, 224);

  @Override
  public void execute(Context ctx) {
    if (ctx.interaction().getArguments().isEmpty()) {
      List<Command> commands = new ArrayList<>(TranslateBot.instance.commands.values())
        .stream()
        .filter((command) -> TranslateBot.instance.commands
          .permissions()
          .check(command, ctx.interaction()).isEmpty())
        .toList();

      EmbedBuilder embed = new EmbedBuilder()
        .setAuthor("Help [" + commands.size() + "]",
          null, ctx.interaction().getUser().getAvatar())
        .setColor(EMBED_COLOR);

      int padding = 0;
      for (Command command : commands) {
        String name = command.meta().name();
        if (padding < name.length()) {
          padding = name.length();
        }
      }

      StringBuilder builder = new StringBuilder();
      for (Command command : commands) {
        builder.append("`")
          .append(command.meta().name());

        int length = padding - command.meta().name().length();
        builder.append(" ".repeat(Math.max(0, length + 1)));

        builder.append(":`")
          .append(" ")
          .append(" | ")
          .append(command.meta().description());

        builder.append("\n");
      }

      embed.setDescription(builder.toString())
        .setFooter("Tip: Use /help <command name> to see more info");

      ctx.interaction().createImmediateResponder()
        .addEmbed(embed)
        .respond();
      return;
    }

    String commandName = ctx.interaction().getArgumentByName("name")
      .get()
      .getStringValue()
      .orElse(null);
    if (commandName == null || commandName.isEmpty()) {
      ctx.interaction().createImmediateResponder()
        .setContent("Could not find a command with that name")
        .respond();

      return;
    }

    Command command = TranslateBot.instance.commands.get(commandName.toLowerCase());
    if (command == null) {
      ctx.interaction().createImmediateResponder()
        .setContent("Could not find a command with that name")
        .respond();

      return;
    }

    EmbedBuilder embed = new EmbedBuilder()
      .setAuthor("Help - " + command.meta().name(),
        null, ctx.interaction().getUser().getAvatar())
      .setColor(EMBED_COLOR);

    StringBuilder builder = new StringBuilder();

    builder.append("`Description :` | ")
      .append(command.meta().description())
      .append("\n");
    builder.append("`Permissions :` | ");

    Permission[] permissions = command.meta().permissions();
    if (permissions.length == 0) {
      builder.append("NONE");
    } else {
      builder.append(Arrays.stream(command.meta().permissions()).map(Enum::name).collect(Collectors.joining(", ")));
    }
    builder.append("\n");

    if (!command.options().isEmpty()) {
      builder.append("`Options     :` | ")
        .append("\n");

      List<SlashCommandOption> options = command.options();

      int padding = 0;
      for (SlashCommandOption option : options) {
        String name = option.getName();
        if (padding < name.length()) {
          padding = name.length();
        }
      }

      for (SlashCommandOption option : command.options()) {
        boolean r = option.isRequired();

        builder.append("- ");
        builder.append("`");
        builder.append(r ? "[" : "<");
        builder.append(option.getName());
        builder.append(r ? "]" : ">");

        int length = padding - option.getName().length();
        builder.append(" ".repeat(Math.max(0, length + 1)));

        builder.append(":` | ");

        builder.append(option.getDescription())
          .append("\n");
      }
    }

    embed.setDescription(builder.toString())
      .setFooter("<> = Optional, [] = Required");

    ctx.interaction().createImmediateResponder()
      .addEmbed(embed)
      .respond();
  }

  @Override
  public List<SlashCommandOption> options() {
    return Collections.singletonList(
      SlashCommandOption.createStringOption("name",
        "The command to get extra information on", false));
  }
}
