package dev._2lstudios.skywars.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerRespawnEvent;

import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;

public class PlayerRespawnListener implements Listener {
  @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    final Player player = event.getPlayer();
    final GamePlayer gamePlayer = SkyWars.getSkyWarsManager().getPlayerManager().getPlayer(player);

    if (gamePlayer != null) {
      final Arena arena = gamePlayer.getArena();

      if (arena != null) {
        if (player.getWorld() != arena.getArenaWorld() || player.getLastDamageCause() == null
            || player.getLastDamageCause().getCause() == DamageCause.VOID) {
          event.setRespawnLocation(arena.getArenaWorld().getSpectatorSpawn(gamePlayer));
        } else {
          event.setRespawnLocation(event.getPlayer().getLocation());
        }
      }

      gamePlayer.clear(GameMode.ADVENTURE);
      gamePlayer.giveItems(0);
    } else {
      event.setRespawnLocation(event.getPlayer().getLocation());
    }
  }
}
