package dev._2lstudios.skywars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;

public class PlayerDeathListener implements Listener {
  private final GamePlayerManager playerManager;

  public PlayerDeathListener(GamePlayerManager playerManager) {
    this.playerManager = playerManager;
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getEntity();
    if (player != null) {
      GamePlayer gamePlayer = this.playerManager.getPlayer(player);
      Arena arena = gamePlayer.getArena();

      if (arena != null) {
        Player killer = player.getKiller();

        if (killer != null) {
          arena.getKills().addKill(this.playerManager.getPlayer(killer));
        }

        arena.remove(gamePlayer);
      }
    }
  }
}
