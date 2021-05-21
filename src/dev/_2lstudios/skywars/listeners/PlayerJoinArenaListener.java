package dev._2lstudios.skywars.listeners;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.events.PlayerJoinArenaEvent;
import dev._2lstudios.skywars.game.GameParty;
import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.ArenaSpawn;
import dev._2lstudios.skywars.game.arena.GameArena;
import dev._2lstudios.skywars.managers.CageManager;

public class PlayerJoinArenaListener implements Listener {
  @EventHandler
  public void onPlayerJoinArena(PlayerJoinArenaEvent event) {
    final GamePlayer gamePlayer = event.getGamePlayer();
    final GameArena arena = event.getArena();

    if (arena.getPlayers().getPlayers().contains(gamePlayer)) {
      gamePlayer.sendMessage(ChatColor.RED + "Ya estas dentro de la arena que intentas acceder!");
      return;
    }
    if (arena.getState() != GameState.WAITING) {
      gamePlayer.sendMessage(ChatColor.RED + "La arena que intentas acceder esta en juego!");
      return;
    }
    if (arena.getPlayers().size() >= arena.getArenaWorld().getSpawns().size()) {
      gamePlayer.sendMessage(ChatColor.RED + "La arena que intentas acceder esta llena!");
      return;
    }

    final ArenaSpawn arenaSpawn = arena.getArenaWorld().getFirstSpawn();

    if (arenaSpawn != null) {
      GameParty gameParty = gamePlayer.getParty();
      CageManager cageManager = SkyWars.getMainManager().getCageManager();
      GameArena lastGameArena = gamePlayer.getArena();
      Player player = gamePlayer.getPlayer();

      if (lastGameArena != null) {
        lastGameArena.remove(gamePlayer);
      }

      gamePlayer.setArena(arena);
      arena.getPlayers().getPlayers().add(gamePlayer);
      gamePlayer.setGameSpawn(arenaSpawn);
      gamePlayer.clear(GameMode.ADVENTURE);
      gamePlayer.giveItems(1);

      arenaSpawn.createCage(cageManager.getCage(gamePlayer.getSelectedCage()));
      arena.sendMessage(ChatColor.GRAY + player.getDisplayName() + ChatColor.YELLOW + " entro a la partida ("
          + ChatColor.AQUA + arena.getPlayers().size() + ChatColor.YELLOW + "/" + ChatColor.AQUA
          + arena.getArenaWorld().getSpawns().size() + ChatColor.YELLOW + ")!");
      if (gameParty != null && gameParty.getOwner() == gamePlayer) {
        Collection<GamePlayer> members = gameParty.getMembers();
        for (GamePlayer gamePlayer1 : members) {
          gamePlayer1.getPlayer().sendMessage(ChatColor.GOLD + "Ingresando al mapa del owner de la party...");
          arena.addPlayer(gamePlayer1);
        }
      }

      for (final GamePlayer spectator : arena.getPlayers().getSpectators()) {
        spectator.update();
      }
    }
  }
}
