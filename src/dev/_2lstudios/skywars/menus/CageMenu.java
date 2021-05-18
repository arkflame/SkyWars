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
import dev._2lstudios.skywars.game.GameCage;
import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.game.arena.ArenaSpawn;
import dev._2lstudios.skywars.managers.CageManager;

public class CageMenu implements GameMenu {
  private static final int INVENTORY_SIZE = 54;

  private final String title = ChatColor.DARK_GRAY + "SkyWars - Jaulas";

  private final CageManager cageManager;

  private final ItemStack openItem = new ItemStack(Material.STAINED_GLASS);

  private final int[] slots = new int[28];

  public CageMenu(CageManager cageManager) {
    this.cageManager = cageManager;
    ItemMeta openItemMeta = this.openItem.getItemMeta();
    openItemMeta.setDisplayName(ChatColor.YELLOW + "Menu de Jaulas");
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
    List<GameCage> gameCages = new ArrayList<>(this.cageManager.getCages());
    int index = 0;
    for (GameCage gameCage : gameCages) {
      if (index < this.slots.length) {
        int slot = this.slots[index];
        ItemStack slotItem = inventory.getItem(slot);
        if (slotItem == null)
          inventory.setItem(slot, gameCage.getItemStack());
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
          Player player = gamePlayer.getPlayer();
          ArenaSpawn gameSpawn = gamePlayer.getGameSpawn();
          String cageName = itemMeta.getDisplayName();
          gamePlayer.setSelectedCage(cageName);
          player.closeInventory();
          player.sendMessage(
              ChatColor.translateAlternateColorCodes('&', "&aSeleccionaste la jaula &b" + cageName + "&a!"));
          if (gameSpawn != null)
            gameSpawn.createCage(this.cageManager.getCage(cageName));
        }
      }
    }
  }

  public ItemStack getOpenItem() {
    return this.openItem;
  }

  public String getTitle() {
    return this.title;
  }

  public MenuType getType() {
    return MenuType.CAGE;
  }
}
