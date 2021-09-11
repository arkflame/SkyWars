package dev._2lstudios.skywars.menus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev._2lstudios.inventoryapi.InventoryAPI;
import dev._2lstudios.inventoryapi.events.InventoryAPIClickEvent;
import dev._2lstudios.inventoryapi.inventory.InventoryUtil;
import dev._2lstudios.skywars.SkyWarsManager;
import dev._2lstudios.skywars.chest.ChestManager;
import dev._2lstudios.skywars.chest.ChestType;
import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;

public class ChestMenu implements GameMenu {
  private static final String ID = "sw_chestmenu";
  private static final String TITLE = "SkyWars - Cofres";

  private final ItemStack openItem = new ItemStack(Material.CHEST);
  private final ChestManager chestManager;
  private final GamePlayerManager playerManager;
  private final MenuManager menuManager;
  private final InventoryUtil inventoryUtil;

  ChestMenu(final SkyWarsManager skyWarsManager) {
    this.chestManager = skyWarsManager.getChestManager();
    this.playerManager = skyWarsManager.getPlayerManager();
    this.menuManager = skyWarsManager.getMenuManager();
    this.inventoryUtil = InventoryAPI.getInstance().getInventoryUtil();
    final ItemMeta openItemMeta = this.openItem.getItemMeta();
    openItemMeta.setDisplayName(ChatColor.YELLOW + "Cofres");
    openItem.setItemMeta(openItemMeta);
  }

  public Inventory getInventory(final GamePlayer gamePlayer, final int page) {
    final Inventory inventory = inventoryUtil.createInventory(TITLE, gamePlayer.getPlayer(), page, ID).getInventory();

    inventory.setItem(10, chestManager.getOpenItem(ChestType.BASIC));
    inventory.setItem(13, chestManager.getOpenItem(ChestType.NORMAL));
    inventory.setItem(16, chestManager.getOpenItem(ChestType.INSANE));

    return null;
  }

  @Override
  public Inventory getInventory(GamePlayer gamePlayer) {
    getInventory(gamePlayer, 1);

    return null;
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryAPIClick(final InventoryAPIClickEvent event) {
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
              if (player.hasPermission("skywars.votechest")) {
                if (item.isSimilar(this.chestManager.getOpenItem(ChestType.BASIC))) {
                  arena.addChestVote(gamePlayer, ChestType.BASIC);
                } else if (item.isSimilar(this.chestManager.getOpenItem(ChestType.NORMAL))) {
                  arena.addChestVote(gamePlayer, ChestType.NORMAL);
                } else if (item.isSimilar(this.chestManager.getOpenItem(ChestType.INSANE))) {
                  arena.addChestVote(gamePlayer, ChestType.INSANE);
                }
              } else {
                player.sendMessage(
                    ChatColor.translateAlternateColorCodes('&', "&cNecesitas rango &lTITAN&c para votar cofres!"));
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
    return MenuType.CHEST;
  }
}
