package dev._2lstudios.skywars.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dev._2lstudios.skywars.game.arena.Arena;

public class ArenaEvent extends Event {
  private static final HandlerList HANDLERS = new HandlerList();
  private final Arena arena;

  public ArenaEvent(Arena arena) {
    this.arena = arena;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  public HandlerList getHandlers() {
    return getHandlerList();
  }

  public Arena getArena() {
    return this.arena;
  }
}
