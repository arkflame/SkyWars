package dev._2lstudios.skywars.game;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GameCage {
  private final String name;
  
  private final Material primaryMaterial;
  
  private final Material secondaryMaterial;
  
  private final byte data;
  
  private final ItemStack itemStack;
  
  public GameCage(String name, Material primaryMaterial, Material secondaryMaterial, byte data) {
    this.name = name;
    this.primaryMaterial = primaryMaterial;
    this.secondaryMaterial = secondaryMaterial;
    this.data = data;
    this.itemStack = new ItemStack(primaryMaterial, 1);
    if (this.itemStack.getType() != Material.AIR) {
      ItemMeta itemMeta = this.itemStack.getItemMeta();
      if (itemMeta != null) {
        itemMeta.setDisplayName(name);
        this.itemStack.setItemMeta(itemMeta);
        this.itemStack.setDurability((short)data);
      } 
    } 
  }
  
  public String getName() {
    return this.name;
  }
  
  public Material getPrimaryMaterial() {
    return this.primaryMaterial;
  }
  
  public Material getSecondaryMaterial() {
    return this.secondaryMaterial;
  }
  
  public byte getData() {
    return this.data;
  }
  
  public ItemStack getItemStack() {
    return this.itemStack;
  }
}
