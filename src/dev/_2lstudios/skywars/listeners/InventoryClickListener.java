package dev._2lstudios.skywars.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;
import dev._2lstudios.skywars.menus.MenuManager;

public class InventoryClickListener implements Listener {
  private final GamePlayerManager playerManager;
  
  private final MenuManager menuManager;
  
  public InventoryClickListener(GamePlayerManager playerManager, MenuManager menuManager) {
    this.playerManager = playerManager;
    this.menuManager = menuManager;
  }
  
  @EventHandler(ignoreCancelled = true)
  public void onInventoryClick(InventoryClickEvent event) {
    String inventoryTitle = event.getInventory().getTitle();
    GameMenu gameMenu = this.menuManager.getMenu(inventoryTitle);
    if (gameMenu != null)
      event.setCancelled(true); 
    HumanEntity humanEntity = event.getWhoClicked();
    if (humanEntity instanceof Player) {
      GamePlayer gamePlayer = this.playerManager.getPlayer((Player)humanEntity);
      if (gameMenu != null) {
        gameMenu.runAction(event.getSlot(), event.getCurrentItem(), gamePlayer);
        event.setCancelled(true);
      } else {
        Arena arena = gamePlayer.getArena();
        if (arena == null || gamePlayer.isSpectating() || arena.getState() != GameState.PLAYING)
          event.setCancelled(true); 
      } 
    } 
  }
}
