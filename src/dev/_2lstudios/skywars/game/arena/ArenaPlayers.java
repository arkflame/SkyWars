package dev._2lstudios.skywars.game.arena;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Server;

import dev._2lstudios.skywars.events.PlayerJoinArenaEvent;
import dev._2lstudios.skywars.events.PlayerQuitArenaEvent;
import dev._2lstudios.skywars.events.SpectatorJoinArenaEvent;
import dev._2lstudios.skywars.events.SpectatorQuitArenaEvent;
import dev._2lstudios.skywars.game.GamePlayer;

public class ArenaPlayers {
  private final Collection<GamePlayer> players = new HashSet<>();
  private final Collection<GamePlayer> spectators = new HashSet<>();
  private final Server server;
  private final GameArena arena;

  ArenaPlayers(final Server server, final GameArena arena) {
    this.server = server;
    this.arena = arena;
  }

  public Collection<GamePlayer> getPlayers() {
    return this.players;
  }

  public Collection<GamePlayer> getSpectators() {
    return this.spectators;
  }

  public void addPlayer(GamePlayer gamePlayer) {
    server.getPluginManager().callEvent(new PlayerJoinArenaEvent(gamePlayer, arena));
  }

  public void addSpectator(GamePlayer gamePlayer) {
    server.getPluginManager().callEvent(new SpectatorJoinArenaEvent(gamePlayer, arena));
  }

  public void remove(GamePlayer gamePlayer) {
    if (this.players.remove(gamePlayer)) {
      server.getPluginManager().callEvent(new PlayerQuitArenaEvent(gamePlayer, arena));
    }

    if (this.spectators.remove(gamePlayer)) {
      server.getPluginManager()
          .callEvent(new SpectatorQuitArenaEvent(gamePlayer, arena));
    }
  }

  public void removePlayers() {
    for (GamePlayer gamePlayer : getPlayers()) {
      remove(gamePlayer);
    }
  }

  public void removeSpectators() {
    for (GamePlayer gamePlayer : getSpectators()) {
      remove(gamePlayer);
    }
  }

  public boolean isEmpty() {
    return players.isEmpty();
  }

  public int size() {
    return players.size();
  }

  public GamePlayer getFirstPlayer() {
    for (final GamePlayer gamePlayer : players) {
      return gamePlayer;
    }

    return null;
  }
}
