package dev._2lstudios.skywars.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev._2lstudios.skywars.events.SpectatorJoinArenaEvent;
import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.GameArena;

public class SpectatorJoinArenaListener implements Listener {
  @EventHandler
  public void onSpectatorJoinArena(SpectatorJoinArenaEvent event) {
    final GameArena arena = event.getArena();
    final GamePlayer gamePlayer = event.getGamePlayer();

    if (arena.getState() == GameState.EDITING) {
      gamePlayer.sendMessage(ChatColor.RED + "La arena esta en mantenimiento temporal!");
      return;
    }

    if (arena.getPlayers().getPlayers().contains(gamePlayer)
        || arena.getPlayers().getSpectators().contains(gamePlayer)) {
      gamePlayer.sendMessage(ChatColor.RED + "Ya estas espectando esa arena!");
      return;
    }

    if (gamePlayer.getArena() != null) {
      gamePlayer.getArena().getPlayers().getPlayers().remove(gamePlayer);
    }

    gamePlayer.setArena(arena);
    arena.getPlayers().getSpectators().add(gamePlayer);
    gamePlayer.clear(GameMode.ADVENTURE, false, true, true);

    Player player = gamePlayer.getPlayer();

    if (player.getWorld() != arena.getWorld()) {
      player.teleport(arena.getSpectatorSpawn(gamePlayer));
    }

    gamePlayer.giveItems(0);

    player.sendMessage(ChatColor.RED + "Puedes salir del modo espectador con /salir!");
  }
}
