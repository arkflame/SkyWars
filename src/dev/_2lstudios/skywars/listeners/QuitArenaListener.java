package dev._2lstudios.skywars.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev._2lstudios.skywars.events.PlayerQuitArenaEvent;
import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.game.arena.GameArena;

public class QuitArenaListener implements Listener {
  @EventHandler
  public void onPlayerQuitArena(PlayerQuitArenaEvent event) {
    GamePlayer gamePlayer = event.getGamePlayer();
    GameArena gameArena = event.getArena();
    UUID uuid = gamePlayer.getUUID();
    gameArena.removeChestVote(uuid);
    gameArena.removeTimeVote(uuid);
    if (gamePlayer != null) {
      Player player = gamePlayer.getPlayer();
      gamePlayer.clear(GameMode.ADVENTURE, true, false, true);
      gamePlayer.setGameSpawn(null);
      gamePlayer.setArena(null);
      if (player != null) {
        Server server = Bukkit.getServer();

        player.teleport(server.getWorlds().get(0).getSpawnLocation());
        gamePlayer.giveItems(0);
      }
    }
  }
}
