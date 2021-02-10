package ml.frfole.opuka.bukkit;

import ml.frfole.opuka.bukkit.commands.OpukaCommand;
import ml.frfole.opuka.common.Opuka;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class OpukaBukkit extends JavaPlugin {
  private static OpukaBukkit instance;

  public OpukaBukkit() {
    OpukaBukkit.instance = this;
  }

  @Override
  public void onDisable() {
    super.onDisable();
  }

  @Override
  public void onEnable() {
    super.onEnable();
    new Opuka(new BukkitMethods(this, System.currentTimeMillis()));
    Bukkit.getPluginCommand("opuka").setExecutor(new OpukaCommand());
  }

  public static OpukaBukkit getInstance() {
    return OpukaBukkit.instance;
  }
}
