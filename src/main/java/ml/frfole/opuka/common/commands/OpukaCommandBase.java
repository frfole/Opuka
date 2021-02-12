package ml.frfole.opuka.common.commands;

import ml.frfole.opuka.bukkit.inventory.GGInvBukkit;
import ml.frfole.opuka.common.Opuka;
import ml.frfole.opuka.common.inventory.GameGridInventory;
import java.util.UUID;

public abstract class OpukaCommandBase {

  protected void play(final UUID id, final int size, final int minesCount) {
    final GameGridInventory ggi = new GGInvBukkit(size, id, minesCount);
    Opuka.getInstance().methods.setPlayerGGI(id, ggi);
  }

  protected void spectate(final UUID id, final UUID targetId) {
    Opuka.getInstance().methods.setPlayerGGI(id, targetId);
  }

  protected abstract String[] getPlayingNames();
}
