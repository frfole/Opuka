package ml.frfole.opuka.bukkit;

import ml.frfole.opuka.bukkit.commands.OpukaCommand;
import ml.frfole.opuka.bukkit.events.GameFinishEvent;
import ml.frfole.opuka.bukkit.inventory.CInvBukkit;
import ml.frfole.opuka.bukkit.inventory.GGInvBukkit;
import ml.frfole.opuka.bukkit.listeners.InventoryListener;
import ml.frfole.opuka.common.Opuka;
import ml.frfole.opuka.common.gamegrid.GameGrid;
import ml.frfole.opuka.common.inventory.ConfigInventory;
import ml.frfole.opuka.common.inventory.GameGridInventory;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.UUID;

public class OpukaBukkit extends JavaPlugin {
  private static OpukaBukkit instance;

  protected final InventoryListener inventoryListener;
  protected final OpukaCommand opukaCommand;

  private int taskTickN;

  public OpukaBukkit() {
    OpukaBukkit.instance = this;
    this.inventoryListener = new InventoryListener();
    this.opukaCommand = new OpukaCommand();
  }

  @Override
  public void onDisable() {
    super.onDisable();
    Opuka.getInstance().methods.shutdown();
    HandlerList.unregisterAll(this.inventoryListener);
    Bukkit.getScheduler().cancelTask(this.taskTickN);
  }

  @Override
  public void onEnable() {
    super.onEnable();
    this.saveResource("lang.json", false);
    new Opuka(new BukkitMethods(System.currentTimeMillis()), this.getDataFolder());
    Bukkit.getPluginCommand("opuka").setExecutor(this.opukaCommand);
    Bukkit.getPluginManager().registerEvents(this.inventoryListener, this);
    this.taskTickN = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, Opuka.getInstance().methods::tick, 5, 5);
  }


  public static OpukaBukkit getInstance() {
    return OpukaBukkit.instance;
  }

  public static class BukkitMethods extends Opuka.Methods {

    public BukkitMethods(long seed) {
      this.random = new Random(seed);
    }

    @Override
    public ConfigInventory createCI(UUID owner) {
      return new CInvBukkit(owner);
    }

    @Override
    public GameGridInventory createGGI(UUID owner, int minesCount) {
      return new GGInvBukkit(owner, minesCount);
    }

    @Override
    public void callEventGameFinish(GameGrid gameGrid, UUID owner) {
      Bukkit.getPluginManager().callEvent(new GameFinishEvent(gameGrid, owner));
    }

    @Override
    public void shutdown() {
      super.shutdown();
      if (OpukaBukkit.instance.isEnabled())
        Bukkit.getPluginManager().disablePlugin(OpukaBukkit.getInstance());
    }
  }
}
