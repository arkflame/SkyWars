package dev._2lstudios.skywars.game.arena;

import dev._2lstudios.skywars.game.player.GamePlayer;

public class PlayerKills {
  private final GamePlayer gamePlayer;
  private int amount;

  PlayerKills(final GamePlayer gamePlayer, final int kills) {
    this.gamePlayer = gamePlayer;
    this.amount = kills;
  }

  String getName() {
    return gamePlayer == null ? "N/A" : gamePlayer.getName();
  }

  int add() {
    return ++this.amount;
  }

  public int amount() {
    return this.amount;
  }
}
