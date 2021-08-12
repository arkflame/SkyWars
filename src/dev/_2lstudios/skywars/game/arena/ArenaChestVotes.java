package dev._2lstudios.skywars.game.arena;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev._2lstudios.skywars.chest.ChestType;
import dev._2lstudios.skywars.game.player.GamePlayer;

public class ArenaChestVotes {
    private final Arena arena;
    private final Map<UUID, ChestType> chestVotes = new HashMap<>();

    ArenaChestVotes(final Arena arena) {
        this.arena = arena;
    }

    public ChestType getMostVoted() {
        Map<ChestType, Integer> votes = new EnumMap<>(ChestType.class);
        ChestType mostVoted = ChestType.NORMAL;
        int mostVotedNumber = 0;
        for (ChestType chestType : chestVotes.values())
            votes.put(chestType,
                    Integer.valueOf(((Integer) votes.getOrDefault(chestType, Integer.valueOf(0))).intValue() + 1));
        for (Map.Entry<ChestType, Integer> entry : votes.entrySet()) {
            ChestType chestType = entry.getKey();
            int number = ((Integer) entry.getValue()).intValue();
            if (number > mostVotedNumber) {
                mostVoted = chestType;
                mostVotedNumber = number;
            }
        }
        if (mostVotedNumber == 0) {
            int random = (int) (Math.random() * 3.0D);
            if (random == 0)
                return ChestType.INSANE;
            if (random == 1)
                return ChestType.BASIC;
            return ChestType.NORMAL;
        }
        return mostVoted;
    }

    public void addChestVote(GamePlayer gamePlayer, ChestType chestType) {
        if (gamePlayer != null) {
            UUID uuid = gamePlayer.getUUID();
            ChestType chestTypeNow = chestVotes.getOrDefault(uuid, null);
            if (chestTypeNow != chestType) {
                Player player = gamePlayer.getPlayer();
                chestVotes.put(uuid, chestType);
                arena.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7" + player.getDisplayName() + "&a voto por tipo de cofre &b" + chestType.getName() + "&a!"));
            }
        }
    }

    public void removeChestVote(UUID uuid) {
        chestVotes.remove(uuid);
    }

    public void clear() {
        chestVotes.clear();
    }
}
