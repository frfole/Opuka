package ml.frfole.opuka.bukkit;

import ml.frfole.opuka.common.Opuka;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * {@link ItemStack} utilities.
 */
public class ItemUtils {
  /**
   * Renames {@link ItemStack}.
   * @param item the {@link ItemStack}
   * @param name the name
   */
  public static void setRName(ItemStack item, String name) {
    final ItemMeta meta = item.getItemMeta();
    if (meta == null) return;
    meta.setDisplayName(name);
    item.setItemMeta(meta);
  }

  /**
   * Sets lore of {@link ItemStack}.
   * @param item the {@link ItemStack}
   * @param lore the lore
   */
  public static void setRLore(ItemStack item, List<String> lore) {
    final ItemMeta meta = item.getItemMeta();
    if (meta == null) return;
    meta.setLore(lore);
    item.setItemMeta(meta);
  }

  public static void setName(ItemStack item, String path) {
    setRName(item, Opuka.getInstance().getLangManager().get(path));
  }

  public static void setName(ItemStack item, String path, Map<String, String> placeholders) {
    setRName(item, Opuka.getInstance().getLangManager().get(path, placeholders));
  }

  public static void setLore(ItemStack item, String path) {
    setRLore(item, Arrays.asList(Opuka.getInstance().getLangManager().get(path).split("\n")));
  }

  public static void setLore(ItemStack item, String path, Map<String, String> placeholders) {
    setRLore(item, Arrays.asList(Opuka.getInstance().getLangManager().get(path, placeholders).split("\n")));
  }
}
