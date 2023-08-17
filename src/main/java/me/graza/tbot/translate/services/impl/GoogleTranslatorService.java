package me.graza.tbot.translate.services.impl;

import me.graza.tbot.translate.Language;
import me.graza.tbot.translate.services.TranslationResult;
import me.graza.tbot.translate.services.Translator;
import me.graza.tbot.util.NetworkUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static me.graza.tbot.util.NetworkUtils.encodeUri;
import static me.graza.tbot.util.NetworkUtils.queryString;

/**
 * @author graza, therealbush
 * @since 08/16/23
 */
public class GoogleTranslatorService implements Translator {
  private static final String GOOGLE_TRANSLATE_URL = "https://translate.googleapis.com/translate_a/single";

  @Override
  public TranslationResult translateTo(Language language, String text) {
    try {
      HttpURLConnection connection = (HttpURLConnection) new URL(
        genUrl(language, Language.AUTO, text)).openConnection();

      connection.setConnectTimeout(5000);
      connection.setReadTimeout(5000);
      connection.setDoOutput(true);
      connection.setUseCaches(false);

      connection.connect();

      if (connection.getResponseCode() == 200) {
        String content = NetworkUtils.readInputStream(
          connection.getInputStream());

        JSONArray arr = new JSONArray(content);

        StringBuilder builder = new StringBuilder();
        JSONArray obj = arr.getJSONArray(0);

        for (Object json : obj) {
          if (json instanceof JSONArray jsonArray) {
            Object value = jsonArray.get(0);
            if (value instanceof String s) {
              builder.append(s.replaceAll("\n", "")).append(" ");
            }
          }
        }

        String translated = builder.toString();
        return new TranslationResult(translated, null);
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

  private String genUrl(Language language, Language source, String text) {
    return GOOGLE_TRANSLATE_URL + "?" + queryString(new String[][]{
      {"client", "gtx"},
      {"dt", "at"},
      {"dt", "bd"},
      {"dt", "ex"},
      {"dt", "ld"},
      {"dt", "md"},
      {"dt", "qca"},
      {"dt", "rw"},
      {"dt", "rm"},
      {"dt", "ss"},
      {"dt", "t"},
      {"ie", "UTF-8"},
      {"oe", "UTF-8"},
      {"otf", "1"},
      {"ssel", "0"},
      {"tsel", "0"},
      {"tk", "bushissocool"},
      {"sl", source.locale()},
      {"tl", language.locale()},
      {"hl", language.locale()},
      {"q", encodeUri(text)}
    });
  }
}
