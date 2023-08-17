package me.graza.tbot.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author graza
 * @since 08/17/23
 */
public class NetworkUtils {

  public static String readInputStream(InputStream is) throws IOException {
    StringBuilder builder = new StringBuilder();
    int b;

    while ((b = is.read()) != -1) {
      builder.append((char) b);
    }

    is.close();
    return builder.toString();
  }

  /**
   * Creates a query string from an array
   * @param params the query string
   * @return the formatted query string
   */
  public static String queryString(String[]... params) {
    return Arrays.stream(params).map((arr) -> {
      String key = arr[0];

      String value;
      try {
        value = arr[1];
      } catch (IndexOutOfBoundsException e) {
        value = key;
      }

      return encodeUri(key) + "=" + value;
    }).collect(Collectors.joining("&"));
  }

  /**
   * Encodes text into URL friendly UTF-8 text
   * @param input the input
   * @return the encoded URI text
   */
  public static String encodeUri(String input) {
    return URLEncoder.encode(input, StandardCharsets.UTF_8);
  }
}
