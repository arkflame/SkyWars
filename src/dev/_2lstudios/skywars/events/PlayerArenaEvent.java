package dev._2lstudios.skywars.events;

import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.game.arena.GameArena;

public class PlayerArenaEvent extends ArenaEvent {
    private GamePlayer gamePlayer;

    public PlayerArenaEvent(final GamePlayer gamePlayer, final GameArena gameArena) {
        super(gameArena);
        this.gamePlayer = gamePlayer;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }
}
