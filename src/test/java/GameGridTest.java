import junit.framework.Assert;
import ml.frfole.opuka.common.gamegrid.GameGrid;
import ml.frfole.opuka.common.Opuka;
import ml.frfole.opuka.common.gamegrid.GameGridSS;
import org.junit.Before;
import org.junit.Test;

public class GameGridTest {

  @Before
  public void before() {
    new Opuka(
            new Opuka.Methods() {

            }
    );
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
}
