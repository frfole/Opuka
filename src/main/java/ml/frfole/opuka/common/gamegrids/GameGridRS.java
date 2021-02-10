package ml.frfole.opuka.common.gamegrids;

import ml.frfole.opuka.common.GameGrid;
import ml.frfole.opuka.common.Opuka;

public class GameGridRS extends GameGrid {
  public GameGridRS(int height, int width) {
    super(height, width);
    this.random = Opuka.getInstance().methods.getRandom();
  }
}
