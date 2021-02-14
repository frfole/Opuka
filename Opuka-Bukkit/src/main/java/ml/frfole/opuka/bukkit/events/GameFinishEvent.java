package ml.frfole.opuka.bukkit.events;

import ml.frfole.opuka.common.gamegrid.GameGrid;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class GameFinishEvent extends Event implements ml.frfole.opuka.common.events.GameFinishEvent {
  private static final HandlerList handlers = new HandlerList();

  private final GameGrid gameGrid;
  private final UUID owner;

  public GameFinishEvent(GameGrid gameGrid, UUID owner) {
    super(false);
    this.gameGrid = gameGrid;
    this.owner = owner;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }


  @Override
  public UUID getOwner() {
    return owner;
  }

  @Override
  public GameGrid getGameGrid() {
    return gameGrid;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }
}
