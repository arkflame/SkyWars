package dev._2lstudios.skywars.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;

public class EntityDamageListener implements Listener {
  private final GamePlayerManager playerManager;
  
  public EntityDamageListener(GamePlayerManager playerManager) {
    this.playerManager = playerManager;
  }
  
  @EventHandler(ignoreCancelled = true)
  public void onEntityDamage(EntityDamageEvent event) {
    Entity entity = event.getEntity();
    if (entity instanceof Player) {
      Player player = (Player)entity;
      GamePlayer gamePlayer = this.playerManager.getPlayer(player);
      if (gamePlayer != null) {
        Arena arena = gamePlayer.getArena();
        if (gamePlayer.isSpectating() || arena == null || arena.getState() != GameState.PLAYING)
          event.setCancelled(true); 
      } 
    } 
  }
}
