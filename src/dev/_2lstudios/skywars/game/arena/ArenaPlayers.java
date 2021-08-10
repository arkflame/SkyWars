package dev._2lstudios.skywars.game.arena;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Server;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import dev._2lstudios.skywars.events.PlayerJoinArenaEvent;
import dev._2lstudios.skywars.events.PlayerQuitArenaEvent;
import dev._2lstudios.skywars.events.SpectatorJoinArenaEvent;
import dev._2lstudios.skywars.events.SpectatorQuitArenaEvent;
import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.game.GamePlayerMode;

public class ArenaPlayers {
  private final Map<UUID, GamePlayer> players = new ConcurrentHashMap<>();
  private final Server server;
  private final GameArena arena;

  ArenaPlayers(final Server server, final GameArena arena) {
    this.server = server;
    this.arena = arena;
  }

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
    final Event event;

    if (gamePlayer.getPlayerMode() == GamePlayerMode.PLAYER) {
      event = new PlayerJoinArenaEvent(gamePlayer, arena);
    } else if (gamePlayer.getPlayerMode() == GamePlayerMode.SPECTATOR) {
      event = new SpectatorJoinArenaEvent(gamePlayer, arena);
    } else {
      event = null;
    }

    if (event instanceof Cancellable && !((Cancellable) event).isCancelled()) {
      server.getPluginManager().callEvent(event);
      players.put(gamePlayer.getUUID(), gamePlayer);
    }
  }

  public void addPlayer(GamePlayer gamePlayer) {
    gamePlayer.setPlayerMode(GamePlayerMode.PLAYER);

    add(gamePlayer);
  }

  public void addSpectator(GamePlayer gamePlayer) {
    gamePlayer.setPlayerMode(GamePlayerMode.SPECTATOR);

    add(gamePlayer);
  }

  public void removePlayer(GamePlayer gamePlayer) {
    if (players.remove(gamePlayer.getUUID()) != null) {
      server.getPluginManager().callEvent(new PlayerQuitArenaEvent(gamePlayer, arena));
    }
  }

  public void removeSpectator(GamePlayer gamePlayer) {
    if (players.remove(gamePlayer.getUUID()) != null) {
      server.getPluginManager().callEvent(new SpectatorQuitArenaEvent(gamePlayer, arena));
    }
  }

  public void removePlayers() {
    for (GamePlayer gamePlayer : new HashSet<>(getPlayers())) {
      removePlayer(gamePlayer);
    }
  }

  public void removeSpectators() {
    for (GamePlayer gamePlayer : new HashSet<>(getSpectators())) {
      removeSpectator(gamePlayer);
    }
  }

  public boolean isEmpty() {
    return players.isEmpty();
  }

  public int size() {
    return players.size();
  }

  public GamePlayer getFirstPlayer() {
    for (final GamePlayer gamePlayer : players.values()) {
      return gamePlayer;
    }

    return null;
  }

  public void remove(GamePlayer gamePlayer) {
    removePlayer(gamePlayer);
    removeSpectator(gamePlayer);
  }
}
