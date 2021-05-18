package dev._2lstudios.skywars.game.arena;

public class PlayerKills {
  private final String name;
  private int amount;
  
  PlayerKills(final String name, final int kills) {
    this.name = name;
    this.amount = kills;
  }
  
  String getName() {
    return name;
  }
  
  void add() {
    this.amount++;
  }
  
  public int amount() {
    return this.amount;
  }
}
