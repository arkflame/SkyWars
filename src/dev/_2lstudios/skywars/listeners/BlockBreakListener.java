package dev._2lstudios.skywars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;

public class BlockBreakListener implements Listener {
  private final GamePlayerManager playerManager;
  
  public BlockBreakListener(GamePlayerManager playerManager) {
    this.playerManager = playerManager;
  }
  
  @EventHandler(ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent event) {
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
