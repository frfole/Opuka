package ml.frfole.opuka.common.inventory;

import java.util.UUID;

public abstract class InventoryBase {
  protected static String invName;

  protected final UUID ownerId;

  protected InventoryBase(final UUID ownerId) {
    this.ownerId = ownerId;
  }

  /**
   * Called every right click.
   * @param slot      the slot
   * @param width     the width of inventory
   * @param performer the {@link UUID} of performer
   */
  protected abstract void rightClick(final int slot, final int width, final UUID performer);

  /**
   * Called every left click
   * @param slot      the slot
   * @param width     the width of inventory
   * @param performer the {@link UUID} of performer
   */
  protected abstract void leftClick(final int slot, final int width, final UUID performer);

  /**
   * Called when need to destroy {@link GameGridInventory}.
   */
  public abstract void destroy();

  /**
   * Opens inventory to target with selected {@link UUID}.
   * @param target the {@link UUID} of target
   */
  public abstract void open(final UUID target);

  /**
   * Called when need to update inventory.
   */
  public abstract void update();

  /**
   * Gets inventory.
   * @return the inventory
   */
  public abstract Object getInventory();

  /**
   * Should be called every tick.
   */
  public void tick() {
    update();
  }

  /**
   * Gets {@link UUID} of owner.
   * @return {@link UUID} of owner
   */
  public UUID getOwnerId() {
    return ownerId;
  }

  /**
   * Checks if {@link UUID} is same as {@link #ownerId}.
   * @param id the {@link UUID}
   * @return {@code true} if same, {@code false} otherwise
   */
  public boolean isOwner(final UUID id) {
    return id.equals(ownerId);
  }
}
