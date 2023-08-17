package me.graza.tbot;

import me.graza.tbot.command.CommandRegistry;
import me.graza.tbot.config.ConfigurationReader;
import me.graza.tbot.database.DatabaseDriver;
import me.graza.tbot.listener.ReactionEventHandler;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.user.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author graza
 * @since 08/15/23
 */
public enum TranslateBot {

  /**
   * The instance for TranslateBot
   */
  instance;

  /**
   * The config file
   */
  public static final File CONFIG_FILE = Paths.get("")
    .resolve("config.txt")
    .toFile();

  /**
   * The logger for the bot
   */
  public static final Logger LOGGER = LoggerFactory.getLogger(
    "Translate-Bot");

  /**
   * The bot executor
   */
  public static final Executor EXECUTOR = Executors.newFixedThreadPool(1);

  public DiscordApi api;
  public CommandRegistry commands;
  public DatabaseDriver dbDriver;

  void init() throws IOException {
    LOGGER.info("Starting {} {}+{}-{}/{}", BuildConfig.NAME,
      BuildConfig.VERSION,
      BuildConfig.BUILD,
      BuildConfig.HASH,
      BuildConfig.BRANCH);

    LOGGER.info("Reading config file {}", CONFIG_FILE);
    LOGGER.info("Loaded {} config keys", ConfigurationReader.read(CONFIG_FILE));

    LOGGER.info("Creating Discord API link");
    api = new DiscordApiBuilder()
      .setToken(ConfigurationReader.get("token"))
      .setIntents(Intent.GUILD_MESSAGE_REACTIONS, Intent.MESSAGE_CONTENT)
      .setUserCacheEnabled(false)
      .login()
      .join();

    dbDriver = new DatabaseDriver("testing");

    api.updateStatus(UserStatus.DO_NOT_DISTURB);
    api.updateActivity(ActivityType.LISTENING, "reactions on your messages!");

    LOGGER.info("Creating command registry");
    commands = new CommandRegistry(api);

    api.addLostConnectionListener((e) -> LOGGER.info("Lost connection"));
    api.addReactionAddListener(new ReactionEventHandler());

    Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
  }

  public void shutdown() {
    LOGGER.info("Shutting down...");
    api.disconnect();
    dbDriver.shutdown();
    LOGGER.info("All good! Goodbye");
  }
}
