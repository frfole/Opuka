package ml.frfole.opuka.bukkit;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * {@link ItemStack} utilities.
 */
public class ItemUtils {
  /**
   * Renames {@link ItemStack}.
   * @param item the {@link ItemStack}
   * @param name the name
   * @return renamed {@link ItemStack}
   */
  public static ItemStack setName(@NotNull ItemStack item, @NotNull String name) {
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(name);
    item.setItemMeta(meta);
    return item;
  }

  /**
   * Sets lore of {@link ItemStack}.
   * @param item the {@link ItemStack}
   * @param lore the lore
   * @return {@link ItemStack} with lore
   */
  public static ItemStack setLore(@NotNull ItemStack item, @NotNull List<String> lore) {
    ItemMeta meta = item.getItemMeta();
    meta.setLore(lore);
    item.setItemMeta(meta);
    return item;
  }
}
