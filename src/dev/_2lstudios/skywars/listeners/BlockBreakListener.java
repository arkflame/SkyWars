package dev._2lstudios.skywars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.GameArena;
import dev._2lstudios.skywars.managers.PlayerManager;

public class BlockBreakListener implements Listener {
  private final PlayerManager playerManager;
  
  public BlockBreakListener(PlayerManager playerManager) {
    this.playerManager = playerManager;
  }
  
  @EventHandler(ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent event) {
    Player player = event.getPlayer();
    if (!player.hasPermission("skywars.admin")) {
      GamePlayer gamePlayer = this.playerManager.getPlayer(player);
      if (gamePlayer != null) {
        GameArena gameArena = gamePlayer.getArena();
        if (gamePlayer.isSpectating() || gameArena == null || gameArena.getState() != GameState.PLAYING)
          event.setCancelled(true); 
      } 
    } 
  }
}
