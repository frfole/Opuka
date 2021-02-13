package ml.frfole.opuka.common.gamegrid;

import java.util.Random;

public class GameGridSS extends GameGrid {
  public GameGridSS(int height, int width, long seed) {
    super(height, width);
    this.random = new Random(seed);
  }
}
