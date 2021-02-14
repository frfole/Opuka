package ml.frfole.opuka.minestom.events;

import ml.frfole.opuka.common.gamegrid.GameGrid;
import net.minestom.server.event.Event;

import java.util.UUID;

public class GameFinishEvent extends Event implements ml.frfole.opuka.common.events.GameFinishEvent {
  private final GameGrid gameGrid;
  private final UUID owner;

  public GameFinishEvent(GameGrid gameGrid, UUID owner) {
    this.gameGrid = gameGrid;
    this.owner = owner;
  }

  @Override
  public UUID getOwner() {
    return owner;
  }

  @Override
  public GameGrid getGameGrid() {
    return gameGrid;
  }
}
