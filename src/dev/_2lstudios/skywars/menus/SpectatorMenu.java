package dev._2lstudios.skywars.menus;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.inventoryapi.InventoryAPI;
import dev._2lstudios.inventoryapi.events.InventoryAPIClickEvent;
import dev._2lstudios.inventoryapi.inventory.InventoryUtil;
import dev._2lstudios.inventoryapi.inventory.InventoryWrapper;
import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.SkyWarsManager;
import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.arena.ArenaManager;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;
import dev._2lstudios.skywars.game.player.GamePlayerMode;

public class SpectatorMenu implements GameMenu, Listener {
  private static final String ID = "sw_specmenu";
  private static final String TITLE = ChatColor.DARK_GRAY + "SkyWars - Espectador";

  private final ArenaManager arenaManager;
  private final GamePlayerManager playerManager;
  private final InventoryUtil inventoryUtil;

  private final ItemStack openItem = new ItemStack(Material.COMPASS);

  SpectatorMenu(final SkyWarsManager skyWarsManager) {
    final Plugin plugin = SkyWars.getInstance();

    this.arenaManager = skyWarsManager.getArenaManager();
    this.playerManager = skyWarsManager.getPlayerManager();
    this.inventoryUtil = InventoryAPI.getInstance().getInventoryUtil();
    final ItemMeta openItemMeta = this.openItem.getItemMeta();
    openItemMeta.setDisplayName(ChatColor.YELLOW + "Menu de Espectador");
    this.openItem.setItemMeta(openItemMeta);

    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  private void setFireworkColor(final Arena arena, final FireworkEffectMeta fireworkEffectMeta, final Color color,
      final ChatColor chatColor) {
    final String arenaName = arena.getName();
    final FireworkEffect fireworkEffect = FireworkEffect.builder().withColor(color).build();
    fireworkEffectMeta.setDisplayName(chatColor + arenaName.substring(0, 1).toUpperCase() + arenaName.substring(1));
    fireworkEffectMeta.setEffect(fireworkEffect);
    fireworkEffectMeta.setLore(Arrays.asList(new String[] {
        ChatColor.GRAY + "Solo Normal", "", ChatColor.GRAY + "Jugadores: " + ChatColor.GREEN
            + arena.getPlayers().getPlayers().size() + "/" + arena.getSpawns().size(),
        "", ChatColor.GREEN + "Click para unirte!" }));
  }

  private Collection<ItemStack> generateItems(final Collection<Arena> arenas) {
    final Collection<ItemStack> items = new HashSet<>();

    for (final Arena arena : this.arenaManager.getGameArenasAsSet()) {
      final ItemStack itemStack = new ItemStack(Material.FIREWORK_CHARGE, 0);
      final FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta) itemStack.getItemMeta();
      final Collection<GamePlayer> players = arena.getPlayers().getPlayers();
      final int size = players.size();

      itemStack.setAmount(size > 0 ? size : 1);

      if (arena.getState() == GameState.WAITING && !players.isEmpty()) {
        setFireworkColor(arena, fireworkEffectMeta, Color.YELLOW, ChatColor.GREEN);
      } else if (arena.getState() == GameState.WAITING && arena.getSpawns().size() > 1) {
        setFireworkColor(arena, fireworkEffectMeta, Color.LIME, ChatColor.GREEN);
      } else {
        setFireworkColor(arena, fireworkEffectMeta, Color.RED, ChatColor.RED);
      }

      itemStack.setItemMeta((ItemMeta) fireworkEffectMeta);
      items.add(itemStack);
    }

    return items;
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
    return MenuType.SPECTATOR;
  }

  @Override
  public void runAction(int paramInt, ItemStack itemStack, GamePlayer gamePlayer) {
    // TODO Fix how menus are structured
  }

  public Inventory getInventory(GamePlayer gamePlayer, final int page) {
    final Collection<Arena> arenas = this.arenaManager.getGameArenasAsSet();
    final Collection<ItemStack> arenaItems = generateItems(arenas);
    
    InventoryAPI.getInstance().getInventoryUtil()
        .createDisplayInventory(TITLE, gamePlayer.getPlayer(), page, ID, arenaItems);

    return null;
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
            final Arena arena1 = this.arenaManager
                .getArena(ChatColor.stripColor(item.getItemMeta().getDisplayName().toLowerCase()));

            if (arena1 != null) {
              player.closeInventory();
              gamePlayer.updateArena(arena1, GamePlayerMode.SPECTATOR);
            }
          }
        }
      }
    }
  }
}
