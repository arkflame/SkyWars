package dev._2lstudios.skywars.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import dev._2lstudios.skywars.SkyWars;

public class GameKit {
  private final String name;
  private final ItemStack icon = new ItemStack(Material.STONE);
  private final Collection<ItemStack> items = new ArrayList<>();
  private Collection<String> lore = new ArrayList<>();
  
  public GameKit(String name) {
    this.name = name;
    reload();
  }
  
  private void reload() {
    YamlConfiguration kitsYml = SkyWars.getConfigurationUtil().getConfiguration("%datafolder%/kits.yml");
    if (Material.getMaterial(kitsYml.getString(this.name + ".icon")) != null)
      this.icon.setType(Material.getMaterial(kitsYml.getString(this.name + ".icon"))); 
    this.lore.clear();
    for (String line : kitsYml.getStringList(this.name + ".lore")) {
      line = ChatColor.translateAlternateColorCodes('&', line.replace("%price%", "GRATIS"));
      this.lore.add(line);
    } 
    for (String itemDataString : kitsYml.getStringList(this.name + ".items")) {
      String[] itemDataArray = itemDataString.split(", ");
      Material material = Material.STONE;
      int data = 0;
      int amount = 1;
      List<Enchantment> enchantments = new ArrayList<>();
      List<Integer> levels = new ArrayList<>();
      ItemStack itemStack = new ItemStack(material, amount);
      int index;
      for (index = 0; index < itemDataArray.length; index++) {
        if (index == 0) {
          if (itemDataArray[index].contains(":")) {
            String[] itemMaterialArray = itemDataArray[index].split(":");
            material = Material.getMaterial(itemMaterialArray[0].toUpperCase());
            data = Integer.parseInt(itemMaterialArray[1]);
          } else {
            material = Material.getMaterial(itemDataArray[index].toUpperCase());
            if (material == null)
              SkyWars.getPlugin().getLogger().info(itemDataArray[index] + " from kits.yml cannot be loaded!"); 
          } 
        } else if (index == 1) {
          amount = Integer.parseInt(itemDataArray[index]);
        } else if (itemDataArray[index].contains(":")) {
          String[] itemEnchantmentArray = itemDataArray[index].split(":");
          enchantments.add(Enchantment.getByName(itemEnchantmentArray[0].toUpperCase()));
          levels.add(Integer.valueOf(itemEnchantmentArray[1]));
        } 
      } 
      if (material != null)
        itemStack.setType(material); 
      itemStack.setAmount(amount);
      itemStack.setDurability((short)data);
      for (index = 0; index < enchantments.size(); index++) {
        if (enchantments.get(index) != null && levels.get(index) != null)
          itemStack.addEnchantment(enchantments.get(index), ((Integer)levels.get(index)).intValue()); 
      } 
      this.items.add(itemStack);
    } 
  }
  
  public String getName() {
    return this.name;
  }
  
  public Collection<String> getLore() {
    return this.lore;
  }
  
  public ItemStack getIcon() {
    return this.icon;
  }
  
  public void giveItems(PlayerInventory inventory) {
    for (ItemStack itemStack : this.items) {
      inventory.addItem(new ItemStack[] { itemStack });
    } 
  }
}
