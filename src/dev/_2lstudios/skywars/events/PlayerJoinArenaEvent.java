package dev._2lstudios.skywars.events;

import org.bukkit.event.Cancellable;

import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.game.arena.GameArena;

public class PlayerJoinArenaEvent extends PlayerArenaEvent implements Cancellable {
  private boolean cancelled = false;
  
  public PlayerJoinArenaEvent(GamePlayer gamePlayer, GameArena gameArena) {
    super(gamePlayer, gameArena);
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
