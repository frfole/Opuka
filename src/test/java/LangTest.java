import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import ml.frfole.opuka.common.LangManager;
import ml.frfole.opuka.common.Opuka;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LangTest {

  @Before
  public void before() {
    new Opuka(
            new Opuka.Methods() {

            },
            new File("src/test/resources"));
  }

  @Test
  public void readTest() {
    Map<String, String> map = new HashMap<>();
    map.put("author", "frfole");
    Assert.assertEquals(true, Opuka.getInstance().getLangManager().load());
    Assert.assertEquals("frfole", Opuka.getInstance().getLangManager().get("opuka.test.alfa"));
    Assert.assertEquals("RNG", Opuka.getInstance().getLangManager().get("game"));
    Assert.assertEquals("Opuka is made by frfole.", Opuka.getInstance().getLangManager().get("opuka.placeholder.test", map));
    Assert.assertEquals("§4Unable to locate key §9not existing§4!", Opuka.getInstance().getLangManager().get("not existing"));
  }
}
