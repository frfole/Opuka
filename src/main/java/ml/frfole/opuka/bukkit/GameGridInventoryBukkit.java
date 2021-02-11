package ml.frfole.opuka.bukkit;

import ml.frfole.opuka.common.gamegrids.GameGrid;
import ml.frfole.opuka.common.GameGridInventory;
import ml.frfole.opuka.common.Opuka;
import ml.frfole.opuka.common.gamegrids.GameGridRS;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.Arrays;
import java.util.UUID;

public class GameGridInventoryBukkit extends GameGridInventory implements Listener {
  public static final int SLOT_INFO = 4;
  public static final int SLOT_RESET_START = 0;

  private final Inventory inv;
  private final int taskNumber;

  public GameGridInventoryBukkit(int size, UUID uuid, int minesCount) {
    this.inv = Bukkit.createInventory(null, size, "Opuka");
    Bukkit.getPluginManager().registerEvents((Listener) this, OpukaBukkit.getInstance());
    this.taskNumber = Bukkit.getScheduler().scheduleSyncRepeatingTask(OpukaBukkit.getInstance(), this::tick, 10, 10);
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
    inv.setItem(4, item);

    // (start | restart) item
    item = new ItemStack(gameGrid.getState() == GameGrid.State.READY ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER);
    ItemUtils.setName(item, gameGrid.getState() == GameGrid.State.READY
            ? "§2Start"
            : "§4Reset");
    inv.setItem(0, item);
  }

  @Override
  public void destroy() {
    Bukkit.getScheduler().cancelTask(this.taskNumber);
    this.inv.getViewers().forEach(p -> {
      if (p != null && !super.isOwner(p.getUniqueId()))
        p.closeInventory();
    });
  }

  @Override
  public void open(UUID uuid) {
    final Player p = Bukkit.getPlayer(uuid);
    if (p != null && p.isOnline()) p.openInventory(this.inv);
  }

  @EventHandler
  public void onClick(final InventoryClickEvent event) {
    // TODO move to own class
    final UUID performerId = event.getWhoClicked().getUniqueId();
    if (!event.getInventory().equals(inv)) return;
    event.setCancelled(true);
    if (!super.isOwner(performerId)) return;
    final int slot = event.getRawSlot();
    if (slot < 0 || slot > inv.getSize() - 1) return;
    if (event.isLeftClick()) {
      if (gameGrid.getState() != GameGrid.State.READY && slot == SLOT_RESET_START) {
        gameGrid.reset();
      } else {
        super.leftClick(slot, 9, performerId);
      }
    }
    else if (event.isRightClick()) {
      if (gameGrid.getState() != GameGrid.State.READY && slot == SLOT_RESET_START) {
        gameGrid.reset();
      } else {
        super.rightClick(slot, 9, performerId);
      }
    }
  }

  @EventHandler
  public void onClose(final InventoryCloseEvent event) {
    // TODO move to own class
    if (!event.getInventory().equals(inv)) return;
    final UUID uuid = event.getPlayer().getUniqueId();
    if (!super.isOwner(uuid)) return;
    Opuka.getInstance().methods.removePlayerGGI(uuid);
  }
}
