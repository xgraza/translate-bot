package me.graza.tbot.translate.services;

import me.graza.tbot.translate.Language;

/**
 * @author Gavin
 * @since 08/16/23
 */
public interface Translator {

  /**
   * Translates text into another language (eg, (Language.SPANISH, "Hello, world!") should translate into spanish)
   * @param language the language to translate the text into
   * @param text the text to translate
   * @return the result of the translation or null
   */
  TranslationResult translateTo(Language language, String text);

  /**
   * Translates text from a source language
   * @param source the language the text is currently in
   * @param target the language to translate into
   * @param text the text to translate
   * @return the result of the translation or null
   */
  TranslationResult translateFrom(Language source, Language target, String text);

}
