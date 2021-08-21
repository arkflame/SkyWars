package dev._2lstudios.skywars.menus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev._2lstudios.inventoryapi.InventoryAPI;
import dev._2lstudios.inventoryapi.events.InventoryAPIClickEvent;
import dev._2lstudios.inventoryapi.inventory.InventoryUtil;
import dev._2lstudios.inventoryapi.inventory.InventoryWrapper;
import dev._2lstudios.skywars.SkyWarsManager;
import dev._2lstudios.skywars.game.GameKit;
import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;
import dev._2lstudios.skywars.managers.KitManager;

public class KitMenu implements GameMenu, Listener {
  private static final String ID = "sw_kitmenu";
  private static final String TITLE = ChatColor.DARK_GRAY + "SkyWars - Kits";

  private final KitManager kitManager;
  private final GamePlayerManager playerManager;
  private final ItemStack openItem = new ItemStack(Material.STORAGE_MINECART, 1);
  private final InventoryUtil inventoryUtil;

  KitMenu(final SkyWarsManager skyWarsManager) {
    this.kitManager = skyWarsManager.getKitManager();
    this.playerManager = skyWarsManager.getPlayerManager();
    final ItemMeta openItemMeta = this.openItem.getItemMeta();
    openItemMeta.setDisplayName(ChatColor.YELLOW + "Menu de Kits");
    this.openItem.setItemMeta(openItemMeta);
    this.inventoryUtil = InventoryAPI.getInstance().getInventoryUtil();
  }

  private Collection<ItemStack> generateItems(final Collection<GameKit> kits) {
    final Collection<ItemStack> items = new HashSet<>();

    for (final GameKit kit : kits) {
      final ItemStack itemStack = kit.getIcon();
      final ItemMeta itemMeta = itemStack.getItemMeta();
      final String name = kit.getName();
      final List<String> lore = new ArrayList<>();

      // TODO: Check if player bought kit
      final String option = ChatColor.GREEN + "Click para equipar!";

      for (final String line : kit.getLore()) {
        lore.add(line.replace("%option%", option));
      }

      itemMeta.setDisplayName(ChatColor.GREEN + name.substring(0, 1).toUpperCase() + name.substring(1));
      itemMeta.setLore(lore);
      itemStack.setItemMeta(itemMeta);
      items.add(itemStack);
    }

    return items;
  }

  public Inventory getInventory(final GamePlayer gamePlayer, final int page) {
    final Collection<GameKit> kits = this.kitManager.getKits();
    final Collection<ItemStack> items = generateItems(kits);

    InventoryAPI.getInstance().getInventoryUtil().createDisplayInventory(TITLE, gamePlayer.getPlayer(), page, ID,
        items);

    return null;
  }

  @Override
  public void runAction(final int paramInt, final ItemStack itemStack, final GamePlayer gamePlayer) {
    // TODO Fix how menus are structured
  }

  @Override
  public String getTitle() {
    return TITLE;
  }

  @Override
  public ItemStack getOpenItem() {
    return this.openItem;
  }

  @Override
  public MenuType getType() {
    return MenuType.KIT;
  }

  @Override
  public Inventory getInventory(GamePlayer gamePlayer) {
    getInventory(gamePlayer, 1);

    return null;
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryAPIClick(final InventoryAPIClickEvent event) {
    final InventoryWrapper inventoryWrapper = event.getInventoryWrapper();

    if (inventoryWrapper.getId().equals(ID)) {
      final Player player = event.getPlayer();
      final GamePlayer gamePlayer = playerManager.getPlayer(player);
      final ItemStack item = event.getEvent().getCurrentItem();
      final int page = inventoryWrapper.getPage();

      if (item != null) {
        final ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta != null && itemMeta.hasDisplayName()) {
          if (item.isSimilar(inventoryUtil.getBackItem(page))) {
            getInventory(gamePlayer, page - 1);
          } else if (item.isSimilar(inventoryUtil.getNextItem(page))) {
            getInventory(gamePlayer, page + 1);
          } else if (item.isSimilar(inventoryUtil.getCloseItem())) {
            player.closeInventory();
          } else {
            String displayName = itemMeta.getDisplayName();
            String kitName = ChatColor.stripColor(displayName.toLowerCase());

            gamePlayer.setSelectedKit(kitName);
            player.sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&aSeleccionaste el kit &b" + displayName + "&a!"));
            player.closeInventory();
          }
        }
      }
    }
  }
}
