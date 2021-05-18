package dev._2lstudios.skywars.listeners;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.PlayerInventory;
import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.managers.PlayerManager;
import dev._2lstudios.skywars.menus.MenuManager;
import dev._2lstudios.skywars.menus.MenuType;

public class PlayerJoinListener implements Listener {
  private final SkyWars skywars;
  
  private final MenuManager menuManager;
  
  private final PlayerManager playerManager;
  
  public PlayerJoinListener(SkyWars skywars, MenuManager menuManager, PlayerManager playerManager) {
    this.skywars = skywars;
    this.menuManager = menuManager;
    this.playerManager = playerManager;
  }
  
  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (!player.isOnline())
      return; 
    GamePlayer gamePlayer = this.playerManager.addGamePlayer(player);
    PlayerInventory playerInventory = player.getInventory();
    player.teleport(((World)this.skywars.getServer().getWorlds().get(0)).getSpawnLocation());
    gamePlayer.clear(GameMode.ADVENTURE, true, false, true);
    playerInventory.setItem(0, this.menuManager.getMenu(MenuType.MAP).getOpenItem());
    playerInventory.setItem(1, SkyWars.getRandomMapItem());
    playerInventory.setItem(4, this.menuManager.getMenu(MenuType.SHOP).getOpenItem());
    playerInventory.setItem(8, this.menuManager.getMenu(MenuType.SPECTATOR).getOpenItem());
  }
}
