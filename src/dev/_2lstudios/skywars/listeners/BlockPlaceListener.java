package dev._2lstudios.skywars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;

public class BlockPlaceListener implements Listener {
  private final GamePlayerManager playerManager;
  
  public BlockPlaceListener(GamePlayerManager playerManager) {
    this.playerManager = playerManager;
  }
  
  @EventHandler(ignoreCancelled = true)
  public void onBlockPlace(BlockPlaceEvent event) {
    Player player = event.getPlayer();
    if (!player.hasPermission("skywars.admin")) {
      GamePlayer gamePlayer = this.playerManager.getPlayer(player);
      if (gamePlayer != null) {
        Arena arena = gamePlayer.getArena();
        if (gamePlayer.isSpectating() || arena == null || arena.getState() != GameState.PLAYING)
          event.setCancelled(true); 
      } 
    } 
  }
}
