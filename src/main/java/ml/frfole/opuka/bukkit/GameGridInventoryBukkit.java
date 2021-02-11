package ml.frfole.opuka.bukkit;

import ml.frfole.opuka.common.gamegrids.GameGrid;
import ml.frfole.opuka.common.GameGridInventory;
import ml.frfole.opuka.common.Opuka;
import ml.frfole.opuka.common.gamegrids.GameGridRS;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GameGridInventoryBukkit implements GameGridInventory, Listener {
  private final Inventory inv;
  private final Player player;
  private final GameGrid gameGrid;
  private long startTime = 0;
  private final int taskNumber;

  public GameGridInventoryBukkit(int size, Player player) {
    inv = Bukkit.createInventory(null, size, "Opuka");
    Bukkit.getPluginManager().registerEvents(this, OpukaBukkit.getInstance());
    this.taskNumber = Bukkit.getScheduler().scheduleSyncRepeatingTask(OpukaBukkit.getInstance(), this::tick, 10, 10);
    this.player = player;
    this.gameGrid = new GameGridRS(6, 9);
    this.gameGrid.setInvalid(0, 0); // start / reset
    this.gameGrid.setInvalid(4, 0); // time
    this.gameGrid.populateWithMines(5);
    update();
  }

  private void tick() {
    this.update();
  }

  @Override
  public void open() {
    this.player.openInventory(this.inv);
  }

  @Override
  public void close() {
    Bukkit.getScheduler().cancelTask(this.taskNumber);
  }

  @Override
  public void update() {
    // TODO update logic
    GameGrid.FieldType[][] grid = gameGrid.getGrid();
    boolean isFinishedMine = gameGrid.getState() == GameGrid.State.FINISHED_MINE;
    boolean isFinishedWin = gameGrid.getState() == GameGrid.State.FINISHED_WIN;
    long timeDelta = System.currentTimeMillis() - startTime;
    ItemStack item;
    ItemMeta meta;
    for (int y = 0; y < gameGrid.getHeight(); y++) {
      for (int x = 0; x < gameGrid.getWidth(); x++) {
        GameGrid.FieldType v = grid[y][x];
        if (v.isUnknown()) {
          item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
          if (isFinishedMine && v == GameGrid.FieldType.UNKNOWN_MINE)
            item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
          if (isFinishedWin && v == GameGrid.FieldType.UNKNOWN_MINE)
            item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        }/* else if (v == FieldType.CLEAR) {
          item = new ItemStack(Material.AIR);
        } else if (11 <= v && v <= 18) {
          item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, v - 10);
        } else if (v == 19) {
          item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        } else if (20 <= v && v <= 29) {
          item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
          if (isFinishedWin && v == 29)
            item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        }
        else {
          item = new ItemStack(Material.RED_NETHER_BRICKS);
        }
        inv.setItem(y*9 + x, item);*/
      }
    }
    // time item
    item = new ItemStack(Material.SIGN);
    ItemUtils.setName(item, "Info");
    ItemUtils.setLore(item, Arrays.asList(
            "§7Time: " + ((timeDelta / 60000) % 60 ) + ":" + ((timeDelta / 1000) % 60),
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

  @EventHandler
  public void onClick(org.bukkit.event.inventory.InventoryClickEvent event) {
    if (!event.getInventory().equals(inv) || !event.getWhoClicked().equals(player)) return;
    event.setCancelled(true);
    int slot = event.getRawSlot();
    if (slot < 0 || slot > inv.getSize() - 1) return;
    if (event.getClick().isLeftClick()) {
      if (gameGrid.getState() == GameGrid.State.READY) this.startTime = System.currentTimeMillis();
      if (gameGrid.getState() != GameGrid.State.READY && slot == 0) {
        gameGrid.reset();
        this.update();
        return;
      }
      gameGrid.dig(slot%9, slot/9);
      if (gameGrid.getState() == GameGrid.State.FINISHED_MINE) System.out.println(event.getWhoClicked().getName() + " clicked on mine!");
      if (gameGrid.getState() == GameGrid.State.FINISHED_WIN) System.out.println(event.getWhoClicked().getName() + " finished without exploding!");
      this.update();
    }
    else if (event.isRightClick()) {
      if (gameGrid.getState() == GameGrid.State.READY) this.startTime = System.currentTimeMillis();
      gameGrid.flag(slot%9, slot/9);
      this.update();
    }
  }

  @EventHandler
  public void onClose(final InventoryCloseEvent event) {
    if (!event.getInventory().equals(inv)) return;
    Opuka.getInstance().methods.removePlayerGGI(event.getPlayer().getUniqueId());
  }
}
