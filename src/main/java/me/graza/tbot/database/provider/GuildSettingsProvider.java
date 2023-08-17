package me.graza.tbot.database.provider;

import me.graza.tbot.database.DatabaseDriver;
import me.graza.tbot.database.entities.GuildSettings;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.mongodb.client.model.Filters.eq;

/**
 * @author graza
 * @since 08/16/23
 */
public class GuildSettingsProvider extends Provider<GuildSettings> {

  private final Map<String, GuildSettings> guildSettingsMap = new ConcurrentHashMap<>();

  public GuildSettingsProvider(DatabaseDriver driver) {
    super("guild", driver);
  }

  public <V> V get(String id, String key, V defaultValue) {
    GuildSettings guildSettings = get(id);
    if (guildSettings == null) return defaultValue;
    return guildSettings.get(key, defaultValue);
  }

  public void set(String id, String key, Object value) {
    GuildSettings guildSettings = get(id);
    if (guildSettings != null) {
      guildSettings.set(key, value);
      update(id, guildSettings);
    }
  }

  @Override
  public GuildSettings get(String id) {
    if (guildSettingsMap.containsKey(id)) {
      return guildSettingsMap.get(id);
    }

    GuildSettings settings = collection().find(eq("guildId", id),
      GuildSettings.class).first();
    if (settings == null) {
      settings = new GuildSettings();
      settings.guildId = id;
      collection().insertOne(settings);
      guildSettingsMap.put(id, settings);
    }

    return settings;
  }

  @Override
  protected void update(String key, GuildSettings object) {
    collection().replaceOne(eq("guildId", key), object);
    guildSettingsMap.put(key, object);
  }
}
