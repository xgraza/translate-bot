package me.graza.tbot.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author graza
 * @since 08/15/23
 */
public class ConfigurationReader {

  /**
   * A map containing the config key and its value
   */
  private static final Map<String, String> configMap = new HashMap<>();

  /**
   * Gets the value of a config key
   * @param key the key
   * @return the value or null
   */
  public static String get(String key) {
    return configMap.get(key);
  }

  /**
   * Reads the config file
   * @param file the file
   * @return the amount of entries loaded from that config file
   * @throws IOException
   */
  public static int read(File file) throws IOException {
    if (!file.exists()) throw new RuntimeException(
      file + " does not exist");

    configMap.clear();

    InputStream is = Files.newInputStream(file.toPath());

    StringBuilder builder = new StringBuilder();
    int b;
    while ((b = is.read()) != -1) {
      builder.append((char) b);
    }
    is.close();

    String content = builder.toString();
    if (content.isEmpty()) throw new RuntimeException(
      "Empty config file");

    for (String line : content.split("\n")) {
      line = line.trim();
      if (line.isEmpty()) continue;

      String[] parts = line.split("=");
      if (parts.length < 2) continue;

      // 10/10 code
      String key = parts[0];
      String value = String.join("=",
        Arrays.copyOfRange(parts, 1, parts.length));

      configMap.put(key, value);
    }

    return configMap.size();
  }
}
