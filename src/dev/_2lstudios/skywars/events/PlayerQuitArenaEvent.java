package dev._2lstudios.skywars.events;

import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;

public class PlayerQuitArenaEvent extends PlayerArenaEvent {
  public PlayerQuitArenaEvent(GamePlayer gamePlayer, Arena arena) {
    super(gamePlayer, arena);
  }
}
