package dev._2lstudios.skywars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.game.arena.GameArena;
import dev._2lstudios.skywars.managers.PlayerManager;

public class PlayerDeathListener implements Listener {
  private final PlayerManager playerManager;
  
  public PlayerDeathListener(PlayerManager playerManager) {
    this.playerManager = playerManager;
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getEntity();
    if (player != null) {
      Player killer = player.getKiller();
      GamePlayer gamePlayer = this.playerManager.getPlayer(player);
      GameArena gameArena = gamePlayer.getArena();
      if (gameArena != null) {
        if (killer != null)
          gameArena.addKill(killer.getName()); 
        gameArena.addSpectator(gamePlayer);
      } 
    } 
  }
}
