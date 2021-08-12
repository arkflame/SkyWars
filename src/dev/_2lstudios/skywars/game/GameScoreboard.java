package dev._2lstudios.skywars.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.scoreboard.Main;
import dev._2lstudios.scoreboard.managers.SidebarManager;
import dev._2lstudios.skywars.game.arena.GameArena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.managers.PlayerManager;
import dev._2lstudios.skywars.utils.ConfigurationUtil;

public class GameScoreboard implements Listener {
  private final PlayerManager playerManager;
  private final Collection<String> lobbyScoreboard;
  private final Collection<String> spectatorScoreboard;
  private final Collection<String> pregameScoreboard;
  private final Collection<String> ingameScoreboard;

  public GameScoreboard(Plugin plugin, ConfigurationUtil configurationUtil, PlayerManager playerManager) {
    YamlConfiguration yamlConfiguration = configurationUtil.getConfiguration("%datafolder%/scoreboards.yml");
    Server server = plugin.getServer();

    this.playerManager = playerManager;
    this.lobbyScoreboard = yamlConfiguration.getStringList("scoreboards.lobby");
    this.spectatorScoreboard = yamlConfiguration.getStringList("scoreboards.spectator");
    this.pregameScoreboard = yamlConfiguration.getStringList("scoreboards.pregame");
    this.ingameScoreboard = yamlConfiguration.getStringList("scoreboards.ingame");

    server.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
      SidebarManager sidebarManager = Main.getEssentialsManager().getVariableManager().getSidebarManager();

      for (Player player : server.getOnlinePlayers()) {
        update(player, sidebarManager);
      }
    }, 20L, 20L);
  }

  private void update(Player player, SidebarManager sidebarManager) {
    if (player != null && !player.isDead() && player.isOnline()) {
      GamePlayer gamePlayer = this.playerManager.getPlayer(player);
      if (gamePlayer != null) {
        Collection<String> list;
        final GameArena gameArena = gamePlayer.getArena();
        final List<String> newList = new ArrayList<>();

        if (gameArena == null) {
          list = this.lobbyScoreboard;
        } else if (gamePlayer.isSpectating()) {
          list = this.spectatorScoreboard;
        } else if (gameArena.getState() == GameState.WAITING) {
          list = this.pregameScoreboard;
        } else if (gameArena.getState() == GameState.PLAYING) {
          list = this.ingameScoreboard;
        } else {
          list = this.lobbyScoreboard;
        }

        for (String line : list) {
          newList.add(replacePlaceholders(gameArena, gamePlayer, line));
        }

        sidebarManager.setCustomSidebar(player, newList);
      }
    }
  }

  private String replacePlaceholders(GameArena gameArena, GamePlayer gamePlayer, String message) {
    if (gameArena != null && gamePlayer != null) {
      message = message.replace("%players%", String.valueOf(gameArena.getPlayers().size()))
          .replace("%maxplayers%", String.valueOf(gameArena.getSpawns().size()))
          .replace("%mostvotedchest%", gameArena.getMostVotedChest().toString()).replace("%map%", gameArena.getName())
          .replace("%wins%", String.valueOf(gamePlayer.getWins()))
          .replace("%kills%", String.valueOf(gameArena.getKills(gamePlayer.getPlayer().getName()).amount()));
    } else if (gamePlayer != null) {
      message = message.replace("%wins%", String.valueOf(gamePlayer.getWins()));
    }
    return message;
  }
}
