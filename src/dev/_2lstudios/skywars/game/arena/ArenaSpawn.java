package dev._2lstudios.skywars.game.arena;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.GameCage;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.tasks.CreateCageTask;

public class ArenaSpawn {
  private final Vector vector;
  
  private final String arenaName;
  
  private UUID playerUUID;
  
  ArenaSpawn(Arena arena, Vector vector) {
    this.arenaName = arena.getName();
    this.vector = vector.add(new Vector(0.5D, 0.5D, 0.5D));
  }
  
  public UUID getPlayerUUID() {
    return this.playerUUID;
  }
  
  public void setPlayer(GamePlayer gamePlayer) {
    this.playerUUID = (gamePlayer == null) ? null : gamePlayer.getUUID();
  }
  
  public Location getLocation() {
    return this.vector.toLocation(SkyWars.getSkyWarsManager().getArenaManager().getArena(this.arenaName).getWorld());
  }
  
  public void createCage(GameCage gameCage) {
    new CreateCageTask(gameCage, getLocation()).run();
  }
  
  public Vector getVector() {
    return this.vector;
  }
}
