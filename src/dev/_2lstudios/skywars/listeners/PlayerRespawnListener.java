package dev._2lstudios.skywars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    Player player = event.getPlayer();

    event.setRespawnLocation(player.getLocation());
  }
}
