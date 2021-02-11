package ml.frfole.opuka.common;

import ml.frfole.opuka.common.gamegrids.GameGrid;

import java.util.UUID;

public abstract class GameGridInventory {
  protected GameGrid gameGrid;

  private boolean didEnded = false;

  public abstract void update();
  public abstract void destroy();
  public abstract void open(UUID uuid);

  protected void tick() {
    update();
  }

  public void rightClick(int slot, int width) {
    if (gameGrid.getState() == GameGrid.State.READY) {
      gameGrid.timeStart();
      didEnded = false;
    }
    gameGrid.flag(slot%width, slot/width);
    update();
  }

  public void leftClick(int slot, int width) {
    if (gameGrid.getState() == GameGrid.State.READY) {
      gameGrid.timeStart();
      didEnded = false;
    }
    gameGrid.dig(slot%width, slot/width);
    if (gameGrid.getState().isFinished() && !didEnded) {
      gameGrid.timeEnd();
      didEnded = true;
    }
    update();
  }
}
