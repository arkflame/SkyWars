package dev._2lstudios.skywars.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;

public class PlayerJoinListener implements Listener {
  private final GamePlayerManager playerManager;

  public PlayerJoinListener(GamePlayerManager playerManager) {
    this.playerManager = playerManager;
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    GamePlayer gamePlayer = this.playerManager.addGamePlayer(player);

    player.teleport(SkyWars.getSpawn());
    gamePlayer.clear(GameMode.ADVENTURE);
    gamePlayer.giveItems(0);
    SkyWars.getSidebar().update(player);
  }
}
