package me.graza.tbot.database.provider;

import com.mongodb.client.MongoCollection;
import me.graza.tbot.database.DatabaseDriver;
import me.graza.tbot.database.entities.DatabaseEntity;
import me.graza.tbot.database.entities.GuildSettings;

/**
 * @author graza
 * @since 08/16/23
 */
public abstract class Provider<T extends DatabaseEntity> {

  private final String collectionName;
  protected final DatabaseDriver driver;

  public Provider(String collectionName, DatabaseDriver driver) {
    this.collectionName = collectionName;
    this.driver = driver;

    if (collection() == null) {
      driver.db().createCollection(collectionName);
    }
  }

  public abstract T get(String id);

  protected abstract void update(String key, T object);

  protected MongoCollection<GuildSettings> collection() {
    return driver.db().getCollection(collectionName, GuildSettings.class);
  }

  /**
   * The name of the collection
   * @return the collection this provider handles
   */
  public String collectionName() {
    return collectionName;
  }
}
