package me.graza.tbot.database.entities;

import me.graza.tbot.translate.services.TranslateService;

/**
 * @author graza
 * @since 08/16/23
 */
public class GuildSettings extends DatabaseEntity {
  public String guildId;
  public TranslateService translateService = TranslateService.GOOGLE;

  public GuildSettings() {

  }
}
