package ml.frfole.opuka.bukkit;

import ml.frfole.opuka.common.GameGridInventory;
import ml.frfole.opuka.common.OpukaMethods;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class BukkitMethods implements OpukaMethods {
  private final Random random;
  private final OpukaBukkit plugin;
  private final HashMap<UUID, GameGridInventory> player2GGI = new HashMap<>();

  public BukkitMethods(OpukaBukkit plugin, long seed) {
    this.plugin = plugin;
    this.random = new Random(seed);
  }

  @Override
  public Random getRandom() {
    return this.random;
  }

  @Override
  public void setPlayerGGI(UUID uuid, GameGridInventory ggi) {
    removePlayerGGI(uuid);
    player2GGI.put(uuid, ggi);
  }

  @Override
  public GameGridInventory removePlayerGGI(UUID uuid) {
    GameGridInventory ggi = player2GGI.remove(uuid);
    if (ggi != null) {
      if (ggi instanceof Listener)
        HandlerList.unregisterAll((Listener) ggi);
      ggi.close();
    }
    return ggi;
  }

  @Override
  public GameGridInventory getPlayerGGI(UUID uuid) {
    return player2GGI.get(uuid);
  }
}
