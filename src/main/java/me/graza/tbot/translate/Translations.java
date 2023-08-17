package me.graza.tbot.translate;

import me.graza.tbot.translate.services.TranslateService;
import me.graza.tbot.translate.services.TranslationResult;
import me.graza.tbot.translate.services.Translator;
import me.graza.tbot.translate.services.impl.DeepLTranslatorService;
import me.graza.tbot.translate.services.impl.GoogleTranslatorService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author graza
 * @since 08/16/23
 */
public class Translations {

  private static final Map<TranslateService, Translator> translatorServiceMap = new HashMap<>();

  static {
    translatorServiceMap.put(TranslateService.GOOGLE, new GoogleTranslatorService());
    translatorServiceMap.put(TranslateService.DEEPL, new DeepLTranslatorService());
  }

  public static TranslationResult translateTo(TranslateService service, Language language, String text) {
    Translator translator = get(service);
    if (translator != null) {
      return translator.translateTo(language, text);
    }
    return null;
  }

  public static TranslationResult translateFrom(TranslateService service, Language source, Language target, String text) {
    Translator translator = get(service);
    if (translator != null) {
      return translator.translateFrom(source, target, text);
    }
    return null;
  }

  public static Translator get(TranslateService service) {
    return translatorServiceMap.get(service);
  }
}
