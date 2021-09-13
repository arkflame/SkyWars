package dev._2lstudios.skywars.menus;

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
import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.SkyWarsManager;
import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;
import dev._2lstudios.skywars.time.TimeManager;
import dev._2lstudios.skywars.time.TimeType;
import dev._2lstudios.skywars.utils.BukkitUtil;

public class TimeMenu implements GameMenu, Listener {
  private static final String ID = "sw_timemenu";
  private static final String TITLE = "SkyWars - Tiempo";

  private final ItemStack openItem = BukkitUtil.createItem(Material.WATCH, ChatColor.YELLOW + "Tiempo");
  private final TimeManager timeManager;
  private final GamePlayerManager playerManager;
  private final MenuManager menuManager;
  private final InventoryUtil inventoryUtil;

  TimeMenu(final SkyWarsManager skyWarsManager) {
    this.timeManager = skyWarsManager.getTimeManager();
    this.playerManager = skyWarsManager.getPlayerManager();
    this.menuManager = skyWarsManager.getMenuManager();
    this.inventoryUtil = InventoryAPI.getInstance().getInventoryUtil();

    final Plugin plugin = SkyWars.getInstance();
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  public Inventory getInventory(GamePlayer gamePlayer, final int page) {
    final Inventory inventory = inventoryUtil.createInventory(TITLE, gamePlayer.getPlayer(), page, ID).getInventory();

    inventory.setItem(10, timeManager.getOpenItem(TimeType.MORNING));
    inventory.setItem(12, timeManager.getOpenItem(TimeType.DAY));
    inventory.setItem(14, timeManager.getOpenItem(TimeType.NOON));
    inventory.setItem(16, timeManager.getOpenItem(TimeType.NIGHT));
    inventory.setItem(49, inventoryUtil.getCloseItem());

    return null;
  }

  @Override
  public Inventory getInventory(GamePlayer gamePlayer) {
    getInventory(gamePlayer, 1);

    return null;
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryAPIClick(final InventoryAPIClickEvent event) {
    if (!event.getInventoryWrapper().getId().equals(ID)) {
      return;
    }
    
    final Player player = event.getPlayer();
    final GamePlayer gamePlayer = playerManager.getPlayer(player);
    final ItemStack item = event.getEvent().getCurrentItem();

    if (item != null) {
      final ItemMeta itemMeta = item.getItemMeta();
      if (itemMeta != null) {
        final String displayName = itemMeta.getDisplayName();
        if (displayName != null) {

          if (item.isSimilar(inventoryUtil.getCloseItem())) {
            menuManager.getMenu(MenuType.VOTE).getInventory(gamePlayer);
          } else {
            final Arena arena = gamePlayer.getArena();
            if (arena != null) {
              if (player.hasPermission("skywars.votetime")) {
                for (final TimeType timeType : TimeType.values()) {
                  if (this.timeManager.isOpenItem(timeType, item)) {
                    arena.addTimeVote(player.getUniqueId(), timeType);
                    break;
                  }
                }
              } else {
                player.sendMessage(
                    ChatColor.translateAlternateColorCodes('&', "&cNecesitas rango &d&lMEGA&c para votar cofres!"));
              }
              player.closeInventory();
            }
          }
        }
      }
    }
  }

  public String getTitle() {
    return TITLE;
  }

  public ItemStack getOpenItem() {
    return this.openItem;
  }

  public MenuType getType() {
    return MenuType.TIME;
  }
}
