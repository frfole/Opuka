import junit.framework.Assert;
import ml.frfole.opuka.common.gamegrids.GameGrid;
import ml.frfole.opuka.common.GameGridInventory;
import ml.frfole.opuka.common.Opuka;
import ml.frfole.opuka.common.OpukaMethods;
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
    GameGrid grid = new GameGridSS(7, 65, 342224193951367169L);
    grid.populateWithMines(20);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_CLEAR, grid.getGrid()[0][4]);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_NEAR_1, grid.getGrid()[0][0]);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_MINE, grid.getGrid()[1][1]);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_MINE, grid.getGrid()[2][8]);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_MINE, grid.getGrid()[3][8]);
  }

  @Test
  public void digPopulateTest() {
    GameGrid grid = new GameGridSS(7, 65, 342224193951367169L);
    grid.populateWithMines(20);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_CLEAR, grid.getGrid()[0][4]);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_NEAR_1, grid.getGrid()[0][0]);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_MINE, grid.getGrid()[1][1]);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_MINE, grid.getGrid()[2][8]);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_MINE, grid.getGrid()[3][8]);
    grid.dig(13, 5);
    Assert.assertEquals(GameGrid.FieldType.NEAR_1, grid.getGrid()[2][0]);
  }

  @Test
  public void flagDigPopulateTest() {
    GameGrid grid = new GameGridSS(7, 65, 342224193951367169L);
    grid.populateWithMines(20);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_CLEAR, grid.getGrid()[0][4]);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_NEAR_1, grid.getGrid()[0][0]);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_MINE, grid.getGrid()[1][1]);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_MINE, grid.getGrid()[2][8]);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_MINE, grid.getGrid()[3][8]);
    grid.dig(13, 5);
    Assert.assertEquals(GameGrid.FieldType.NEAR_1, grid.getGrid()[2][0]);
    grid.flag(38, 0);
    Assert.assertEquals(GameGrid.FieldType.UNKNOWN_FLAG_CLEAR, grid.getGrid()[0][38]);
  }

  private static void prettyPrint(GameGrid gameGrid) {
    final GameGrid.FieldType[][] grid = gameGrid.getGrid();

    for (int i = 0; i < gameGrid.getWidth() * 3 + 3; i++)
      System.out.print("-");
    System.out.println();

    String a;
    for (GameGrid.FieldType[] line : grid) {
      System.out.print("| ");
      for (GameGrid.FieldType i : line) {
        System.out.print(i + " ");
      }
      System.out.println("|");
    }

    for (int i = 0; i < gameGrid.getWidth() * 3 + 3; i++)
      System.out.print("-");
    System.out.println();
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

}
