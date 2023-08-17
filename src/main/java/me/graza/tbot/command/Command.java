package me.graza.tbot.command;

import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;

import java.util.Collections;
import java.util.List;

/**
 * @author graza
 * @since 08/15/23
 */
public abstract class Command {
  private final CommandMeta meta;
  private SlashCommand slashCommand;

  /**
   * Creates a new command
   */
  public Command() {
    if (!getClass().isAnnotationPresent(CommandMeta.class))
      throw new RuntimeException(
        "@CommandMeta is required on the top of Command implementations.");

    meta = getClass().getDeclaredAnnotation(CommandMeta.class);
  }

  /**
   * Executes this command
   * @param ctx the command context
   */
  public abstract void execute(Context ctx);

  public List<SlashCommandOption> options() {
    return Collections.emptyList();
  }

  public CommandMeta meta() {
    return meta;
  }

  public void setSlashCommand(SlashCommand slashCommand) {
    if (this.slashCommand == null) {
      this.slashCommand = slashCommand;
    }
  }

  public SlashCommand slashCommand() {
    return slashCommand;
  }
}
