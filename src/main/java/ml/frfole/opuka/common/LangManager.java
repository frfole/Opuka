package ml.frfole.opuka.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LangManager {
  private static final Type LANG_MAP_TYPE = new TypeToken<Map<String, String>>(){}.getType();
  public static final String MSG_KEY_NOT_FOUND = "§4Unable to locate key §9%key%§4!";

  private final Gson gson = new Gson();
  private final Map<String, String> stringMap = new HashMap<>();

  public boolean load() {
    try (final JsonReader reader = new JsonReader(new FileReader(new File(Opuka.getInstance().getDataFolder(), "lang.json"), StandardCharsets.UTF_8))) {
      stringMap.clear();
      stringMap.putAll(gson.fromJson(reader, LANG_MAP_TYPE));
      return true;
    } catch (IOException ignored) {
      return false;
    }
  }

  public String get(final String key) {
    final String s = stringMap.get(key);
    return s == null ? MSG_KEY_NOT_FOUND.replaceAll("%key%", key) : s;
  }

  public String get(String key, final Map<String, String> placeholders) {
    String s = get(key);
    for (final Map.Entry<String, String> p : placeholders.entrySet()) {
      s = s.replaceAll("%" + p.getKey() + "%", p.getValue());
    }
    return s;
  }

}
