package dev._2lstudios.skywars.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.time.TimeManager;
import dev._2lstudios.skywars.time.TimeType;

public class TimeMenu implements GameMenu {
  private final String title = ChatColor.DARK_GRAY + "Votacion de Tiempo";
  
  private final Inventory inventory = Bukkit.createInventory(null, 36, this.title);
  
  private final ItemStack openItem = new ItemStack(Material.WATCH);
  
  private final TimeManager timeManager;
  
  public TimeMenu(TimeManager timeManager) {
    this.timeManager = timeManager;
    ItemMeta openItemMeta = this.openItem.getItemMeta();
    openItemMeta.setDisplayName(ChatColor.YELLOW + "Tiempo");
    this.openItem.setItemMeta(openItemMeta);
    this.inventory.setItem(10, timeManager.getOpenItem(TimeType.MORNING));
    this.inventory.setItem(12, timeManager.getOpenItem(TimeType.DAY));
    this.inventory.setItem(14, timeManager.getOpenItem(TimeType.NOON));
    this.inventory.setItem(16, timeManager.getOpenItem(TimeType.NIGHT));
  }
  
  public Inventory getInventory(GamePlayer gamePlayer) {
    return this.inventory;
  }
  
  public void runAction(int slot, ItemStack itemStack, GamePlayer gamePlayer) {
    if (itemStack != null) {
      ItemMeta itemMeta = itemStack.getItemMeta();
      if (itemMeta != null) {
        String displayName = itemMeta.getDisplayName();
        if (displayName != null) {
          Arena arena = gamePlayer.getArena();
          if (arena != null) {
            Player player = gamePlayer.getPlayer();
            if (player.hasPermission("skywars.votetime")) {
              for (TimeType timeType : TimeType.values()) {
                if (this.timeManager.isOpenItem(timeType, itemStack)) {
                  arena.addTimeVote(player.getUniqueId(), timeType);
                  break;
                } 
              } 
            } else {
              player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNecesitas rango &d&lMEGA&c para votar cofres!"));
            } 
            player.closeInventory();
          } 
        } 
      } 
    } 
  }
  
  public String getTitle() {
    return this.title;
  }
  
  public ItemStack getOpenItem() {
    return this.openItem;
  }
  
  public MenuType getType() {
    return MenuType.TIME;
  }
}
