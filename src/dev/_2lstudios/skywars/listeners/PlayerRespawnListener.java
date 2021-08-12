package dev._2lstudios.skywars.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;

public class PlayerRespawnListener implements Listener {
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    Player player = event.getPlayer();
    final GamePlayer gamePlayer = SkyWars.getSkyWarsManager().getPlayerManager().getPlayer(player);
    final Arena arena = gamePlayer.getLastArena();

    event.setRespawnLocation(player.getLocation());

    if (arena != null) {
      arena.addSpectator(gamePlayer);
      player.sendMessage(ChatColor.RED + "Puedes salir del modo espectador con /salir!");
    }
  }
}
