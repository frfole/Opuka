package ml.frfole.opuka.common;

import ml.frfole.opuka.common.inventory.ConfigInventory;
import ml.frfole.opuka.common.inventory.GameGridInventory;

import java.io.File;
import java.util.*;

public class Opuka {
  public final Methods methods;
  private static Opuka instance;
  private final File dataFolder;
  private final LangManager langManager = new LangManager();

  public Opuka(Methods methods, File dataFolder) {
    this.methods = methods;
    this.dataFolder = dataFolder;
    Opuka.instance = this;
  }

  public static Opuka getInstance() {
    return Opuka.instance;
  }

  public File getDataFolder() {
    return dataFolder;
  }

  public LangManager getLangManager() {
    return langManager;
  }

  public abstract static class Methods {
    protected Random random = new Random();
    protected final HashMap<UUID, GameGridInventory> uuid2GGI = new HashMap<>();
    protected final HashMap<UUID, UUID> spec2Owner = new HashMap<>();
    protected final HashMap<UUID, ConfigInventory> uuid2CI = new HashMap<>();

    /**
     * Internal tick.
     */
    public void tick() {
      for (Map.Entry<UUID, GameGridInventory> entry : uuid2GGI.entrySet()) {
        entry.getValue().tick();
      }
      for (Map.Entry<UUID, ConfigInventory> entry : uuid2CI.entrySet()) {
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

    /**
     * Assigns player {@link GameGridInventory}.
     * @param uuid the {@link UUID} of player
     * @param ggi  the {@link GameGridInventory}
     */
    public void setPlayerGGI(UUID uuid, GameGridInventory ggi) {
      removePlayerGGI(uuid);
      removePlayerCI(uuid);
      uuid2GGI.put(uuid, ggi);
    }

    /**
     * Sets player to spectate target.
     * <br/>
     * Works only, if player is not already playing.
     * @param spectator the {@link UUID} of player(spectator)
     * @param owner     the {@link UUID} of target
     */
    public void setPlayerGGI(UUID spectator, UUID owner) {
      removePlayerGGI(spectator);
      removePlayerCI(spectator);
      GameGridInventory ggi = uuid2GGI.get(owner);
      if (ggi != null && !ggi.isOwner(spectator)) {
        spec2Owner.put(spectator, owner);
        ggi.open(spectator);
      }
    }

    /**
     * Assigns player {@link ConfigInventory}.
     * @param target the {@link UUID} of player
     * @param ci     the {@link ConfigInventory}
     */
    public void setPlayerCI(UUID target, ConfigInventory ci) {
      removePlayerGGI(target);
      removePlayerCI(target);
      uuid2CI.put(target, ci);
    }

    /**
     * Removes player from spectating other player or playing.
     * @param uuid the {@link UUID} of player
     */
    public void removePlayerGGI(UUID uuid) {
      GameGridInventory ggi = uuid2GGI.remove(uuid);
      spec2Owner.remove(uuid);
      if (ggi != null && ggi.isOwner(uuid)) {
        ggi.destroy();
      }
    }

    /**
     * Removes player from {@link #uuid2CI}.
     * @param target the {@link UUID} of player
     */
    public void removePlayerCI(UUID target) {
      ConfigInventory ci = uuid2CI.remove(target);
      if (ci != null && ci.isOwner(target)) {
        ci.destroy();
      }
    }

    /**
     * Gets {@link GameGridInventory} of player.
     * @param uuid the {@link UUID} of player
     * @return {@link GameGridInventory} of player, {@code null} if player is not playing
     */
    public GameGridInventory getPlayerGGI(UUID uuid) {
      return uuid2GGI.get(uuid);
    }

    /**
     * Gets {@link ConfigInventory} of player.
     * @param target the {@link UUID} of player
     * @return {@link ConfigInventory} of player, {@code null} if not in {@link #uuid2CI}
     */
    public ConfigInventory getPlayerCI(final UUID target) {
      return uuid2CI.get(target);
    }

    /**
     * Gets {@link Set<UUID>} of currently playing players as {@link UUID}.
     * @return {@link Set} of {@link UUID} of currently playing players.
     */
    public Set<UUID> getPlayers() {
      return uuid2GGI.keySet();
    }

    /**
     * Checks if player is spectating.
     * @param uuid the {@link UUID} of player
     * @return {@code true} if player is spectating, {@code false} otherwise
     */
    public boolean isSpectator(UUID uuid) {
      return spec2Owner.containsKey(uuid);
    }

    /**
     * Checks if player is in config.
     * @param uuid the {@link UUID} of player
     * @return {@code true} if in, {@code false} otherwise.
     */
    public boolean isInConfig(UUID uuid) {
      return uuid2CI.containsKey(uuid);
    }
  }
}
