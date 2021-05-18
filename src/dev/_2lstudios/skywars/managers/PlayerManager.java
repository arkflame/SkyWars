package dev._2lstudios.skywars.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import dev._2lstudios.skywars.game.GamePlayer;

public class PlayerManager {
  private final Map<UUID, GamePlayer> gamePlayers = new HashMap<>();
  
  public GamePlayer getPlayer(UUID uuid) {
    if (uuid == null)
      return null; 
    return this.gamePlayers.getOrDefault(uuid, null);
  }
  
  public GamePlayer getPlayer(Player player) {
    return (player == null) ? null : getPlayer(player.getUniqueId());
  }
  
  public GamePlayer addGamePlayer(Player player) {
    UUID uuid = player.getUniqueId();
    GamePlayer gamePlayer = new GamePlayer(player);
    
    this.gamePlayers.put(uuid, gamePlayer);

    return gamePlayer;
  }
  
  public void removeGamePlayer(UUID uuid) {
    this.gamePlayers.remove(uuid);
  }
  
  public void removeGamePlayer(Player player) {
    removeGamePlayer(player.getUniqueId());
  }
}
