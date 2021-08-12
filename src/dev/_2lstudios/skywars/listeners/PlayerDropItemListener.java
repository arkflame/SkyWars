package dev._2lstudios.skywars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;

public class PlayerDropItemListener implements Listener {
  private final GamePlayerManager playerManager;
  
  public PlayerDropItemListener(GamePlayerManager playerManager) {
    this.playerManager = playerManager;
  }
  
  @EventHandler(ignoreCancelled = true)
  public void onPlayerDropItem(PlayerDropItemEvent event) {
    if (event.getItemDrop() != null) {
      GamePlayer gamePlayer = this.playerManager.getPlayer(event.getPlayer());
      if (gamePlayer != null) {
        Arena arena = gamePlayer.getArena();
        if (arena == null || arena.getState() != GameState.PLAYING || gamePlayer.isSpectating())
          event.setCancelled(true); 
      } else {
        event.setCancelled(true);
      } 
    } 
  }
}
