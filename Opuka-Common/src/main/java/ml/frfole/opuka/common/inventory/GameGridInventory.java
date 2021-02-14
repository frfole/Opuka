package ml.frfole.opuka.common.inventory;

import ml.frfole.opuka.common.Opuka;
import ml.frfole.opuka.common.gamegrid.GameGrid;
import ml.frfole.opuka.common.gamegrid.GameGridRS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

public abstract class GameGridInventory extends InventoryBase {
  public static final int SLOT_INFO = 4;
  public static final int SLOT_RESET_START = 0;
  public static final DateFormat dateFormatter = new SimpleDateFormat("mm:ss.SSS");

  protected final String invName;
  protected GameGrid gameGrid;
  private boolean didEnded = false;

  protected GameGridInventory(UUID ownerId, final int height, final int width, final int minesCount) {
    super(ownerId);
    this.invName = Opuka.getInstance().getLangManager().get("opuka.inventory.gamegrid.name");
    this.gameGrid = new GameGridRS(height, width);
    this.gameGrid.setInvalid(0, 0); // start / reset
    this.gameGrid.setInvalid(4, 0); // time
    this.gameGrid.populateWithMines(minesCount);
  }

  public GameGrid getGameGrid() {
    return gameGrid;
  }

  /**
   * Perform left click on slot.
   * <br/>
   * Perform {@link GameGrid#dig(int, int)} and optionally {@link GameGrid#timeStart()} or {@link GameGrid#timeEnd()}.
   * @param slot        the slot (left to right, up to down)
   * @param width       the width (count of horizontal slots)
   * @param performerId the {@link UUID} of performer, who clicked
   */
  public void leftClick(int slot, int width, UUID performerId) {
    if (!isOwner(performerId)) return;
    if (gameGrid.getState() == GameGrid.State.READY) {
      gameGrid.timeStart();
      didEnded = false;
    }
    gameGrid.dig(slot%width, slot/width);
    if (gameGrid.getState().isFinished() && !didEnded) {
      gameGrid.timeEnd();
      Opuka.getInstance().methods.callEventGameFinish(gameGrid, ownerId);
      didEnded = true;
    }
    update();
  }

  /**
   * Perform right click on slot.
   * <br/>
   * Perform {@link GameGrid#flag} and optionally {@link GameGrid#timeStart()}.
   * @param slot        the slot (left to right, up to down)
   * @param width       the width (count of horizontal slots)
   * @param performerId the {@link UUID} of performer, who clicked
   */
  public void rightClick(int slot, int width, UUID performerId) {
    if (!isOwner(performerId)) return;
    if (gameGrid.getState() == GameGrid.State.READY) {
      gameGrid.timeStart();
      didEnded = false;
    }
    gameGrid.flag(slot%width, slot/width);
    update();
  }
}
