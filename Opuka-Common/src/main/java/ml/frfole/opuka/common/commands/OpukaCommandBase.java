package ml.frfole.opuka.common.commands;

import ml.frfole.opuka.common.Opuka;
import java.util.UUID;

public abstract class OpukaCommandBase {

  /**
   * Opens {@link ml.frfole.opuka.common.inventory.ConfigInventory} for performer.
   * @param performer the {@link UUID} of performer
   */
  public void config(final UUID performer) {
    Opuka.getInstance().methods.setPlayerCI(performer, Opuka.getInstance().methods.createCI(performer));
  }

  /**
   * Opens {@link ml.frfole.opuka.common.inventory.GameGridInventory} for performer.
   * @param performer  the {@link UUID} of performer
   * @param minesCount the amount of mines
   */
  public void play(final UUID performer, final int minesCount) {
    Opuka.getInstance().methods.setPlayerGGI(performer, Opuka.getInstance().methods.createGGI(performer, minesCount));
  }

  /**
   * Opens {@link ml.frfole.opuka.common.inventory.GameGridInventory} of target for performer.
   * @param performer the {@link UUID} of performer
   * @param target    the {@link UUID} of target
   */
  public void spectate(final UUID performer, final UUID target) {
    Opuka.getInstance().methods.setPlayerGGI(performer, target);
  }

  /**
   * Gets array of player names that are currently playing.
   * @return array of player names that are currently playing
   */
  protected abstract String[] getPlayingNames();
}
