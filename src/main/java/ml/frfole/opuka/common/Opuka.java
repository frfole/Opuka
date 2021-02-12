package ml.frfole.opuka.common;

import ml.frfole.opuka.common.inventory.GameGridInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Opuka {
  public final Methods methods;
  private static Opuka instance;

  public Opuka(Methods methods) {
    this.methods = methods;
    Opuka.instance = this;
  }

  public static Opuka getInstance() {
    return Opuka.instance;
  }

  public abstract static class Methods {
    protected Random random = new Random();
    protected final HashMap<UUID, GameGridInventory> uuid2GGI = new HashMap<>();
    protected final HashMap<UUID, UUID> spec2Owner = new HashMap<>();

    public void tick() {
      for (Map.Entry<UUID, GameGridInventory> entry : uuid2GGI.entrySet()) {
        entry.getValue().tick();
      }
    }

    /**
     * Gets {@link #random}.
     * @return {@link #random}
     */
    public Random getRandom() {
      return this.random;
    }

    public void setPlayerGGI(UUID uuid, GameGridInventory ggi) {
      this.removePlayerGGI(uuid);
      this.uuid2GGI.put(uuid, ggi);
    }

    public void setPlayerGGI(UUID spectator, UUID owner) {
      GameGridInventory ggi = uuid2GGI.get(owner);
      if (ggi != null && !ggi.isOwner(spectator)) {
        spec2Owner.put(spectator, owner);
        ggi.open(spectator);
      }
    }

    public void removePlayerGGI(UUID uuid) {
      GameGridInventory ggi = uuid2GGI.remove(uuid);
      spec2Owner.remove(uuid);
      if (ggi != null && ggi.isOwner(uuid)) {
        ggi.destroy();
      }
    }

    public GameGridInventory getPlayerGGI(UUID uuid) {
      return uuid2GGI.get(uuid);
    }

    public boolean isSpectator(UUID uuid) {
      return spec2Owner.containsKey(uuid);
    }
  }
}
