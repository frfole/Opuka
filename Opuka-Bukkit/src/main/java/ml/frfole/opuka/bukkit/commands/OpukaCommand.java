package ml.frfole.opuka.bukkit.commands;

import ml.frfole.opuka.bukkit.inventory.CInvBukkit;
import ml.frfole.opuka.bukkit.inventory.GGInvBukkit;
import ml.frfole.opuka.common.commands.OpukaCommandBase;
import ml.frfole.opuka.common.Opuka;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

public class OpukaCommand extends OpukaCommandBase implements TabExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
    if (sender instanceof Player) {
      final UUID performer = ((Player) sender).getUniqueId();
      if (args.length == 0) {
        play(performer, 54, 8);
      } else if (args.length == 1) {
        if (args[0].equalsIgnoreCase("play") || args[0].equalsIgnoreCase("p")) {
          play(performer, 54, 8);
        } else {
          sender.sendMessage(Opuka.getInstance().getLangManager().get("opuka.command.opuka.usage.1").split("\n"));
        }
      } else {
        if (args[0].equalsIgnoreCase("play") || args[0].equalsIgnoreCase("p")) {
          if (isInt(args[1])) {
            play(performer, 54, Integer.parseInt(args[1]));
          } else if (args[1].equalsIgnoreCase("c") || args[1].equalsIgnoreCase("config")) {
            config(performer);
          } else {
            sender.sendMessage(Opuka.getInstance().getLangManager().get("opuka.command.opuka.usage.p").split("\n"));
          }
        } else if (args[0].equalsIgnoreCase("spectate") || args[0].equalsIgnoreCase("s")) {
          final Player targetP = Bukkit.getPlayer(args[1]);
          if (targetP != null && targetP.isOnline()) {
            spectate(performer, targetP.getUniqueId());
          } else {
            sender.sendMessage(Opuka.getInstance().getLangManager().get("opuka.command.opuka.error.target_not_found").split("\n"));
          }
        } else {
          sender.sendMessage(Opuka.getInstance().getLangManager().get("opuka.command.opuka.usage.1").split("\n"));
        }
      }
    } else {
      sender.sendMessage(Opuka.getInstance().getLangManager().get("opuka.command.opuka.error.for_players_only").split("\n"));
    }
    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    if (args.length == 1) {
      return List.of("", "play", "spectate", "p", "s");
    }
    else if (args.length == 2) {
      if (args[0].equalsIgnoreCase("spectate") || args[0].equalsIgnoreCase("s")) {
        return Arrays.asList(getPlayingNames());
      }
      else if (args[0].equalsIgnoreCase("play") || args[0].equalsIgnoreCase("p")) {
        return Arrays.asList("", "c", "config");
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
  protected void config(UUID performer) {
    Opuka.getInstance().methods.setPlayerCI(performer, new CInvBukkit(performer));
  }

  @Override
  protected void play(UUID performer, int size, int minesCount) {
    Opuka.getInstance().methods.setPlayerGGI(performer, new GGInvBukkit(size, performer, minesCount));
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
