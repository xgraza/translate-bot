package me.graza.tbot.util.formatting;

/**
 * @author graza
 * @since 08/15/23
 *
 * ima be real idk why i made this
 */
public class CodeblockBuilder {

  private final StringBuilder builder = new StringBuilder();
  private boolean started;

  public CodeblockBuilder start(String language) {
    if (started) {
      throw new RuntimeException("Already started!");
    }

    started = true;
    builder.append("```")
      .append(language)
      .append("\n");
    return this;
  }

  public CodeblockBuilder append(String content) {
    builder.append(content);
    return this;
  }

  public CodeblockBuilder appendln(String content) {
    builder.append(content).append("\n");
    return this;
  }

  public String build() {
    return builder.append("```").toString();
  }
}
