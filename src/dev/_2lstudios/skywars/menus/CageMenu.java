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
import org.bukkit.plugin.Plugin;

import dev._2lstudios.inventoryapi.InventoryAPI;
import dev._2lstudios.inventoryapi.events.InventoryAPIClickEvent;
import dev._2lstudios.inventoryapi.inventory.InventoryUtil;
import dev._2lstudios.inventoryapi.inventory.InventoryWrapper;
import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.SkyWarsManager;
import dev._2lstudios.skywars.game.GameCage;
import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.arena.ArenaSpawn;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;
import dev._2lstudios.skywars.managers.CageManager;

public class CageMenu implements GameMenu, Listener {
  private static final String ID = "sw_cagemenu";
  private static final String TITLE = "SkyWars - Jaulas";

  private final InventoryUtil inventoryUtil;
  private final CageManager cageManager;
  private final GamePlayerManager playerManager;
  private final ItemStack openItem = new ItemStack(Material.STAINED_GLASS);

  public CageMenu(final SkyWarsManager skyWarsManager) {
    this.inventoryUtil = InventoryAPI.getInstance().getInventoryUtil();
    this.cageManager = skyWarsManager.getCageManager();
    this.playerManager = skyWarsManager.getPlayerManager();
    final ItemMeta openItemMeta = this.openItem.getItemMeta();
    openItemMeta.setDisplayName(ChatColor.YELLOW + "Menu de Jaulas");
    openItem.setItemMeta(openItemMeta);

    final Plugin plugin = SkyWars.getInstance();
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  private Collection<ItemStack> generateItems(final Collection<GameCage> gameCages) {
    final Collection<ItemStack> items = new HashSet<>();

    for (final GameCage gameCage : gameCages) {
      final ItemStack itemStack = new ItemStack(gameCage.getPrimaryMaterial(), 1, gameCage.getData());
      final ItemMeta itemMeta = itemStack.getItemMeta();
      final String displayName = gameCage.getDisplayName();
      final List<String> lore = new ArrayList<>();

      // TODO: Check if player bought cage
      final String option = ChatColor.GREEN + "Click para seleccionar!";

      lore.add(option);
      itemMeta.setDisplayName(displayName);
      itemMeta.setLore(lore);
      itemStack.setItemMeta(itemMeta);
      items.add(itemStack);
    }

    return items;
  }

  public Inventory getInventory(final GamePlayer gamePlayer, final int page) {
    final Collection<GameCage> cages = this.cageManager.getCages();
    final Collection<ItemStack> items = generateItems(cages);

    inventoryUtil.createDisplayInventory(TITLE, gamePlayer.getPlayer(), page, ID, items);

    return null;
  }

  @Override
  public Inventory getInventory(final GamePlayer gamePlayer) {
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
            final ArenaSpawn gameSpawn = gamePlayer.getGameSpawn();
            final String cageName = ChatColor.stripColor(item.getItemMeta().getDisplayName().toLowerCase());
            gamePlayer.setSelectedCage(cageName);
            player.closeInventory();
            player.sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&aSeleccionaste la jaula &b" + cageName + "&a!"));
            if (gameSpawn != null) {
              gameSpawn.createCage(this.cageManager.getCage(cageName));
            }
          }
        }
      }
    }
  }

  public ItemStack getOpenItem() {
    return this.openItem;
  }

  public String getTitle() {
    return TITLE;
  }

  public MenuType getType() {
    return MenuType.CAGE;
  }
}
