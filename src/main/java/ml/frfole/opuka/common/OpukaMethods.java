package ml.frfole.opuka.common;

import java.util.Random;
import java.util.UUID;

public interface OpukaMethods {
  Random getRandom();
  void setPlayerGGI(UUID uuid, GameGridInventory ggi);
  GameGridInventory removePlayerGGI(UUID uuid);
  GameGridInventory getPlayerGGI(UUID uuid);
}
