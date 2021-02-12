package ml.frfole.opuka.common.inventory;

import java.util.UUID;

public abstract class ConfigInventory extends InventoryBase {
  public static final int SLOT_INFO = 0;
  public static final int SLOT_PLAY = 8;
  public static final int SLOT_MINES_LESS = 11;
  public static final int SLOT_MINES_CURRENT = 13;
  public static final int SLOT_MINES_MORE = 15;

  protected int minesCount = 8;

  static {
    invName = "Opuka - Minesweeper config";
  }

  protected ConfigInventory(UUID ownerId) {
    super(ownerId);
  }

  protected abstract void createGame(final int minesCount, final UUID performer);

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
