package ml.frfole.opuka.bukkit.inventory;

import ml.frfole.opuka.bukkit.ItemUtils;
import ml.frfole.opuka.common.gamegrid.GameGrid;
import ml.frfole.opuka.common.inventory.GameGridInventory;
import ml.frfole.opuka.common.gamegrid.GameGridRS;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GGInvBukkit extends GameGridInventory {
  public static final int SLOT_INFO = 4;
  public static final int SLOT_RESET_START = 0;

  private final Inventory inv;

  public GGInvBukkit(int size, UUID uuid, int minesCount) {
    this.inv = Bukkit.createInventory(null, size, "Opuka");
    this.ownerId = uuid;
    this.gameGrid = new GameGridRS(6, 9);
    this.gameGrid.setInvalid(0, 0); // start / reset
    this.gameGrid.setInvalid(4, 0); // time
    this.gameGrid.populateWithMines(minesCount);
    this.open(uuid);
  }

  @Override
  public void update() {
    GameGrid.FieldType[][] grid = gameGrid.getGrid();
    long timeDelta = (gameGrid.getState() != GameGrid.State.PLAYING ? gameGrid.getTimeEnd() : System.currentTimeMillis()) - gameGrid.getTimeStart();
    for (int y = 0; y < gameGrid.getHeight(); y++) {
      for (int x = 0; x < gameGrid.getWidth(); x++) {
        this.inv.setItem(y*9 + x, ItemUtils.getItem(grid[y][x], gameGrid.getState()));
      }
    }
    // time item
    ItemStack item = new ItemStack(Material.SIGN);
    ItemUtils.setName(item, "Info");
    ItemUtils.setLore(item, Arrays.asList(
            "§7Time: " + ((timeDelta / 60000) % 60 ) + ":" + ((timeDelta / 1000) % 60 + "." + (timeDelta % 1000)),
            "§7Mines count: " + gameGrid.getMinesCount(),
            "§7Watchers count: " + (inv.getViewers().size() - 1)
    ));
    inv.setItem(SLOT_INFO, item);

    // (start | restart) item
    item = new ItemStack(gameGrid.getState() == GameGrid.State.READY ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER);
    ItemUtils.setName(item, gameGrid.getState() == GameGrid.State.READY
            ? "§2Start"
            : "§4Reset");
    inv.setItem(SLOT_RESET_START, item);
  }

  @Override
  public void destroy() {
    List<HumanEntity> viewers = new ArrayList<>(this.inv.getViewers());
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
