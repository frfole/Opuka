package ml.frfole.opuka.minestom.inventory;

import ml.frfole.opuka.common.gamegrid.GameGrid;
import ml.frfole.opuka.common.inventory.GameGridInventory;
import ml.frfole.opuka.minestom.ItemUtils;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static ml.frfole.opuka.common.gamegrid.GameGrid.FieldType.*;

public class GGInvMinestom extends GameGridInventory {
  private final Inventory inv;

  public GGInvMinestom(UUID ownerId, int minesCount) {
    super(ownerId, 6, 9, minesCount);
    this.inv = new Inventory(InventoryType.CHEST_6_ROW, this.invName);
    this.open(ownerId);
  }

  @Override
  public void destroy() {
    Set<Player> players = inv.getViewers();
    players.forEach(player -> {
      if (player != null)
        player.closeInventory();
    });
  }

  @Override
  public void open(UUID target) {
    final Player player = MinecraftServer.getConnectionManager().getPlayer(target);
    if (player != null && player.isOnline()) {
      player.openInventory(this.inv);
    }
  }

  @Override
  public void update() {
    GameGrid.FieldType[][] grid = gameGrid.getGrid();
    long timeDelta = (gameGrid.getState() != GameGrid.State.PLAYING ? gameGrid.getTimeEnd() : System.currentTimeMillis()) - gameGrid.getTimeStart();
    Map<String, String> placeholders = new HashMap<>();
    placeholders.put("mines", String.valueOf(gameGrid.getMinesCount()));
    placeholders.put("spec", String.valueOf(inv.getViewers().size() - 1));
    placeholders.put("state", gameGrid.getState().getName());
    placeholders.put("time", ((timeDelta / 60000) % 60) + ":" + ((timeDelta / 1000) % 60 + "." + (timeDelta % 1000)));

    for (int y = 0; y < gameGrid.getHeight(); y++) {
      for (int x = 0; x < gameGrid.getWidth(); x++) {
        if (grid[y][x] != INVALID)
          inv.setItemStack(y*9 + x, getItem(grid[y][x], gameGrid.getState()));
      }
    }

    ItemStack item = new ItemStack(Material.OAK_SIGN, (byte)1);
    ItemUtils.setName(item, "opuka.inventory.gamegrid.item.info.name", placeholders);
    ItemUtils.setLore(item, "opuka.inventory.gamegrid.item.info.lore", placeholders);
    inv.setItemStack(SLOT_INFO, item);

    item = new ItemStack(gameGrid.getState() == GameGrid.State.READY ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER, (byte)1);
    ItemUtils.setName(item, "opuka.inventory.gamegrid.item."
            + (gameGrid.getState() == GameGrid.State.READY ? "start" : "reset")
            + ".name", placeholders);
    ItemUtils.setLore(item, "opuka.inventory.gamegrid.item."
            + (gameGrid.getState() == GameGrid.State.READY ? "start" : "reset")
            + ".lore", placeholders);
    inv.setItemStack(SLOT_RESET_START, item);
  }

  private ItemStack getItem(GameGrid.FieldType type, GameGrid.State state) {
    ItemStack item;
    if (type.isUnknown()) {
      item = new ItemStack(state.isFinished() && type == UNKNOWN_MINE ? Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE, (byte)1);
      ItemUtils.setRName(item, state.isFinished() && type == UNKNOWN_MINE ? "Mine" : "Unknown");
      ItemUtils.setRLore(item, "Right click to flag.", "Left click to dig.");
      return item;
    } else if (type.isFlagged()) {
      item = new ItemStack(state.isFinished() && type == UNKNOWN_FLAG_MINE ? Material.RED_STAINED_GLASS_PANE : Material.BLACK_STAINED_GLASS_PANE, (byte)1);
      ItemUtils.setRName(item, state.isFinished() && type == UNKNOWN_FLAG_MINE ? "Mine" : "Flag");
      ItemUtils.setRLore(item, "Right click to un-flag.");
      return item;
    } else if (type.isKnown()) {
      if (type == MINE)
        return new ItemStack(Material.RED_STAINED_GLASS_PANE, (byte)1);
      byte count = 0;
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
      ItemUtils.setRLore(item, "There is nearby " + count + " mine(s)");
      return item;
    } else if (type == INVALID) {
      item = ItemStack.getAirItem();
    } else {
      item = new ItemStack(Material.STONE, (byte)1);
    }
    return item;
  }

  @Override
  public Object getInventory() {
    return this.inv;
  }
}
