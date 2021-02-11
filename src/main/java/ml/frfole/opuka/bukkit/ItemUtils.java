package ml.frfole.opuka.bukkit;

import ml.frfole.opuka.common.gamegrids.GameGrid;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * {@link ItemStack} utilities.
 */
public class ItemUtils {
  /**
   * Renames {@link ItemStack}.
   * @param item the {@link ItemStack}
   * @param name the name
   */
  public static void setName(@NotNull ItemStack item, @NotNull String name) {
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
  public static void setLore(@NotNull ItemStack item, @NotNull List<String> lore) {
    ItemMeta meta = item.getItemMeta();
    if (meta == null) return;
    meta.setLore(lore);
    item.setItemMeta(meta);
  }

  public static ItemStack getItem(GameGrid.FieldType type, GameGrid.State state) {
    ItemStack item = new ItemStack(Material.STONE);
    if (type.isUnknown()) {
      item = new ItemStack(state.isFinished() && type == GameGrid.FieldType.UNKNOWN_MINE
              ? Material.RED_STAINED_GLASS_PANE
              : Material.LIME_STAINED_GLASS_PANE);
      setName(item, state.isFinished() && type == GameGrid.FieldType.UNKNOWN_MINE ? "Mine" : "Unknown");
      setLore(item,
              Arrays.asList("Right click to flag.", "Left click to dig."));
      return item;
    }
    else if (type.isFlagged()) {
      item = new ItemStack(state.isFinished() && type == GameGrid.FieldType.UNKNOWN_FLAG_MINE
              ? Material.RED_STAINED_GLASS_PANE
              : Material.BLACK_STAINED_GLASS_PANE);
      setName(item, state.isFinished() && type == GameGrid.FieldType.UNKNOWN_FLAG_MINE ? "Mine" : "Flag");
      setLore(item, Arrays.asList("Right click to un-flag."));
      return item;
    }
    else if (type.isKnown()) {
      if (type == GameGrid.FieldType.MINE)
        return new ItemStack(Material.RED_STAINED_GLASS_PANE);
      int count = 0;
      switch (type) {
        case NEAR_1: count = 1; break;
        case NEAR_2: count = 2; break;
        case NEAR_3: count = 3; break;
        case NEAR_4: count = 4; break;
        case NEAR_5: count = 5; break;
        case NEAR_6: count = 6; break;
        case NEAR_7: count = 7; break;
        case NEAR_8: count = 8; break;
        default: break;
      }
      item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, count);
      setName(item, count + " mine(s) nearby");
      setLore(item, Arrays.asList("There is nearby " + count + " mine(s)"));
      return item;
    }
    return item;
  }
}
