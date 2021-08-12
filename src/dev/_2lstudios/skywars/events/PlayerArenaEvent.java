package dev._2lstudios.skywars.events;

import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;

public class PlayerArenaEvent extends ArenaEvent {
    private GamePlayer gamePlayer;

    public PlayerArenaEvent(final GamePlayer gamePlayer, final Arena arena) {
        super(arena);
        this.gamePlayer = gamePlayer;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }
}
