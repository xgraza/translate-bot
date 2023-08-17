package me.graza.tbot.listener;

import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.listener.server.ServerJoinListener;

/**
 * @author graza
 * @since 08/16/23
 */
public class ServerJoinEventHandler implements ServerJoinListener {
  @Override
  public void onServerJoin(ServerJoinEvent event) {
    String id = event.getServer().getIdAsString();

  }
}
