package ml.frfole.opuka.bukkit;

import ml.frfole.opuka.common.GameGridInventory;
import ml.frfole.opuka.common.OpukaMethods;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class BukkitMethods extends OpukaMethods {
  private final Random random;
  private final HashMap<UUID, GameGridInventory> player2GGI = new HashMap<>();

  public BukkitMethods(long seed) {
    this.random = new Random(seed);
  }

  @Override
  public GameGridInventory removePlayerGGI(UUID uuid) {
    GameGridInventory ggi = player2GGI.remove(uuid);
    if (ggi != null && ggi.isOwner(uuid)) {
      HandlerList.unregisterAll((Listener) ggi);
      ggi.destroy();
    }
    return ggi;
  }
}
