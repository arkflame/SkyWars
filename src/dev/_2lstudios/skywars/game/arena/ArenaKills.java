package dev._2lstudios.skywars.game.arena;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dev._2lstudios.skywars.game.player.GamePlayer;

public class ArenaKills {
  private final Map<UUID, PlayerKills> playerKillsMap;
  private final List<PlayerKills> playerKillsList;

  public ArenaKills() {
    this.playerKillsMap = new HashMap<>();
    this.playerKillsList = new ArrayList<>();
  }

  public void clear() {
    playerKillsMap.clear();
    playerKillsList.clear();
  }

  public void add(final GamePlayer gamePlayer, final PlayerKills playerKills) {
    playerKillsMap.put(gamePlayer.getUUID(), playerKills);
    playerKillsList.add(playerKills);
  }

  public void addKill(GamePlayer gamePlayer) {
    PlayerKills playerKills = getKills(gamePlayer);

    if (playerKills.add() == 1) {
      add(gamePlayer, playerKills);
    }

    playerKillsList.sort(Comparator.comparing(PlayerKills::amount).reversed());
  }

  public PlayerKills getKills(GamePlayer gamePlayer) {
    return playerKillsMap.getOrDefault(gamePlayer.getUUID(), new PlayerKills(gamePlayer, 0));
  }

  public PlayerKills getKills(int i) {
    if (i < playerKillsList.size()) {
      return playerKillsList.get(i);
    }

    return new PlayerKills(null, 0);
  }
}
