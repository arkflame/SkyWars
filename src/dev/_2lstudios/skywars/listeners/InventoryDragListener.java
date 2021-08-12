package dev._2lstudios.skywars.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;
import dev._2lstudios.skywars.menus.MenuManager;

public class InventoryDragListener implements Listener {
  private final MenuManager menuManager;
  
  private final GamePlayerManager playerManager;
  
  public InventoryDragListener(MenuManager menuManager, GamePlayerManager playerManager) {
    this.menuManager = menuManager;
    this.playerManager = playerManager;
  }
  
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onInventoryDrag(InventoryDragEvent event) {
    String inventoryTitle = event.getInventory().getTitle();
    GameMenu gameMenu = this.menuManager.getMenu(inventoryTitle);

    if (gameMenu != null) {
      event.setCancelled(true);
    } else {
      HumanEntity whoClicked = event.getWhoClicked();

      if (whoClicked instanceof Player) {
        Player player = (Player)whoClicked;
        GamePlayer gamePlayer = this.playerManager.getPlayer(player);

        if (gamePlayer != null) {
          Arena arena = gamePlayer.getArena();
          
          if (gamePlayer.isSpectating() || arena == null || arena.getState() != GameState.PLAYING)
            event.setCancelled(true); 
        } 
      } 
    } 
  }
}
