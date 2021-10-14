package dev._2lstudios.skywars.game;

import java.util.List;

import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;
import dev._2lstudios.skywars.utils.ConfigurationUtil;
import dev._2lstudios.swiftboard.SwiftBoard;
import dev._2lstudios.swiftboard.swift.SwiftSidebar;

public class GameSidebar implements Listener {
  private final GamePlayerManager playerManager;
  private final SwiftSidebar swiftSidebar;
  private final List<String> lobbyScoreboard;
  private final List<String> spectatorScoreboard;
  private final List<String> pregameScoreboard;
  private final List<String> ingameScoreboard;

  public GameSidebar(Plugin plugin, ConfigurationUtil configurationUtil, GamePlayerManager playerManager) {
    YamlConfiguration yamlConfiguration = configurationUtil.getConfiguration("%datafolder%/scoreboards.yml");
    Server server = plugin.getServer();

    this.playerManager = playerManager;
    this.swiftSidebar = SwiftBoard.getSwiftSidebar();
    this.lobbyScoreboard = yamlConfiguration.getStringList("scoreboards.lobby");
    this.spectatorScoreboard = yamlConfiguration.getStringList("scoreboards.spectator");
    this.pregameScoreboard = yamlConfiguration.getStringList("scoreboards.pregame");
    this.ingameScoreboard = yamlConfiguration.getStringList("scoreboards.ingame");

    server.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
      for (Player player : server.getOnlinePlayers()) {
        update(player);
      }
    }, 20L, 20L);
  }

  public void update(Player player) {
    if (player != null && !player.isDead() && player.isOnline()) {
      GamePlayer gamePlayer = this.playerManager.getPlayer(player);
      if (gamePlayer != null) {
        final List<String> list;
        final Arena arena = gamePlayer.getArena();

        if (arena == null) {
          list = this.lobbyScoreboard;
        } else if (gamePlayer.isSpectating()) {
          list = this.spectatorScoreboard;
        } else if (arena.getState() == GameState.WAITING) {
          list = this.pregameScoreboard;
        } else if (arena.getState() == GameState.PLAYING) {
          list = this.ingameScoreboard;
        } else {
          list = this.lobbyScoreboard;
        }

        swiftSidebar.setLines(player, list);
      }
    }
  }
}
