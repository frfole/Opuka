package ml.frfole.opuka.common.gamegrid;

import ml.frfole.opuka.common.Opuka;

public class GameGridRS extends GameGrid {
  public GameGridRS(int height, int width) {
    super(height, width);
    this.random = Opuka.getInstance().methods.getRandom();
  }
}
