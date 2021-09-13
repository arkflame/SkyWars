package dev._2lstudios.skywars.time;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev._2lstudios.skywars.utils.BukkitUtil;

public class TimeManager {
  private final Map<TimeType, ItemStack> openItems = new HashMap<>();
  
  public TimeManager() {
    reloadItems();
  }
  
  public void reloadItems() {
    ItemStack morningOpenItem = BukkitUtil.createItem(Material.STAINED_CLAY, ChatColor.RED + TimeType.MORNING.getName(), 1, (short) 6);
    ItemStack dayOpenItem = BukkitUtil.createItem(Material.STAINED_CLAY, ChatColor.RED + TimeType.MORNING.getName(), 1, (short) 4);
    ItemStack noonOpenItem = BukkitUtil.createItem(Material.STAINED_CLAY, ChatColor.RED + TimeType.MORNING.getName(), 1, (short) 1);
    ItemStack nightOpenItem = BukkitUtil.createItem(Material.STAINED_CLAY, ChatColor.RED + TimeType.MORNING.getName(), 1, (short) 11);

    this.openItems.put(TimeType.MORNING, morningOpenItem);
    this.openItems.put(TimeType.DAY, dayOpenItem);
    this.openItems.put(TimeType.NOON, noonOpenItem);
    this.openItems.put(TimeType.NIGHT, nightOpenItem);
  }
  
  public ItemStack getOpenItem(TimeType timeType) {
    return this.openItems.getOrDefault(timeType, null);
  }
  
  public boolean isOpenItem(TimeType timeType, ItemStack itemStack) {
    return itemStack.isSimilar(this.openItems.getOrDefault(timeType, null));
  }
}
