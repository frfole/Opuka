package ml.frfole.opuka.common.commands;

import ml.frfole.opuka.common.Opuka;
import java.util.UUID;

public abstract class OpukaCommandBase {

  protected abstract void config(final UUID performer);
  protected abstract void play(final UUID performer, final int size, final int minesCount);

  protected void spectate(final UUID id, final UUID targetId) {
    Opuka.getInstance().methods.setPlayerGGI(id, targetId);
  }

  protected abstract String[] getPlayingNames();
}
