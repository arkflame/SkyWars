package dev._2lstudios.skywars.game;

import org.bukkit.inventory.ItemStack;

public class GameItem {
  private final int minChance;
  
  private final int maxChance;
  
  private final ItemStack itemStack;
  
  public GameItem(int minChance, int maxChance, ItemStack itemStack) {
    this.minChance = minChance;
    this.maxChance = maxChance;
    this.itemStack = itemStack;
  }
  
  public int getMinChance() {
    return this.minChance;
  }
  
  public int getMaxChance() {
    return this.maxChance;
  }
  
  public ItemStack getItemStack() {
    return this.itemStack;
  }
}
