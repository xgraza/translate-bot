package me.graza.tbot.translate.services.impl;

import me.graza.tbot.translate.Language;
import me.graza.tbot.translate.services.TranslationResult;
import me.graza.tbot.translate.services.Translator;

/**
 * @author graza
 * @since 08/16/23
 */
public class YandexTranslatorService implements Translator {
  @Override
  public TranslationResult translateTo(Language language, String text) {
    return null;
  }

  @Override
  public TranslationResult translateFrom(Language source, Language target, String text) {
    return null;
  }
}
