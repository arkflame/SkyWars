package dev._2lstudios.skywars.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev._2lstudios.skywars.events.SpectatorQuitArenaEvent;
import dev._2lstudios.skywars.game.GamePlayer;

public class SpectatorQuitArenaListener implements Listener {
  @EventHandler
  public void onSpectatorQuitArena(SpectatorQuitArenaEvent event) {
    GamePlayer gamePlayer = event.getGamePlayer();

    if (gamePlayer != null) {
      gamePlayer.setArena(null);
      gamePlayer.clear(GameMode.ADVENTURE, true, false, true);
      gamePlayer.giveItems(0);
      gamePlayer.sendMessage(ChatColor.GREEN + "Saliste del modo espectador!");
    }
  }
}
