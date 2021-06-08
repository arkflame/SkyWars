package dev._2lstudios.skywars.time;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TimeManager {
  private final Map<TimeType, ItemStack> openItems = new HashMap<>();
  
  public TimeManager() {
    reloadItems();
  }
  
  public void reloadItems() {
    ItemStack morningOpenItem = new ItemStack(Material.STAINED_CLAY, 1, (short)6);
    ItemStack dayOpenItem = new ItemStack(Material.STAINED_CLAY, 1, (short)4);
    ItemStack noonOpenItem = new ItemStack(Material.STAINED_CLAY, 1, (short)1);
    ItemStack nightOpenItem = new ItemStack(Material.STAINED_CLAY, 1, (short)11);
    ItemMeta morningOpenItemMeta = morningOpenItem.getItemMeta();
    ItemMeta dayOpenItemMeta = dayOpenItem.getItemMeta();
    ItemMeta noonOpenItemMeta = noonOpenItem.getItemMeta();
    ItemMeta nightOpenItemMeta = nightOpenItem.getItemMeta();
    morningOpenItemMeta.setDisplayName(ChatColor.RED + "Manana");
    dayOpenItemMeta.setDisplayName(ChatColor.YELLOW + "Dia");
    noonOpenItemMeta.setDisplayName(ChatColor.GOLD + "Tarde");
    nightOpenItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Noche");
    morningOpenItem.setItemMeta(morningOpenItemMeta);
    dayOpenItem.setItemMeta(dayOpenItemMeta);
    noonOpenItem.setItemMeta(noonOpenItemMeta);
    nightOpenItem.setItemMeta(nightOpenItemMeta);
    this.openItems.put(TimeType.MANANA, morningOpenItem);
    this.openItems.put(TimeType.DIA, dayOpenItem);
    this.openItems.put(TimeType.TARDE, noonOpenItem);
    this.openItems.put(TimeType.NOCHE, nightOpenItem);
  }
  
  public ItemStack getOpenItem(TimeType timeType) {
    return this.openItems.getOrDefault(timeType, null);
  }
  
  public boolean isOpenItem(TimeType timeType, ItemStack itemStack) {
    return itemStack.isSimilar(this.openItems.getOrDefault(timeType, null));
  }
}
