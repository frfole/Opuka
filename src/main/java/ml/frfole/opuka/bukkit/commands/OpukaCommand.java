package ml.frfole.opuka.bukkit.commands;

import ml.frfole.opuka.bukkit.GameGridInventoryBukkit;
import ml.frfole.opuka.common.GameGridInventory;
import ml.frfole.opuka.common.Opuka;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OpukaCommand implements TabExecutor {
  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
    if (commandSender instanceof Player) {
      Player p = (Player) commandSender;
      GameGridInventory ggi = new GameGridInventoryBukkit(54, p.getUniqueId());
      Opuka.getInstance().methods.setPlayerGGI(p.getUniqueId(), ggi);
    }
    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
    return new ArrayList<>();
  }
}
