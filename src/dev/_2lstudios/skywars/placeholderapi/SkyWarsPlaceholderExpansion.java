package dev._2lstudios.skywars.placeholderapi;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class SkyWarsPlaceholderExpansion extends PlaceholderExpansion {
    private final Plugin plugin;
    private final GamePlayerManager playerManager;

    public SkyWarsPlaceholderExpansion(final Plugin plugin, final GamePlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    public String getIdentifier() {
        return "skywars";
    }

    public String getPlugin() {
        return "SkyWars";
    }

    public String getAuthor() {
        return "2LStudios";
    }

    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(final Player player, final String identifier) {
        if (player == null) {
            return "";
        }

        final GamePlayer gamePlayer = playerManager.getPlayer(player);

        if (gamePlayer != null) {
            if (identifier.equalsIgnoreCase("wins")) {
                return String.valueOf(gamePlayer.getWins());
            }

            final Arena arena = gamePlayer.getArena();

            if (arena != null) {
                switch (identifier.toUpperCase()) {
                    case "PLAYERS":
                        return String.valueOf(arena.getPlayers().getPlayers().size());
                    case "MAXPLAYERS":
                        return String.valueOf(arena.getSpawns().size());
                    case "MAP":
                        return arena.getName();
                    case "VOTEDCHEST":
                        return arena.getMostVotedChest().getName();
                    case "KILLS":
                        return String.valueOf(arena.getKills().getKills(gamePlayer).amount());

                }
            }
        }

        return null;
    }
}
