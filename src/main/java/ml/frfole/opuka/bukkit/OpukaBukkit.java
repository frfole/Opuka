package ml.frfole.opuka.bukkit;

import ml.frfole.opuka.bukkit.commands.OpukaCommand;
import ml.frfole.opuka.bukkit.listeners.InventoryListener;
import ml.frfole.opuka.common.Opuka;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

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
    HandlerList.unregisterAll(this.inventoryListener);
    Bukkit.getScheduler().cancelTask(this.taskTickN);
  }

  @Override
  public void onEnable() {
    super.onEnable();
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
  }
}
