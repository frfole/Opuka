package ml.frfole.opuka.common.events;

import ml.frfole.opuka.common.gamegrid.GameGrid;

import java.util.UUID;

public interface GameFinishEvent {

  UUID getOwner();
  GameGrid getGameGrid();
}
