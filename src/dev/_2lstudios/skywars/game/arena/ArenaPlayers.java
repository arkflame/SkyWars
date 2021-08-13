package dev._2lstudios.skywars.game.arena;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerMode;

public class ArenaPlayers {
  private final Map<UUID, GamePlayer> players = new ConcurrentHashMap<>();

  private Collection<GamePlayer> getPlayers(GamePlayerMode filterMode) {
    final Collection<GamePlayer> filteredPlayers = new HashSet<>();

    for (final GamePlayer gamePlayer : players.values()) {
      if (gamePlayer.getPlayerMode() == filterMode) {
        filteredPlayers.add(gamePlayer);
      }
    }

    return filteredPlayers;
  }

  public Collection<GamePlayer> getPlayers() {
    return getPlayers(GamePlayerMode.PLAYER);
  }

  public Collection<GamePlayer> getSpectators() {
    return getPlayers(GamePlayerMode.SPECTATOR);
  }

  public void add(GamePlayer gamePlayer) {
    players.put(gamePlayer.getUUID(), gamePlayer);
  }

  public void addPlayer(GamePlayer gamePlayer) {
    gamePlayer.setPlayerMode(GamePlayerMode.PLAYER);

    add(gamePlayer);
  }

  public void addSpectator(GamePlayer gamePlayer) {
    gamePlayer.setPlayerMode(GamePlayerMode.SPECTATOR);

    add(gamePlayer);
  }

  public void remove(GamePlayer gamePlayer) {
    players.remove(gamePlayer.getUUID());
  }

  public void removePlayers() {
    for (GamePlayer gamePlayer : new HashSet<>(getPlayers())) {
      gamePlayer.updateArena(null, null);
    }
  }

  public void removeSpectators() {
    for (GamePlayer gamePlayer : new HashSet<>(getSpectators())) {
      gamePlayer.updateArena(null, null);
    }
  }

  public GamePlayer getFirstPlayer() {
    for (final GamePlayer gamePlayer : getPlayers()) {
      return gamePlayer;
    }

    return null;
  }
}
