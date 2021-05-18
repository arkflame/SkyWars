package dev._2lstudios.skywars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.GameArena;
import dev._2lstudios.skywars.managers.PlayerManager;

public class PlayerDropItemListener implements Listener {
  private final PlayerManager playerManager;
  
  public PlayerDropItemListener(PlayerManager playerManager) {
    this.playerManager = playerManager;
  }
  
  @EventHandler(ignoreCancelled = true)
  public void onPlayerDropItem(PlayerDropItemEvent event) {
    if (event.getItemDrop() != null) {
      GamePlayer gamePlayer = this.playerManager.getPlayer(event.getPlayer());
      if (gamePlayer != null) {
        GameArena gameArena = gamePlayer.getArena();
        if (gameArena == null || gameArena.getState() != GameState.PLAYING || gamePlayer.isSpectating())
          event.setCancelled(true); 
      } else {
        event.setCancelled(true);
      } 
    } 
  }
}
