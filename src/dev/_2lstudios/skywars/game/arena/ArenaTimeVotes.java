package dev._2lstudios.skywars.game.arena;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.time.TimeType;
import dev._2lstudios.skywars.time.TimeVote;

public class ArenaTimeVotes {
    private final Arena arena;
    private final Collection<TimeVote> timeVotes = new HashSet<>();

    public ArenaTimeVotes(final Arena arena) {
        this.arena = arena;
    }

    public TimeType getMostVotedTime() {
        Map<TimeType, Integer> votes = new EnumMap<>(TimeType.class);
        TimeType mostVoted = TimeType.DIA;
        int mostVotedNumber = 0;
        for (TimeVote timeVote : this.timeVotes)
            votes.put(timeVote.getType(), Integer
                    .valueOf(((Integer) votes.getOrDefault(timeVote.getType(), Integer.valueOf(0))).intValue() + 1));
        for (Map.Entry<TimeType, Integer> voteEntry : votes.entrySet()) {
            TimeType timeType = voteEntry.getKey();
            int number = ((Integer) voteEntry.getValue()).intValue();
            if (number > mostVotedNumber) {
                mostVoted = timeType;
                mostVotedNumber = number;
            }
        }
        return mostVoted;
    }

    public void addTimeVote(UUID uuid, TimeType timeType) {
        Iterator<TimeVote> iterator = this.timeVotes.iterator();
        TimeType timeTypeNow = null;
        while (iterator.hasNext()) {
            TimeVote timeVote = iterator.next();
            if (timeVote.getUUID() == uuid) {
                timeTypeNow = timeVote.getType();
                iterator.remove();
                break;
            }
        }
        if (timeTypeNow != timeType) {
            GamePlayer gamePlayer = SkyWars.getSkyWarsManager().getPlayerManager().getPlayer(uuid);
            this.timeVotes.add(new TimeVote(uuid, timeType));
            if (gamePlayer != null) {
                Player player = gamePlayer.getPlayer();
                arena.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + player.getDisplayName()
                        + "&a voto por tiempo &b" + timeType.name().toLowerCase() + "&a!"));
            }
        }
    }

    public void removeTimeVote(UUID uuid) {
        Iterator<TimeVote> iterator = this.timeVotes.iterator();
        while (iterator.hasNext()) {
            TimeVote timeVote = iterator.next();
            if (timeVote.getUUID() == uuid) {
                iterator.remove();
                break;
            }
        }
    }
}
