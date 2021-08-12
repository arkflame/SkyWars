package dev._2lstudios.skywars.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.player.GamePlayer;

public class VoteMenu implements GameMenu {
  private final MenuManager menuManager;
  
  private final String title = ChatColor.DARK_GRAY + "Votacion";
  
  private final Inventory inventory = Bukkit.createInventory(null, 36, this.title);
  
  private final ItemStack openItem = new ItemStack(Material.PAPER);
  
  public VoteMenu(MenuManager menuManager) {
    this.menuManager = menuManager;
    ItemMeta openItemMeta = this.openItem.getItemMeta();
    openItemMeta.setDisplayName(ChatColor.YELLOW + "Votacion");
    this.openItem.setItemMeta(openItemMeta);
    this.inventory.setItem(10, menuManager.getMenu(MenuType.CHEST).getOpenItem());
    this.inventory.setItem(13, menuManager.getMenu(MenuType.TIME).getOpenItem());
  }
  
  public Inventory getInventory(GamePlayer gamePlayer) {
    return this.inventory;
  }
  
  public void runAction(int slot, ItemStack itemStack, GamePlayer gamePlayer) {
    if (itemStack != null) {
      GameMenu chestMenu = this.menuManager.getMenu(MenuType.CHEST);
      GameMenu timeMenu = this.menuManager.getMenu(MenuType.TIME);
      if (itemStack.isSimilar(chestMenu.getOpenItem())) {
        gamePlayer.getPlayer().openInventory(chestMenu.getInventory(gamePlayer));
      } else if (itemStack.isSimilar(timeMenu.getOpenItem())) {
        gamePlayer.getPlayer().openInventory(timeMenu.getInventory(gamePlayer));
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
    return MenuType.VOTE;
  }
}
