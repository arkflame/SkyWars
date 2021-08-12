package dev._2lstudios.skywars.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.player.GamePlayer;

public class ShopMenu implements GameMenu {
  private final MenuManager menuManager;
  
  private final String title = ChatColor.DARK_GRAY + "SkyWars - Tienda";
  
  private final Inventory inventory = Bukkit.createInventory(null, 54, this.title);
  
  private final ItemStack openItem = new ItemStack(Material.CHEST, 1);
  
  private final ItemStack closeItem = new ItemStack(Material.ARROW, 1);
  
  public ShopMenu(MenuManager menuManager) {
    this.menuManager = menuManager;
    ItemMeta openItemMeta = this.openItem.getItemMeta();
    ItemMeta closeItemMeta = this.closeItem.getItemMeta();
    ItemStack particlesItem = new ItemStack(Material.BLAZE_POWDER, 1);
    ItemMeta particlesItemMeta = particlesItem.getItemMeta();
    openItemMeta.setDisplayName(ChatColor.YELLOW + "Menu de Tienda");
    closeItemMeta.setDisplayName(ChatColor.RED + "Cerrar");
    particlesItemMeta.setDisplayName(ChatColor.YELLOW + "Particulas");
    this.openItem.setItemMeta(openItemMeta);
    this.closeItem.setItemMeta(closeItemMeta);
    particlesItem.setItemMeta(particlesItemMeta);
    this.inventory.setItem(10, menuManager.getMenu(MenuType.KIT).getOpenItem());
    this.inventory.setItem(13, menuManager.getMenu(MenuType.CAGE).getOpenItem());
    this.inventory.setItem(16, particlesItem);
    this.inventory.setItem(49, this.closeItem);
  }
  
  public ItemStack getOpenItem() {
    return this.openItem;
  }
  
  public Inventory getInventory(GamePlayer gamePlayer) {
    return this.inventory;
  }
  
  public void runAction(int slot, ItemStack itemStack, GamePlayer gamePlayer) {
    if (itemStack != null)
      if (itemStack.isSimilar(this.closeItem)) {
        gamePlayer.getPlayer().closeInventory();
      } else {
        GameMenu kitMenu = this.menuManager.getMenu(MenuType.KIT);
        GameMenu cageMenu = this.menuManager.getMenu(MenuType.CAGE);
        if (itemStack.isSimilar(kitMenu.getOpenItem())) {
          gamePlayer.getPlayer().openInventory(kitMenu.getInventory(gamePlayer));
        } else if (itemStack.isSimilar(cageMenu.getOpenItem())) {
          gamePlayer.getPlayer().openInventory(cageMenu.getInventory(gamePlayer));
        } 
      }  
  }
  
  public String getTitle() {
    return this.title;
  }
  
  public MenuType getType() {
    return MenuType.SHOP;
  }
}
