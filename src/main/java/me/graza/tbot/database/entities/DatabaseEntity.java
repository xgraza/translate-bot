package me.graza.tbot.database.entities;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author graza
 * @since 08/16/23
 */
public class DatabaseEntity {

  private final Map<String, Field> reflectedFieldCache = new HashMap<>();

  public DatabaseEntity() {
    for (Field field : getClass().getDeclaredFields()) {
      reflectedFieldCache.put(field.getName(), field);
    }
  }

  public void set(String fieldName, Object value) {
    Field field = getField(fieldName);
    if (field != null) {
      try {
        field.set(this, value);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  public <T> T get(String fieldName, T defaultValue) {
    Field field = getField(fieldName);
    if (field != null) {
      try {
        return (T) field.get(this);
      } catch (IllegalAccessException ignored) {
        return defaultValue;
      }
    }
    return defaultValue;
  }

  public Set<String> keys() {
    return reflectedFieldCache.keySet();
  }

  private Field getField(String fieldName) {
    return reflectedFieldCache.getOrDefault(fieldName, null);
  }
}
