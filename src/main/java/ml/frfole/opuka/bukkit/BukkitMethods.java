package ml.frfole.opuka.bukkit;

import ml.frfole.opuka.common.GameGridInventory;
import ml.frfole.opuka.common.OpukaMethods;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class BukkitMethods extends OpukaMethods {

  public BukkitMethods(long seed) {
    this.random = new Random(seed);
  }

  @Override
  public GameGridInventory removePlayerGGI(UUID uuid) {
    GameGridInventory ggi = uuid2GGI.remove(uuid);
    if (ggi != null && ggi.isOwner(uuid)) {
      ggi.destroy();
      HandlerList.unregisterAll((Listener) ggi);
    }
    return ggi;
  }
}
