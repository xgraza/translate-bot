package me.graza.tbot.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import me.graza.tbot.config.ConfigurationReader;
import me.graza.tbot.database.monitor.ConnectionPoolEventListener;
import me.graza.tbot.database.monitor.ServerMonitorEventListener;
import me.graza.tbot.database.provider.GuildSettingsProvider;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * @author graza
 * @since 08/16/23
 */
public class DatabaseDriver {

  /**
   * The logger for the MongoDB connection
   */
  private static final Logger log = LoggerFactory.getLogger(
    "MongoDB");

  private MongoClient client;
  private MongoDatabase database;

  private ServerMonitorEventListener serverMonitor;

  private final GuildSettingsProvider guildSettings;

  /**
   * Creates a database driver "wrapper"
   * @param databaseName the database name to use
   */
  public DatabaseDriver(String databaseName) {
    String mongoUri = ConfigurationReader.get("mongodb_uri");
    if (mongoUri == null) throw new RuntimeException(
      "Invalid MongoDB URI");

    CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
    CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(),
      fromProviders(pojoCodecProvider));

    MongoClientSettings.Builder builder = MongoClientSettings.builder();
    builder.applyConnectionString(new ConnectionString(mongoUri));
    builder.applyToConnectionPoolSettings((api) -> api.addConnectionPoolListener(
      new ConnectionPoolEventListener(log)));
    serverMonitor = new ServerMonitorEventListener(log);
    builder.applyToServerSettings((api) -> api.addServerMonitorListener(serverMonitor));

    client = MongoClients.create(builder.build());
    database = client.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry);

    guildSettings = new GuildSettingsProvider(this);
  }

  public void shutdown() {
    if (client != null) {
      client.close();
      client = null;
    }
  }

  public MongoClient client() {
    return client;
  }

  public MongoDatabase db() {
    return database;
  }

  public ServerMonitorEventListener serverMonitor() {
    return serverMonitor;
  }

  public GuildSettingsProvider guildSettings() {
    return guildSettings;
  }
}
