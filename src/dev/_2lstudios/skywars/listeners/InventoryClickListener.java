package dev._2lstudios.skywars.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.GameArena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.managers.PlayerManager;
import dev._2lstudios.skywars.menus.MenuManager;

public class InventoryClickListener implements Listener {
  private final PlayerManager playerManager;
  
  private final MenuManager menuManager;
  
  public InventoryClickListener(PlayerManager playerManager, MenuManager menuManager) {
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
        GameArena gameArena = gamePlayer.getArena();
        if (gameArena == null || gamePlayer.isSpectating() || gameArena.getState() != GameState.PLAYING)
          event.setCancelled(true); 
      } 
    } 
  }
}
