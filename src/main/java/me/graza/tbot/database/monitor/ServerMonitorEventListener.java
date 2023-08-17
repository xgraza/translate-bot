package me.graza.tbot.database.monitor;

import com.mongodb.event.ServerHeartbeatStartedEvent;
import com.mongodb.event.ServerHeartbeatSucceededEvent;
import com.mongodb.event.ServerMonitorListener;
import org.slf4j.Logger;

/**
 * @author graza
 * @since 08/16/23
 */
public class ServerMonitorEventListener implements ServerMonitorListener {
  private final Logger log;

  private long latency, lastHeartbeat;

  public ServerMonitorEventListener(Logger log) {
    this.log = log;
  }

  @Override
  public void serverHearbeatStarted(ServerHeartbeatStartedEvent event) {
    lastHeartbeat = System.nanoTime();
  }

  @Override
  public void serverHeartbeatSucceeded(ServerHeartbeatSucceededEvent event) {
    long time = System.nanoTime();
    latency = (time - lastHeartbeat) / 1000000L;
  }

  public long latency() {
    return latency;
  }
}
