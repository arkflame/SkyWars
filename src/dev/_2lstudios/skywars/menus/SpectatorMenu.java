package dev._2lstudios.skywars.menus;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.ArenaManager;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerMode;

public class SpectatorMenu implements GameMenu {
  private static final int INVENTORY_SIZE = 54;
  
  private final String title = ChatColor.DARK_GRAY + "SkyWars - Espectador";
  
  private final ArenaManager arenaManager;
  
  private final Inventory inventory = Bukkit.createInventory(null, INVENTORY_SIZE, this.title);
  
  private final ItemStack openItem = new ItemStack(Material.COMPASS);
  
  private final ItemStack itemStack = new ItemStack(Material.FIREWORK_CHARGE, 0);
  
  private final int[] slots = new int[28];
  
  private double lastTimeMillis = 0.0D;
  
  public SpectatorMenu(ArenaManager arenaManager) {
    this.arenaManager = arenaManager;
    ItemMeta openItemMeta = this.openItem.getItemMeta();
    openItemMeta.setDisplayName(ChatColor.YELLOW + "Menu de Espectador");
    this.openItem.setItemMeta(openItemMeta);
    int index = 0;
    int slotCounter = 0;
    for (int slot = 10; slot < INVENTORY_SIZE && 
      this.slots.length > index; slot++) {
      if (slotCounter++ < 7) {
        this.slots[index++] = slot;
      } else if (slotCounter > 8) {
        slotCounter = 0;
      } 
    } 
    updateInventory();
  }
  
  private void updateInventory() {
    double currentTimeMillis = System.currentTimeMillis();
    if (currentTimeMillis - this.lastTimeMillis > 2000.0D) {
      int index = 0;
      for (Arena arena : this.arenaManager.getGameArenasAsSet()) {
        FireworkEffect fireworkEffect;
        Collection<GamePlayer> players = arena.getPlayers().getPlayers();
        int size = players.size();
        if (size == 0)
          size = 1; 
        this.itemStack.setAmount(size);
        FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta)this.itemStack.getItemMeta();
        String arenaName = arena.getName();
        if (arena.getState() == GameState.WAITING && players.size() > 0) {
          fireworkEffect = FireworkEffect.builder().withColor(Color.YELLOW).build();
          fireworkEffectMeta.setDisplayName(ChatColor.GREEN + arenaName
              .substring(0, 1).toUpperCase() + arenaName.substring(1));
        } else if (arena.getState() == GameState.WAITING && arena.getSpawns().size() > 1) {
          fireworkEffect = FireworkEffect.builder().withColor(Color.LIME).build();
          fireworkEffectMeta.setDisplayName(ChatColor.GREEN + arenaName
              .substring(0, 1).toUpperCase() + arenaName.substring(1));
        } else {
          fireworkEffect = FireworkEffect.builder().withColor(Color.RED).build();
          fireworkEffectMeta.setDisplayName(ChatColor.RED + arenaName
              .substring(0, 1).toUpperCase() + arenaName.substring(1));
        } 
        fireworkEffectMeta.setEffect(fireworkEffect);
        fireworkEffectMeta.setLore(Arrays.asList(new String[] { ChatColor.GRAY + "Solo Normal", "", ChatColor.GRAY + "Jugadores: " + ChatColor.GREEN + arena
                
                .getPlayers().getPlayers().size() + "/" + arena.getSpawns().size(), "", ChatColor.GREEN + "Click para unirte!" }));
        this.itemStack.setItemMeta((ItemMeta)fireworkEffectMeta);
        if (index < this.slots.length) {
          int slot = this.slots[index];
          ItemStack slotItem = this.inventory.getItem(slot);
          if (slotItem == null || !slotItem.isSimilar(this.itemStack))
            this.inventory.setItem(slot, this.itemStack); 
          index++;
        } 
      } 
      this.lastTimeMillis = currentTimeMillis;
    } 
  }
  
  public Inventory getInventory(GamePlayer gamePlayer) {
    updateInventory();
    return this.inventory;
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
          Arena arena1 = this.arenaManager.getArena(ChatColor.stripColor(itemStack.getItemMeta().getDisplayName().toLowerCase()));
          if (arena1 != null) {
            gamePlayer.updateArena(arena1, GamePlayerMode.SPECTATOR);
            gamePlayer.getPlayer().closeInventory();
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
    return MenuType.SPECTATOR;
  }
}
