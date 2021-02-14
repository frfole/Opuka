package ml.frfole.opuka.bukkit.inventory;

import ml.frfole.opuka.bukkit.ItemUtils;
import ml.frfole.opuka.common.gamegrid.GameGrid;
import ml.frfole.opuka.common.inventory.GameGridInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static ml.frfole.opuka.common.gamegrid.GameGrid.FieldType.*;

public class GGInvBukkit extends GameGridInventory {
  private final Inventory inv;

  public GGInvBukkit(UUID uuid, int minesCount) {
    super(uuid, 6, 9, minesCount);
    this.inv = Bukkit.createInventory(null, 54, invName);
    this.open(uuid);
  }

  public static ItemStack getItem(GameGrid.FieldType type, GameGrid.State state) {
    ItemStack item = new ItemStack(Material.STONE);
    if (type.isUnknown()) {
      item = new ItemStack(state.isFinished() && type == UNKNOWN_MINE
              ? Material.RED_STAINED_GLASS_PANE
              : Material.LIME_STAINED_GLASS_PANE);
      ItemUtils.setRName(item, state.isFinished() && type == UNKNOWN_MINE ? "Mine" : "Unknown");
      ItemUtils.setRLore(item,
              Arrays.asList("Right click to flag.", "Left click to dig."));
      return item;
    }
    else if (type.isFlagged()) {
      item = new ItemStack(state.isFinished() && type == UNKNOWN_FLAG_MINE
              ? Material.RED_STAINED_GLASS_PANE
              : Material.BLACK_STAINED_GLASS_PANE);
      ItemUtils.setRName(item, state.isFinished() && type == UNKNOWN_FLAG_MINE ? "Mine" : "Flag");
      ItemUtils.setRLore(item, Arrays.asList("Right click to un-flag."));
      return item;
    }
    else if (type.isKnown()) {
      if (type == MINE)
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
      ItemUtils.setRName(item, count + " mine(s) nearby");
      ItemUtils.setRLore(item, Arrays.asList("There is nearby " + count + " mine(s)"));
      return item;
    }
    return item;
  }

  @Override
  public void update() {
    final GameGrid.FieldType[][] grid = gameGrid.getGrid();
    final Map<String, String> placeholders = new HashMap<>();
    placeholders.put("mines", String.valueOf(gameGrid.getMinesCount()));
    placeholders.put("spec", String.valueOf(inv.getViewers().size() - 1));
    placeholders.put("state", gameGrid.getState().getName());
    placeholders.put("time", dateFormatter.format(new Date((gameGrid.getState() != GameGrid.State.PLAYING ? gameGrid.getTimeEnd() : System.currentTimeMillis()) - gameGrid.getTimeStart())));

    for (int y = 0; y < gameGrid.getHeight(); y++) {
      for (int x = 0; x < gameGrid.getWidth(); x++) {
        this.inv.setItem(y*9 + x, getItem(grid[y][x], gameGrid.getState()));
      }
    }
    // time item
    ItemStack item = new ItemStack(Material.PAPER);
    ItemUtils.setName(item, "opuka.inventory.gamegrid.item.info.name", placeholders);
    ItemUtils.setLore(item, "opuka.inventory.gamegrid.item.info.lore", placeholders);
    inv.setItem(SLOT_INFO, item);

    // (start | restart) item
    item = new ItemStack(gameGrid.getState() == GameGrid.State.READY ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER);
    ItemUtils.setName(item, "opuka.inventory.gamegrid.item."
            + (gameGrid.getState() == GameGrid.State.READY ? "start" : "reset")
            + ".name", placeholders);
    ItemUtils.setLore(item, "opuka.inventory.gamegrid.item."
            + (gameGrid.getState() == GameGrid.State.READY ? "start" : "reset")
            + ".lore", placeholders);
    inv.setItem(SLOT_RESET_START, item);
  }

  @Override
  public void destroy() {
    final List<HumanEntity> viewers = new ArrayList<>(this.inv.getViewers());
    viewers.forEach(p -> {
      if (p != null)
        p.closeInventory();
    });
  }

  @Override
  public void open(UUID uuid) {
    final Player p = Bukkit.getPlayer(uuid);
    if (p != null && p.isOnline()) p.openInventory(this.inv);
  }

  @Override
  public Object getInventory() {
    return this.inv;
  }
}
