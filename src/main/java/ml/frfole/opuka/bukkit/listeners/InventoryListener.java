package ml.frfole.opuka.bukkit.listeners;

import ml.frfole.opuka.bukkit.inventory.GGInvBukkit;
import ml.frfole.opuka.common.inventory.ConfigInventory;
import ml.frfole.opuka.common.inventory.GameGridInventory;
import ml.frfole.opuka.common.Opuka;
import ml.frfole.opuka.common.gamegrid.GameGrid;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;

public class InventoryListener implements Listener {

  @EventHandler
  public void onClick(final InventoryClickEvent event) {
    final UUID performerId = event.getWhoClicked().getUniqueId();
    if (Opuka.getInstance().methods.isSpectator(performerId)) {
      event.setCancelled(true);
      return;
    }
    if (Opuka.getInstance().methods.isInConfig(performerId)) {
      event.setCancelled(true);
      final ConfigInventory ci = Opuka.getInstance().methods.getPlayerCI(performerId);
      ci.rightClick(event.getRawSlot(), 9, performerId);
      return;
    }
    final GameGridInventory ggi = Opuka.getInstance().methods.getPlayerGGI(performerId);
    if (ggi == null || !event.getInventory().equals(ggi.getInventory())) {
      return;
    }
    event.setCancelled(true);
    final int slot = event.getRawSlot();
    if (slot < 0 || slot > event.getInventory().getSize() - 1) return;
    if (event.isLeftClick()) {
      if (ggi.getGameGrid().getState() != GameGrid.State.READY && slot == GGInvBukkit.SLOT_RESET_START) {
        ggi.getGameGrid().reset();
      } else {
        ggi.leftClick(slot, 9, performerId);
      }
    } else if (event.isRightClick()) {
      if (ggi.getGameGrid().getState() != GameGrid.State.READY && slot == GGInvBukkit.SLOT_RESET_START) {
        ggi.getGameGrid().reset();
      } else {
        ggi.rightClick(slot, 9, performerId);
      }
    }
  }

  @EventHandler
  public void onClose(final InventoryCloseEvent event) {
    final UUID performerId = event.getPlayer().getUniqueId();
    if (Opuka.getInstance().methods.isSpectator(performerId)) {
      Opuka.getInstance().methods.removePlayerGGI(performerId);
      return;
    }
    final GameGridInventory ggi = Opuka.getInstance().methods.getPlayerGGI(performerId);
    if (ggi == null || !event.getInventory().equals(ggi.getInventory())) return;
    Opuka.getInstance().methods.removePlayerGGI(performerId);
  }
}
