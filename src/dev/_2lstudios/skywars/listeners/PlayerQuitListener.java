package dev._2lstudios.skywars.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import dev._2lstudios.skywars.game.GameParty;
import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.game.arena.GameArena;
import dev._2lstudios.skywars.managers.PlayerManager;

public class PlayerQuitListener implements Listener {
  private final PlayerManager playerManager;

  public PlayerQuitListener(PlayerManager playerManager) {
    this.playerManager = playerManager;
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    GamePlayer gamePlayer = this.playerManager.getPlayer(player);
    GameArena gameArena = gamePlayer.getArena();
    GameParty gameParty = gamePlayer.getParty();

    if (gameArena != null) {
      gameArena.remove(gamePlayer);
    }

    if (gameParty != null) {
      if (gameParty.getOwner() != gamePlayer) {
        gameParty.sendMessage(ChatColor.RED + player.getName() + " salio de la party!");
        gamePlayer.setParty(null);
      } else {
        gameParty
            .sendMessage(ChatColor.RED + "La party fue eliminada por que " + player.getName() + " salio del servidor!");
        gameParty.disband();
      }
    }

    gamePlayer.clear(GameMode.ADVENTURE);
    this.playerManager.removeGamePlayer(player);
  }
}
