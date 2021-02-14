package ml.frfole.opuka.common.inventory;

import ml.frfole.opuka.common.Opuka;

import java.util.UUID;

public abstract class ConfigInventory extends InventoryBase {
  public static final int SLOT_INFO = 0;
  public static final int SLOT_PLAY = 8;
  public static final int SLOT_MINES_LESS = 11;
  public static final int SLOT_MINES_CURRENT = 13;
  public static final int SLOT_MINES_MORE = 15;

  protected int minesCount = 8;
  protected final String invName;

  protected ConfigInventory(UUID ownerId) {
    super(ownerId);
    this.invName = Opuka.getInstance().getLangManager().get("opuka.inventory.config.name");
  }

  /**
   * Opens new game for performer with selected properties.
   * @param minesCount the amount of mines
   * @param performer  the {@link UUID} of player
   */
  protected void createGame(final int minesCount, final UUID performer) {
    Opuka.getInstance().methods.setPlayerGGI(performer, Opuka.getInstance().methods.createGGI(performer, minesCount));
  }

  @Override
  public void rightClick(final int slot, final int width, final UUID performer) {
    if (slot == SLOT_PLAY) {
      destroy();
      createGame(minesCount, performer);
    }
    if (slot == SLOT_MINES_LESS && minesCount > 1) {
      minesCount--;
    }
    if (slot == SLOT_MINES_CURRENT) {
      minesCount = 8;
    }
    if (slot == SLOT_MINES_MORE && minesCount < 52) {
      minesCount++;
    }
    update();
  }

  @Override
  public void leftClick(int slot, int width, UUID performer) {
    rightClick(slot, width, performer);
  }
}
