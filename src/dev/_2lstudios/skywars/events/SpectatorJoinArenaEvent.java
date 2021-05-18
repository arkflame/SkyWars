package dev._2lstudios.skywars.events;

import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.game.arena.GameArena;

public class SpectatorJoinArenaEvent extends PlayerArenaEvent {
  public SpectatorJoinArenaEvent(GamePlayer gamePlayer, GameArena gameArena) {
    super(gamePlayer, gameArena);
  }
}
