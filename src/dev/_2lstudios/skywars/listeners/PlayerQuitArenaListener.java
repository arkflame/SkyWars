package dev._2lstudios.skywars.listeners;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.events.PlayerQuitArenaEvent;
import dev._2lstudios.skywars.game.GameParty;
import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.GameArena;
import dev._2lstudios.skywars.utils.BukkitUtil;

public class PlayerQuitArenaListener implements Listener {
  @EventHandler
  public void onPlayerQuit(final PlayerQuitArenaEvent event) {
    final GamePlayer gamePlayer = event.getGamePlayer();
    final GameArena gameArena = event.getArena();
    final Player player = gamePlayer.getPlayer();
    final UUID uuid = gamePlayer.getUUID();

    if (player != null) {
      final int playersSize = gameArena.getPlayers().size();

      if (gameArena.getState() == GameState.WAITING) {
        gameArena.sendMessage(ChatColor.GRAY + player.getDisplayName() + ChatColor.YELLOW + " salio de la partida ("
            + ChatColor.AQUA + playersSize + ChatColor.YELLOW + "/" + ChatColor.AQUA + gameArena.getSpawns().size()
            + ChatColor.YELLOW + ")!");
      } else {
        if (playersSize > 0) {
          final GamePlayer killer = SkyWars.getMainManager().getPlayerManager().getPlayer(player.getKiller());

          if (killer != null) {
            gameArena.sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&7" + player.getDisplayName() + "&e fue eliminado por &7"
                    + killer.getDisplayName() + "! \n&cQuedan " + playersSize + " jugadores vivos!"));
          } else {
            gameArena.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + player.getDisplayName()
                + "&e fue eliminado! \n" + "&cQuedan " + playersSize + " jugadores vivos!"));
          }

          gameArena.sendSound("ORB_PICKUP", 1.0F);
          BukkitUtil.sendTitle(player, ChatColor.translateAlternateColorCodes('&', "&c&lFIN DEL JUEGO"),
              ChatColor.translateAlternateColorCodes('&', "&7No has ganado esta vez"), 20, 20, 20);
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
            "&eGracias por jugar nuestro &cSkyWars&e!\n&aUsa el comando &b/sw join&a para jugar de nuevo!"));
      }

      final GameParty gameParty = gamePlayer.getParty();

      if (gameParty != null && gameParty.getOwner() == gamePlayer) {
        if (gameArena.getState() == GameState.WAITING) {
          final Collection<GamePlayer> members = gameParty.getMembers();
          for (final GamePlayer partyMember : members) {
            if (partyMember != gamePlayer && partyMember != null)
              gameArena.remove(partyMember);
          }
        } else {
          gameParty.sendMessage(ChatColor.RED + "El owner de la party ha salido de la arena!");
        }
      }
    }

    gameArena.removeChestVote(uuid);
    gameArena.removeTimeVote(uuid);
    gamePlayer.setGameSpawn(null);
    gamePlayer.setArena(null);

    if (player.getKiller() == null) {
      final Server server = Bukkit.getServer();

      player.teleport(server.getWorlds().get(0).getSpawnLocation());
      gamePlayer.clear(GameMode.ADVENTURE);
      gamePlayer.giveItems(0);
    }
  }
}
