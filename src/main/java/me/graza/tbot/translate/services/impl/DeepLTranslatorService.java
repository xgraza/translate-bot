package me.graza.tbot.translate.services.impl;

import me.graza.tbot.config.ConfigurationReader;
import me.graza.tbot.translate.Language;
import me.graza.tbot.translate.services.TranslationResult;
import me.graza.tbot.translate.services.Translator;
import me.graza.tbot.util.NetworkUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author graza
 * @since 08/16/23
 */
public class DeepLTranslatorService implements Translator {

  private static final String URL = "https://api-free.deepl.com/v2/translate";

  /**
   * The API key for requesting deepl
   */
  private final String apiKey = ConfigurationReader.get("deepl_api_key");

  @Override
  public TranslationResult translateTo(Language language, String text) {

    try {
      HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);

      connection.setRequestProperty("Authorization", "DeepL-Auth-Key " + apiKey);
      connection.setRequestProperty("Content-Type", "application/json");

      byte[] bytes = ("{\"text\":[\""
        + text
        + "\"],\"target_lang\":\""
        + language.locale().replaceAll("_", "-").toUpperCase()
        + "\"}").getBytes(StandardCharsets.UTF_8);
      OutputStream os = connection.getOutputStream();
      os.write(bytes, 0, bytes.length);
      os.close();

      connection.connect();

      if (connection.getResponseCode() == 200) {

        String content = NetworkUtils.readInputStream(
          connection.getInputStream());

        JSONObject object = new JSONObject(content);

        if (object.has("translations")) {
          JSONObject translation = object.getJSONArray("translations").getJSONObject(0);
          return new TranslationResult(
            translation.getString("text"),
            null);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public TranslationResult translateFrom(Language source, Language target, String text) {
    return null;
  }
}
