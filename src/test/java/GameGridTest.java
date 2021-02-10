import ml.frfole.opuka.common.GameGrid;
import ml.frfole.opuka.common.GameGridInventory;
import ml.frfole.opuka.common.Opuka;
import ml.frfole.opuka.common.OpukaMethods;
import ml.frfole.opuka.common.gamegrids.GameGridRS;
import ml.frfole.opuka.common.gamegrids.GameGridSS;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.UUID;

public class GameGridTest {

  @Before
  public void before() {
    new Opuka(new TestOpuka());
  }

  @Test
  public void populateTest() {
    GameGrid grid = new GameGridRS(7, 65);
    grid.populateWithMines(20);
    System.out.println(grid.dig(3, 4));
    prettyPrint(grid);
  }

  class TestOpuka implements OpukaMethods {
    private final Random random = new Random();
    @Override
    public Random getRandom() {
      return random;
    }

    @Override
    public void setPlayerGGI(UUID uuid, GameGridInventory ggi) {

    }

    @Override
    public GameGridInventory removePlayerGGI(UUID uuid) {
      return null;
    }

    @Override
    public GameGridInventory getPlayerGGI(UUID uuid) {
      return null;
    }
  }

  private static void prettyPrint(GameGrid gameGrid) {
    final int[][] grid = gameGrid.getGrid();

    for (int i = 0; i < gameGrid.getWidth() * 3 + 3; i++)
      System.out.print("-");
    System.out.println();

    String a;
    for (int[] line : grid) {
      System.out.print("| ");
      for (int i : line) {
        if (i == -1) { a = "@@";}
        else if (i == 0) { a = "++";}
        else if (1 <= i && i <= 8) { a = "+" + i;}
        else if (i == 9) { a = "##";}
        else if (i == 10) { a = "  ";}
        else if (11 <= i && i <= 18) { a = " " + (i - 10);}
        else if (i == 20) { a = "^ ";}
        else if (21 <= i && i <= 28) { a = "^" + (i - 20);}
        else if (i == 29) { a = "^#";}
        else {a = String.valueOf(i);}
        System.out.print(a + " ");
      }
      System.out.println("|");
    }

    for (int i = 0; i < gameGrid.getWidth() * 3 + 3; i++)
      System.out.print("-");
    System.out.println();
  }
}
