package dev._2lstudios.skywars.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;

public class InventoryClickListener implements Listener {
  private final GamePlayerManager playerManager;

  public InventoryClickListener(final GamePlayerManager playerManager) {
    this.playerManager = playerManager;
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryClick(final InventoryClickEvent event) {
    final HumanEntity whoClicked = event.getWhoClicked();

    if (whoClicked instanceof Player) {
      final Player player = (Player) whoClicked;
      final GamePlayer gamePlayer = this.playerManager.getPlayer(player);

      if (gamePlayer != null) {
        final Arena arena = gamePlayer.getArena();

        if (gamePlayer.isSpectating() || arena == null || arena.getState() != GameState.PLAYING)
          event.setCancelled(true);
      }
    }
  }
}
