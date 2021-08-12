package dev._2lstudios.skywars.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.managers.PlayerManager;

public class EntityDamageByEntityListener implements Listener {
  private final PlayerManager playerManager;
  
  public EntityDamageByEntityListener(PlayerManager playerManager) {
    this.playerManager = playerManager;
  }
  
  @EventHandler(ignoreCancelled = true)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    Entity damager = event.getDamager();
    if (damager instanceof Player) {
      Player damagerPlayer = (Player)damager;
      GamePlayer gamePlayer = this.playerManager.getPlayer(damagerPlayer);
      if (gamePlayer != null && gamePlayer.isSpectating())
        event.setCancelled(true); 
    } 
  }
}
