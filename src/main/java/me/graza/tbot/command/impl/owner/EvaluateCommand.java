package me.graza.tbot.command.impl.owner;

import me.graza.tbot.TranslateBot;
import me.graza.tbot.command.Command;
import me.graza.tbot.command.CommandMeta;
import me.graza.tbot.command.Context;
import me.graza.tbot.command.permission.Permission;
import me.graza.tbot.util.formatting.CodeblockBuilder;
import org.javacord.api.interaction.SlashCommandOption;
import org.openjdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author graza
 * @since 08/15/23
 */
@CommandMeta(name = "evaluate",
  description = "Evaluates code",
  permissions = Permission.OWNER)
public class EvaluateCommand extends Command {

  /**
   * The script engine manager to create a new script engine
   */
  private static final ScriptEngineManager manager = new ScriptEngineManager();

  /**
   * The "key" used to reset the script engine
   */
  private static final String RESET_KEY = "--reset";

  /**
   * The current running script engine
   */
  private NashornScriptEngine scriptEngine;

  @Override
  public void execute(Context ctx) {
    if (scriptEngine == null) {
      scriptEngine = createEngine(ctx);
    }

    Optional<String> code = ctx.interaction().getArgumentStringValueByName("code");
    if (code.isEmpty()) {
      ctx.interaction().createImmediateResponder()
        .setContent("erm... what the bllop!/!?")
        .respond();
      return;
    }

    String c = code.get();
    if (c.equals(RESET_KEY)) {
      ctx.interaction().createImmediateResponder()
        .setContent("ok resetting...")
        .respond();

      scriptEngine = createEngine(ctx);
      return;
    }

    scriptEngine
      .getBindings(ScriptContext.ENGINE_SCOPE)
      .put("ctx", ctx);

    try {
      long startTime = System.nanoTime();
      Object result = scriptEngine.eval(c);
      long endTime = System.nanoTime();
      long evalTime = endTime - startTime;
      provideEvalResponse(ctx, result, evalTime);
    } catch (ScriptException e) {
      provideEvalResponse(ctx, e, -1L);
      e.printStackTrace();
    }
  }

  @Override
  public List<SlashCommandOption> options() {
    return Collections.singletonList(
      SlashCommandOption.createStringOption(
        "code", "The code to execute", true));
  }

  private void provideEvalResponse(Context ctx, Object result, long evalTime) {
    CodeblockBuilder builder = new CodeblockBuilder();
    if (result instanceof Exception ex) {
      builder.append("An exception occurred:");

      String stackTrace = formatException(ex);

      builder.start("js");
      builder.append(stackTrace.substring(0, Math.min(1900, stackTrace.length())));
    } else {
      builder.append("Evaluated in ")
        .append(String.valueOf(evalTime))
        .append("ns")
        .append(" (")
        .append(String.valueOf(evalTime / 1_000_000L))
        .append("ms")
        .append(")");

      builder.start("js");
      builder.append(result == null ? "null" : result.toString());
    }

    ctx.interaction().createImmediateResponder()
      .setContent(builder.build())
      .respond();
  }

  private NashornScriptEngine createEngine(Context ctx) {
    ScriptEngine engine = manager.getEngineByExtension("js");
    if (!(engine instanceof NashornScriptEngine nashorn)) throw new RuntimeException(
      "Failed to find Nashorn script engine.");

    Bindings bindings = nashorn.createBindings();
    bindings.put("api", ctx.api());
    bindings.put("interaction", ctx.interaction());
    bindings.put("bot", TranslateBot.instance);

    nashorn.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

    return nashorn;
  }

  public static String formatException(Exception ex) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    String stackTrace = sw.toString();
    pw.close();
    return stackTrace;
  }
}
