package dev._2lstudios.skywars.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dev._2lstudios.skywars.game.arena.GameArena;

public class ArenaEvent extends Event {
  private static final HandlerList HANDLERS = new HandlerList();
  private final GameArena gameArena;

  public ArenaEvent(GameArena gameArena) {
    this.gameArena = gameArena;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  public HandlerList getHandlers() {
    return getHandlerList();
  }

  public GameArena getArena() {
    return this.gameArena;
  }
}
