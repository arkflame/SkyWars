package dev._2lstudios.skywars.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev._2lstudios.skywars.events.SpectatorJoinArenaEvent;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.GameArena;
import dev._2lstudios.skywars.game.player.GamePlayer;

public class SpectatorJoinArenaListener implements Listener {
  @EventHandler
  public void onSpectatorJoinArena(SpectatorJoinArenaEvent event) {
    final GameArena arena = event.getArena();
    final GamePlayer gamePlayer = event.getGamePlayer();

    if (arena.getState() == GameState.EDITING) {
      gamePlayer.sendMessage(ChatColor.RED + "La arena esta en mantenimiento temporal!");
      return;
    }

    if (arena.getPlayers().getSpectators().contains(gamePlayer)) {
      gamePlayer.sendMessage(ChatColor.RED + "Ya estas espectando esa arena!");
      return;
    }

    final GameArena lastArena = gamePlayer.getArena();

    if (lastArena != null) {
      lastArena.remove(gamePlayer);
    }

    Player player = gamePlayer.getPlayer();

    if (player.getWorld() != arena.getWorld()) {
      player.teleport(arena.getSpectatorSpawn(gamePlayer));
    }

    gamePlayer.setArena(arena);
    gamePlayer.clear(GameMode.ADVENTURE);
    gamePlayer.update();
    gamePlayer.giveItems(0);
  }
}
