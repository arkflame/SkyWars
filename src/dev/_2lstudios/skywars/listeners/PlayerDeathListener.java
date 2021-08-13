package dev._2lstudios.skywars.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;
import dev._2lstudios.skywars.game.player.GamePlayerMode;
import dev._2lstudios.skywars.utils.BukkitUtil;

public class PlayerDeathListener implements Listener {
  private final GamePlayerManager playerManager;

  public PlayerDeathListener(GamePlayerManager playerManager) {
    this.playerManager = playerManager;
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getEntity();
    if (player != null) {
      GamePlayer gamePlayer = this.playerManager.getPlayer(player);
      Arena arena = gamePlayer.getArena();

      if (arena != null) {
        Player killer = player.getKiller();
        final int playersSize = arena.getPlayers().getPlayers().size() - 1;

        if (killer != null) {
          arena.getKills().addKill(this.playerManager.getPlayer(killer));
          arena.sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&7" + player.getDisplayName() + "&e fue eliminado por &7"
                    + killer.getDisplayName() + "! \n&cQuedan " + playersSize + " jugadores vivos!"));
        } else {
          arena.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + player.getDisplayName()
                + "&e fue eliminado! \n" + "&cQuedan " + playersSize + " jugadores vivos!"));
        }

        gamePlayer.updateArena(arena, GamePlayerMode.SPECTATOR);
        arena.sendSound("ORB_PICKUP", 1.0F);

        BukkitUtil.sendTitle(player, ChatColor.translateAlternateColorCodes('&', "&c&lFIN DEL JUEGO"),
              ChatColor.translateAlternateColorCodes('&', "&7No has ganado esta vez"), 20, 20, 20);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
            "&eGracias por jugar nuestro &cSkyWars&e!\n&aUsa el comando &b/sw join&a para jugar de nuevo!"));
      }
    }
  }
}
