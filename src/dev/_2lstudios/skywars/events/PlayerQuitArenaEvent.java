package dev._2lstudios.skywars.events;

import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.game.arena.GameArena;

public class PlayerQuitArenaEvent extends PlayerArenaEvent {
  public PlayerQuitArenaEvent(GamePlayer gamePlayer, GameArena gameArena) {
    super(gamePlayer, gameArena);
  }
}
