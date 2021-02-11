package ml.frfole.opuka.common;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public abstract class OpukaMethods {
  protected Random random;
  protected final HashMap<UUID, GameGridInventory> uuid2GGI = new HashMap<>();

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
    if (ggi != null && !ggi.isOwner(spectator))
      ggi.open(spectator);
  }

  public GameGridInventory removePlayerGGI(UUID uuid) {
    GameGridInventory ggi = uuid2GGI.get(uuid);
    if (ggi != null && ggi.isOwner(uuid)) {
      ggi.destroy();
    }
    return ggi;
  }

  public GameGridInventory getPlayerGGI(UUID uuid) {
    return uuid2GGI.get(uuid);
  }
}
