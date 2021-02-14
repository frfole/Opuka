package ml.frfole.opuka.minestom;

import ml.frfole.opuka.common.Opuka;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.item.ItemStack;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemUtils {
  public static void setLore(ItemStack item, String path) {
    item.setLore(Arrays.stream(Opuka.getInstance().getLangManager().get(path).split("\n")).map(ColoredText::of).collect(Collectors.toList()));
  }

  public static void setName(ItemStack item, String path) {
    item.setDisplayName(ColoredText.of(Opuka.getInstance().getLangManager().get(path)));
  }

  public static void setLore(ItemStack item, String path, Map<String, String> placeholders) {
    item.setLore(Arrays.stream(Opuka.getInstance().getLangManager().get(path, placeholders).split("\n")).map(ColoredText::of).collect(Collectors.toList()));
  }

  public static void setName(ItemStack item, String path, Map<String, String> placeholders) {
    item.setDisplayName(ColoredText.of(Opuka.getInstance().getLangManager().get(path, placeholders)));
  }

  public static void setRName(ItemStack item, String name) {
    item.setDisplayName(ColoredText.of(name));
  }

  public static void setRLore(ItemStack item, String... lore) {
    item.setLore(Arrays.stream(lore).map(ColoredText::of).collect(Collectors.toList()));
  }
}
