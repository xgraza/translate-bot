package me.graza.tbot.command.impl.general;

import me.graza.tbot.TranslateBot;
import me.graza.tbot.command.Command;
import me.graza.tbot.command.CommandMeta;
import me.graza.tbot.command.Context;

import java.util.concurrent.ExecutionException;

/**
 * @author graza
 * @since 08/15/23
 */
@CommandMeta(name = "ping", description = "Gets the gateway and rest latency")
public class PingCommand extends Command {
  @Override
  public void execute(Context ctx) {
    long gateway = ctx.api().getLatestGatewayLatency().toMillis();
    long rest = -1L;
    try {
      rest = ctx.api().measureRestLatency().get().toMillis();
    } catch (InterruptedException | ExecutionException e) {
      TranslateBot.LOGGER.error("Failed to get REST latency:");
      e.printStackTrace();
    }

    ctx.interaction().createImmediateResponder()
      .setContent(new StringBuilder()
        .append(":stopwatch: Gateway: `")
        .append(gateway)
        .append("ms`")
        .append("\n")
        .append(":satellite: REST: `")
        .append(rest == -1L ? "ERROR" : (rest + "ms"))
        .append("`")
        .append("\n")
        .append(":bar_chart: MongoDB: ")
        .append("`")
        .append(TranslateBot.instance.dbDriver.serverMonitor().latency())
        .append("ms")
        .append("`")
        .toString())
      .respond();
  }
}
