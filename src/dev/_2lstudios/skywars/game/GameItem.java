package dev._2lstudios.skywars.game;

import org.bukkit.inventory.ItemStack;

public class GameItem {
  private int minChance;
  
  private int maxChance;
  
  private ItemStack itemStack;
  
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
