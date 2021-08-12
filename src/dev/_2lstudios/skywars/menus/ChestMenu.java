package dev._2lstudios.skywars.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import dev._2lstudios.skywars.chest.ChestManager;
import dev._2lstudios.skywars.chest.ChestType;
import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;

public class ChestMenu implements GameMenu {
  private final String title = ChatColor.DARK_GRAY + "Votacion de Cofres";
  
  private final Inventory inventory = Bukkit.createInventory(null, 36, this.title);
  
  private final ItemStack openItem = new ItemStack(Material.CHEST);
  
  private final ChestManager chestManager;
  
  public ChestMenu(ChestManager chestManager) {
    this.chestManager = chestManager;
    ItemMeta openItemMeta = this.openItem.getItemMeta();
    openItemMeta.setDisplayName(ChatColor.YELLOW + "Cofres");
    this.openItem.setItemMeta(openItemMeta);
    this.inventory.setItem(10, chestManager.getOpenItem(ChestType.BASIC));
    this.inventory.setItem(13, chestManager.getOpenItem(ChestType.NORMAL));
    this.inventory.setItem(16, chestManager.getOpenItem(ChestType.INSANE));
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
            if (player.hasPermission("skywars.votechest")) {
              if (itemStack.isSimilar(this.chestManager.getOpenItem(ChestType.BASIC))) {
                arena.addChestVote(gamePlayer, ChestType.BASIC);
              } else if (itemStack.isSimilar(this.chestManager.getOpenItem(ChestType.NORMAL))) {
                arena.addChestVote(gamePlayer, ChestType.NORMAL);
              } else if (itemStack.isSimilar(this.chestManager.getOpenItem(ChestType.INSANE))) {
                arena.addChestVote(gamePlayer, ChestType.INSANE);
              } 
            } else {
              player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNecesitas rango &lTITAN&c para votar cofres!"));
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
    return MenuType.CHEST;
  }
}
