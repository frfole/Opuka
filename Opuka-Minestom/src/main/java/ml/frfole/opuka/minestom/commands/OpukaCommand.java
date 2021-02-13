package ml.frfole.opuka.minestom.commands;

import ml.frfole.opuka.common.Opuka;
import ml.frfole.opuka.common.commands.OpukaCommandBase;
import ml.frfole.opuka.minestom.inventory.CInvMinestom;
import ml.frfole.opuka.minestom.inventory.GGInvMinestom;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class OpukaCommand extends Command {
  OpukaCommandImpl base = new OpukaCommandImpl();

  ArgumentWord actionPlay = ArgumentType.Word("action-p").from("play", "p");
  ArgumentWord actionPlayConfig = ArgumentType.Word("action-p-c").from("config", "c");
  ArgumentNumber<Integer> actionPlayInt = ArgumentType.Integer("action-p-i").between(0, 52);
  ArgumentWord actionSpec = ArgumentType.Word("action-s").from("spectate", "s");
  ArgumentWord actionSpecName = ArgumentType.Word("action-s-n");

  public OpukaCommand() {
    super("opuka");

    setDefaultExecutor((sender, args) -> {
      if (sender.isPlayer()) {
        base.play(sender.asPlayer().getUuid(), 54, 8);
      } else {
        sender.sendMessage(Opuka.getInstance().getLangManager().get("opuka.command.opuka.error.for_players_only").split("\n"));
      }
    });

    addSyntax((sender, args) -> {
      if (sender.isPlayer()) {
        base.play(sender.asPlayer().getUuid(), 54, 8);
      } else {
        sender.sendMessage(Opuka.getInstance().getLangManager().get("opuka.command.opuka.error.for_players_only").split("\n"));
      }
    }, actionPlay);

    addSyntax((sender, args) -> {
      if (sender.isPlayer()) {
        base.config(sender.asPlayer().getUuid());
      } else {
        sender.sendMessage(Opuka.getInstance().getLangManager().get("opuka.command.opuka.error.for_players_only").split("\n"));
      }
    }, actionPlay, actionPlayConfig);
    addSyntax((sender, args) -> {
      if (sender.isPlayer()) {
        base.play(sender.asPlayer().getUuid(), 54, args.get(actionPlayInt));
      } else {
        sender.sendMessage(Opuka.getInstance().getLangManager().get("opuka.command.opuka.error.for_players_only").split("\n"));
      }
    }, actionPlay, actionPlayInt);

    addSyntax((sender, args) -> {
      final Player targetP = MinecraftServer.getConnectionManager().findPlayer(args.get(actionSpecName));
      if (sender.isPlayer()) {
        if (targetP != null && targetP.isOnline()) {
          base.spectate(sender.asPlayer().getUuid(), targetP.getUuid());
        } else {
          sender.sendMessage(Opuka.getInstance().getLangManager().get("opuka.command.opuka.error.target_not_found").split("\n"));
        }
      } else {
        sender.sendMessage(Opuka.getInstance().getLangManager().get("opuka.command.opuka.error.for_players_only").split("\n"));
      }
    }, actionSpec, actionSpecName);

  }

  public void update() {
    actionSpecName = actionSpecName.from(base.getPlayingNames());
  }

  private class OpukaCommandImpl extends OpukaCommandBase {

    @Override
    public void config(UUID performer) {
      Opuka.getInstance().methods.setPlayerCI(performer, new CInvMinestom(performer));
    }

    @Override
    public void play(UUID performer, int size, int minesCount) {
      Opuka.getInstance().methods.setPlayerGGI(performer, new GGInvMinestom(size, performer, minesCount));
    }

    @Override
    public void spectate(UUID id, UUID targetId) {
      super.spectate(id, targetId);
    }

    @Override
    protected String[] getPlayingNames() {
      final Set<UUID> uuidSet = Opuka.getInstance().methods.getPlayers();
      Set<String> names = new HashSet<>();
      for (UUID uuid : uuidSet) {
        final Player p = MinecraftServer.getConnectionManager().getPlayer(uuid);
        if (p != null && p.isOnline())
          names.add(p.getUsername());
      }
      return names.toArray(new String[0]);
    }
  }
}
