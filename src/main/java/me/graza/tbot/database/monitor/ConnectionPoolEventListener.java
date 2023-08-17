package me.graza.tbot.database.monitor;

import com.mongodb.event.*;
import org.slf4j.Logger;

/**
 * @author graza
 * @since 08/16/23
 */
public class ConnectionPoolEventListener implements ConnectionPoolListener {

  private final Logger log;

  public ConnectionPoolEventListener(Logger log) {
    this.log = log;
  }

  @Override
  public void connectionPoolReady(ConnectionPoolReadyEvent event) {
    log.info("Connection pool is ready! Server id: {}", event.getServerId());
  }
}
