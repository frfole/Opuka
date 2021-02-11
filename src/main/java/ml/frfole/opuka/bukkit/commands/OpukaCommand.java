package ml.frfole.opuka.bukkit.commands;

import ml.frfole.opuka.bukkit.GameGridInventoryBukkit;
import ml.frfole.opuka.common.GameGridInventory;
import ml.frfole.opuka.common.Opuka;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OpukaCommand implements TabExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
    if (sender instanceof Player) {
      final UUID performer = ((Player) sender).getUniqueId();
      if (args.length == 0) {
        GameGridInventory ggi = new GameGridInventoryBukkit(54, performer, 8);
        Opuka.getInstance().methods.setPlayerGGI(performer, ggi);
      }
      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("play") || args[0].equalsIgnoreCase("p")) {
          GameGridInventory ggi = new GameGridInventoryBukkit(54, performer, 8);
          Opuka.getInstance().methods.setPlayerGGI(performer, ggi);
        }
      }
      if (args.length >= 2) {
        if (args[0].equalsIgnoreCase("play") || args[0].equalsIgnoreCase("p")) {
          if (isInt(args[1])) {
            final int mineCount = Integer.parseInt(args[1]);
            GameGridInventory ggi = new GameGridInventoryBukkit(54, performer, mineCount);
            Opuka.getInstance().methods.setPlayerGGI(performer, ggi);
          }
        }
        if (args[0].equalsIgnoreCase("spectate") || args[0].equalsIgnoreCase("s")) {
          final Player targetP = Bukkit.getPlayer(args[1]);
          if (targetP != null && targetP.isOnline()) {
            final UUID targetId = targetP.getUniqueId();
            Opuka.getInstance().methods.setPlayerGGI(performer, targetId);
          }
        }
      }
    }
    return true;
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {

    return new ArrayList<>();
  }

  public static boolean isInt(String str) {
    try {
      Integer.parseInt(str);
      return true;
    } catch(NumberFormatException e){
      return false;
    }
  }
}
