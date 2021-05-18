package dev._2lstudios.skywars.game.arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaKills {
  private final Map<String, PlayerKills> playerKillsMap = new HashMap<>();
  private final List<PlayerKills> playerKillsList = new ArrayList<>();

  public void addKill(String playerName) {
    PlayerKills gamePlayerKills = getKills(playerName);

    if (gamePlayerKills.amount() == 0) {
      final PlayerKills kills = new PlayerKills(playerName, 1);

      playerKillsMap.put(playerName, kills);
      playerKillsList.add(kills);
    } else {
      gamePlayerKills.add();
    }

    playerKillsList.sort(Comparator.comparing(PlayerKills::amount));
    Collections.reverse(playerKillsList);
  }

  public void clear() {
    playerKillsList.clear();
  }

  public PlayerKills getKills(String playerName) {
    return playerKillsMap.getOrDefault(playerName, new PlayerKills(playerName, 0));
  }

  public PlayerKills getKills(int i) {
    if (i < playerKillsList.size()) {
      return playerKillsList.get(i);
    }

    return new PlayerKills("N/A", 0);
  }
}
