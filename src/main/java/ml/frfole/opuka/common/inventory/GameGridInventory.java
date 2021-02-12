package ml.frfole.opuka.common.inventory;

import ml.frfole.opuka.common.gamegrid.GameGrid;

import java.util.UUID;

public abstract class GameGridInventory {
  protected GameGrid gameGrid;
  protected UUID ownerId;

  private boolean didEnded = false;

  public GameGrid getGameGrid() {
    return gameGrid;
  }

  public UUID getOwnerId() {
    return ownerId;
  }

  /**
   * Called when need to update inventory.
   */
  public abstract void update();

  /**
   * Called when need to destroy {@link GameGridInventory}.
   */
  public abstract void destroy();

  /**
   * Opens inventory to user with selected {@link UUID}.
   * @param uuid the {@link UUID}
   */
  public abstract void open(UUID uuid);

  public abstract Object getInventory();

  /**
   * Should be called every tick.
   */
  public void tick() {
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
      didEnded = true;
    }
    update();
  }

  /**
   * Checks if {@link UUID} is same as {@link #ownerId}.
   * @param id the {@link UUID}
   * @return {@code true} if same, {@code false} otherwise
   */
  public boolean isOwner(UUID id) {
    return id.equals(ownerId);
  }
}
