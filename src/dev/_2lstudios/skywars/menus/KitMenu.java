package dev._2lstudios.skywars.menus;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import dev._2lstudios.skywars.game.GameKit;
import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.managers.KitManager;

public class KitMenu implements GameMenu {
  private static final int INVENTORY_SIZE = 54;
  private final String title = ChatColor.DARK_GRAY + "SkyWars - Kits";
  private final KitManager kitManager;
  private final ItemStack openItem = new ItemStack(Material.STORAGE_MINECART, 1);
  private final int[] slots = new int[28];

  KitMenu(KitManager kitManager) {
    this.kitManager = kitManager;
    ItemMeta openItemMeta = this.openItem.getItemMeta();
    openItemMeta.setDisplayName(ChatColor.YELLOW + "Menu de Kits");
    this.openItem.setItemMeta(openItemMeta);
    int index = 0;
    int slotCounter = 0;
    for (int slot = 10; slot < INVENTORY_SIZE && this.slots.length > index; slot++) {
      if (slotCounter++ < 7) {
        this.slots[index++] = slot;
      } else if (slotCounter > 8) {
        slotCounter = 0;
      }
    }
  }

  public Inventory getInventory(GamePlayer gamePlayer) {
    Inventory inventory = Bukkit.createInventory(null, INVENTORY_SIZE, this.title);
    int index = 0;
    for (GameKit gameKit : this.kitManager.getKits()) {
      ItemStack itemStack = gameKit.getIcon();
      ItemMeta itemMeta = itemStack.getItemMeta();
      String name = gameKit.getName();
      List<String> lore = new ArrayList<>();
      if (gamePlayer.getArena() == null) {
        for (String line : gameKit.getLore()) {
          line = line.replace("%option%", ChatColor.GREEN + "Click para comprar!");
          lore.add(line);
        }
      } else {
        for (String line : gameKit.getLore()) {
          line = line.replace("%option%", ChatColor.GREEN + "Click para equipar!");
          lore.add(line);
        }
      }
      itemMeta.setDisplayName(ChatColor.GREEN + name.substring(0, 1).toUpperCase() + name.substring(1));
      itemMeta.setLore(lore);
      itemStack.setItemMeta(itemMeta);
      if (index < this.slots.length) {
        inventory.setItem(this.slots[index], itemStack);
        index++;
      }
    }
    return inventory;
  }

  public void runAction(int slot, ItemStack itemStack, GamePlayer gamePlayer) {
    if (itemStack != null) {
      ItemMeta itemMeta = itemStack.getItemMeta();
      if (itemMeta != null && itemMeta.hasDisplayName()) {
        boolean isInSlot = false;
        for (int slot1 : this.slots) {
          if (slot == slot1) {
            isInSlot = true;
            break;
          }
        }
        if (isInSlot) {
          String displayName = itemMeta.getDisplayName();
          String kitName = ChatColor.stripColor(displayName.toLowerCase());
          Player player = gamePlayer.getPlayer();
          gamePlayer.setSelectedKit(kitName);
          player.sendMessage(
              ChatColor.translateAlternateColorCodes('&', "&aSeleccionaste el kit &b" + displayName + "&a!"));
          player.closeInventory();
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
    return MenuType.KIT;
  }
}
