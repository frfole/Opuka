package ml.frfole.opuka.bukkit.commands;

import ml.frfole.opuka.common.commands.OpukaCommandBase;
import ml.frfole.opuka.common.Opuka;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class OpukaCommand extends OpukaCommandBase implements TabExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
    if (sender instanceof Player) {
      final UUID performer = ((Player) sender).getUniqueId();
      if (args.length == 0) {
        super.play(performer, 54, 8);
      }
      else if (args.length == 1) {
        if (args[0].equalsIgnoreCase("play") || args[0].equalsIgnoreCase("p")) {
          super.play(performer, 54, 8);
        }
      }
      else {
        if (args[0].equalsIgnoreCase("play") || args[0].equalsIgnoreCase("p")) {
          if (isInt(args[1])) {
            super.play(performer, 54, Integer.parseInt(args[1]));
          }
        }
        if (args[0].equalsIgnoreCase("spectate") || args[0].equalsIgnoreCase("s")) {
          final Player targetP = Bukkit.getPlayer(args[1]);
          if (targetP != null && targetP.isOnline()) {
            super.spectate(performer, targetP.getUniqueId());
          }
        }
      }
    }
    return true;
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
    if (args.length == 1) {
      return List.of("play", "spectate", "p", "s");
    }
    else if (args.length == 2) {
      if (args[0].equalsIgnoreCase("spectate") || args[0].equalsIgnoreCase("s")) {
        return Arrays.asList(getPlayingNames());
      }
    }
    return new ArrayList<>();
  }

  public static boolean isInt(final String str) {
    try {
      Integer.parseInt(str);
      return true;
    } catch(NumberFormatException e){
      return false;
    }
  }

  @Override
  protected String[] getPlayingNames() {
    final Set<UUID> uuidSet = Opuka.getInstance().methods.getPlayers();
    Set<String> names = new HashSet<>();
    for (UUID uuid : uuidSet) {
      final OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
      if (p.isOnline())
        names.add(p.getName());
    }
    return names.toArray(new String[0]);
  }
}
