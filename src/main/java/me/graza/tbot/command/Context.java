package me.graza.tbot.command;

import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.SlashCommandInteraction;

/**
 * @author graza
 * @since 08/15/23
 * @param interaction the command interaction
 * @param api the discord API link
 */
public record Context(SlashCommandInteraction interaction, DiscordApi api) {
}
