package ml.frfole.opuka.bukkit;

import ml.frfole.opuka.common.gamegrid.GameGrid;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;
import java.util.List;

import static ml.frfole.opuka.common.gamegrid.GameGrid.FieldType.*;

/**
 * {@link ItemStack} utilities.
 */
public class ItemUtils {
  /**
   * Renames {@link ItemStack}.
   * @param item the {@link ItemStack}
   * @param name the name
   */
  public static void setName(ItemStack item, String name) {
    ItemMeta meta = item.getItemMeta();
    if (meta == null) return;
    meta.setDisplayName(name);
    item.setItemMeta(meta);
  }

  /**
   * Sets lore of {@link ItemStack}.
   * @param item the {@link ItemStack}
   * @param lore the lore
   */
  public static void setLore(ItemStack item, List<String> lore) {
    ItemMeta meta = item.getItemMeta();
    if (meta == null) return;
    meta.setLore(lore);
    item.setItemMeta(meta);
  }
}
