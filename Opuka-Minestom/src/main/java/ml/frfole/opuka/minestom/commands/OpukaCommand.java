package ml.frfole.opuka.minestom.commands;

import ml.frfole.opuka.common.Opuka;
import ml.frfole.opuka.common.commands.OpukaCommandBase;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
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

  final protected ArgumentWord actionPlay = ArgumentType.Word("action-p").from("play", "p");
  final protected ArgumentWord actionPlayConfig = ArgumentType.Word("action-p-c").from("config", "c");
  final protected ArgumentNumber<Integer> actionPlayInt = ArgumentType.Integer("action-p-i").between(0, 52);
  final protected ArgumentWord actionSpec = ArgumentType.Word("action-s").from("spectate", "s");
  protected ArgumentWord actionSpecName = ArgumentType.Word("action-s-n");

  public OpukaCommand() {
    super("opuka", "minesweeper", "mines");

    setDefaultExecutor((sender, args) -> base.play(sender.asPlayer().getUuid(), 8));
    setCondition(this::playerOnly);

    // (p|play)
    addSyntax(this::playerOnly, (sender, args) -> base.play(sender.asPlayer().getUuid(), 8),
            actionPlay);
    // (p|play) (c|config)
    addSyntax(this::playerOnly, (sender, args) -> base.config(sender.asPlayer().getUuid()),
            actionPlay, actionPlayConfig);
    // (p|play) <minesCount>
    addSyntax(this::playerOnly, (sender, args) -> base.play(sender.asPlayer().getUuid(), args.get(actionPlayInt)),
            actionPlay, actionPlayInt);
    // (s|spectate) <target>
    addSyntax(this::playerOnly, this::spectate,
            actionSpec, actionSpecName);
  }

  private void spectate(CommandSender sender, Arguments args) {
    final Player targetP = MinecraftServer.getConnectionManager().getPlayer(args.get(actionSpecName));
    if (targetP != null && targetP.isOnline()) {
      base.spectate(sender.asPlayer().getUuid(), targetP.getUuid());
    } else {
      sender.sendMessage(Opuka.getInstance().getLangManager().get("opuka.command.opuka.error.target_not_found").split("\n"));
    }
  }

  private boolean playerOnly(CommandSender source, String command) {
    return source.isPlayer();
  }

  public void update() {
    actionSpecName = actionSpecName.from(base.getPlayingNames());
  }

  private static class OpukaCommandImpl extends OpukaCommandBase {
    @Override
    public void spectate(UUID performer, UUID target) {
      super.spectate(performer, target);
    }

    @Override
    protected String[] getPlayingNames() {
      final Set<UUID> uuidSet = Opuka.getInstance().methods.getPlayers();
      final Set<String> names = new HashSet<>();
      for (UUID uuid : uuidSet) {
        final Player p = MinecraftServer.getConnectionManager().getPlayer(uuid);
        if (p != null && p.isOnline())
          names.add(p.getUsername());
      }
      return names.toArray(new String[0]);
    }
  }
}
