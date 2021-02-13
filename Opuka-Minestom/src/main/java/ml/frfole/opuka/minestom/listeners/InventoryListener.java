package ml.frfole.opuka.minestom.listeners;

import ml.frfole.opuka.common.Opuka;
import ml.frfole.opuka.common.gamegrid.GameGrid;
import ml.frfole.opuka.common.inventory.ConfigInventory;
import ml.frfole.opuka.common.inventory.GameGridInventory;
import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.click.ClickType;

import java.util.UUID;

public class InventoryListener {
  public static <E extends Event> void onClick(InventoryPreClickEvent event) {
    final UUID performerId = event.getPlayer().getUuid();
    if (Opuka.getInstance().methods.isSpectator(performerId)) {
      event.setCancelled(true);
      return;
    }
    if (Opuka.getInstance().methods.isInConfig(performerId)) {
      event.setCancelled(true);
      final ConfigInventory ci = Opuka.getInstance().methods.getPlayerCI(performerId);
      ci.rightClick(event.getSlot(), 9, performerId);
      return;
    }
    final GameGridInventory ggi = Opuka.getInstance().methods.getPlayerGGI(performerId);
    if (ggi == null || !event.getInventory().equals(ggi.getInventory())) {
      return;
    }
    event.setCancelled(true);
    final int slot = event.getSlot();
    if (slot < 0 || slot > event.getInventory().getSize() - 1) return;
    if (event.getClickType() == ClickType.LEFT_CLICK) {
      if (ggi.getGameGrid().getState() != GameGrid.State.READY && slot == GameGridInventory.SLOT_RESET_START) {
        ggi.getGameGrid().reset();
      } else {
        ggi.leftClick(slot, 9, performerId);
      }
    } else if (event.getClickType() == ClickType.RIGHT_CLICK) {
      if (ggi.getGameGrid().getState() != GameGrid.State.READY && slot == GameGridInventory.SLOT_RESET_START) {
        ggi.getGameGrid().reset();
      } else {
        ggi.rightClick(slot, 9, performerId);
      }
    }
  }

  public static <E extends Event> void onClose(InventoryCloseEvent e) {
    final UUID performerId = e.getPlayer().getUuid();
    if (Opuka.getInstance().methods.isSpectator(performerId)) {
      Opuka.getInstance().methods.removePlayerGGI(performerId);
    }
    final GameGridInventory ggi = Opuka.getInstance().methods.getPlayerGGI(performerId);
    if (ggi == null || e.getInventory() == null || !e.getInventory().equals(ggi.getInventory())) return;
    Opuka.getInstance().methods.removePlayerGGI(performerId);
  }
}
