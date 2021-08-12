package dev._2lstudios.skywars.events;

import org.bukkit.event.Cancellable;

import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;

public class PlayerJoinArenaEvent extends PlayerArenaEvent implements Cancellable {
  private boolean cancelled = false;
  
  public PlayerJoinArenaEvent(GamePlayer gamePlayer, Arena arena) {
    super(gamePlayer, arena);
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean arg0) {
    cancelled = arg0;
  }
}
