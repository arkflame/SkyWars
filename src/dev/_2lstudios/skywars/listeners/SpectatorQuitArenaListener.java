package dev._2lstudios.skywars.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev._2lstudios.skywars.events.SpectatorQuitArenaEvent;
import dev._2lstudios.skywars.game.player.GamePlayer;

public class SpectatorQuitArenaListener implements Listener {
  @EventHandler
  public void onSpectatorQuitArena(SpectatorQuitArenaEvent event) {
    GamePlayer gamePlayer = event.getGamePlayer();

    if (gamePlayer != null) {
      Server server = Bukkit.getServer();

      gamePlayer.setPlayerMode(null);
      gamePlayer.getPlayer().teleport(server.getWorlds().get(0).getSpawnLocation());
      gamePlayer.setArena(null);
      gamePlayer.clear(GameMode.ADVENTURE);
      gamePlayer.update();
      gamePlayer.giveItems(0);
      gamePlayer.sendMessage(ChatColor.GREEN + "Saliste del modo espectador!");
    }
  }
}
