package dev._2lstudios.skywars;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import dev._2lstudios.skywars.commands.LeaveCommand;
import dev._2lstudios.skywars.commands.PartyCommand;
import dev._2lstudios.skywars.commands.SkyWarsCommand;
import dev._2lstudios.skywars.game.GameScoreboard;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.arena.ArenaManager;
import dev._2lstudios.skywars.game.player.GamePlayerManager;
import dev._2lstudios.skywars.listeners.BlockBreakListener;
import dev._2lstudios.skywars.listeners.BlockPlaceListener;
import dev._2lstudios.skywars.listeners.EntityDamageByEntityListener;
import dev._2lstudios.skywars.listeners.EntityDamageListener;
import dev._2lstudios.skywars.listeners.InventoryClickListener;
import dev._2lstudios.skywars.listeners.InventoryDragListener;
import dev._2lstudios.skywars.listeners.PlayerDeathListener;
import dev._2lstudios.skywars.listeners.PlayerDropItemListener;
import dev._2lstudios.skywars.listeners.PlayerInteractListener;
import dev._2lstudios.skywars.listeners.PlayerJoinListener;
import dev._2lstudios.skywars.listeners.PlayerQuitListener;
import dev._2lstudios.skywars.listeners.PlayerRespawnListener;
import dev._2lstudios.skywars.listeners.WorldUnloadListener;
import dev._2lstudios.skywars.menus.MenuManager;
import dev._2lstudios.skywars.utils.ConfigurationUtil;
import dev._2lstudios.skywars.utils.WorldUtil;

public class SkyWars extends JavaPlugin {
  private static SkyWars instance;
  private static SkyWarsManager skyWarsManager;
  private static ConfigurationUtil configurationUtil;
  private static WorldUtil worldUtil;
  private static final ItemStack randomMapItem = new ItemStack(Material.ARROW, 1);
  private static final ItemStack leaveItem = new ItemStack(Material.REDSTONE, 1);

  public static Location getSpawn() {
    return Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
  }

  public static SkyWars getInstance() {
    return instance;
  }

  public static SkyWarsManager getSkyWarsManager() {
    return skyWarsManager;
  }

  public static ConfigurationUtil getConfigurationUtil() {
    return configurationUtil;
  }

  public static WorldUtil getWorldUtil() {
    return worldUtil;
  }

  public static ItemStack getRandomMapItem() {
    return randomMapItem;
  }

  public static ItemStack getLeaveItem() {
    return leaveItem;
  }

  @Override
  public void onEnable() {
    instance = this;
    worldUtil = new WorldUtil(this);
    configurationUtil = new ConfigurationUtil(this);
    skyWarsManager = new SkyWarsManager();
    ArenaManager arenaManager = skyWarsManager.getArenaManager();
    MenuManager menuManager = skyWarsManager.getMenuManager();
    GamePlayerManager playerManager = skyWarsManager.getPlayerManager();
    Server server = getServer();
    BukkitScheduler bukkitScheduler = server.getScheduler();
    PluginManager pluginManager = server.getPluginManager();
    ItemMeta randomMapItemMeta = randomMapItem.getItemMeta();
    ItemMeta leaveItemMeta = leaveItem.getItemMeta();
    for (Player player : server.getOnlinePlayers())
      playerManager.addGamePlayer(player);
    randomMapItemMeta.setDisplayName(ChatColor.YELLOW + "Mapa Aleatorio");
    randomMapItem.setItemMeta(randomMapItemMeta);
    leaveItemMeta.setDisplayName(ChatColor.RED + "Salir");
    leaveItem.setItemMeta(leaveItemMeta);
    configurationUtil.createConfiguration("%datafolder%/basic.yml");
    configurationUtil.createConfiguration("%datafolder%/normal.yml");
    configurationUtil.createConfiguration("%datafolder%/insane.yml");
    File mapsDataFolder = new File(getDataFolder() + "/maps/data/");
    if (mapsDataFolder.exists()) {
      byte b;
      int i;
      File[] arrayOfFile;
      for (i = (arrayOfFile = mapsDataFolder.listFiles()).length, b = 0; b < i;) {
        File dataFile = arrayOfFile[b];
        arenaManager.addGameArena(null, dataFile.getName().replace(".yml", ""));
        b++;
      }
    }

    pluginManager.registerEvents(new BlockBreakListener(playerManager), this);
    pluginManager.registerEvents(new BlockPlaceListener(playerManager), this);
    pluginManager.registerEvents(new EntityDamageByEntityListener(playerManager), this);
    pluginManager.registerEvents(new EntityDamageListener(playerManager), this);
    pluginManager.registerEvents(new InventoryClickListener(playerManager, menuManager), this);
    pluginManager.registerEvents(new InventoryDragListener(menuManager, playerManager), this);
    pluginManager.registerEvents(new PlayerDeathListener(playerManager), this);
    pluginManager.registerEvents(new PlayerDropItemListener(playerManager), this);
    pluginManager.registerEvents(new PlayerInteractListener(menuManager, arenaManager, playerManager), this);
    pluginManager.registerEvents(new PlayerJoinListener(playerManager), this);
    pluginManager.registerEvents(new PlayerQuitListener(playerManager), this);
    pluginManager.registerEvents(new PlayerRespawnListener(), this);
    pluginManager.registerEvents(new WorldUnloadListener(), this);

    getCommand("skywars").setExecutor(new SkyWarsCommand(server, arenaManager, playerManager));
    getCommand("leave").setExecutor(new LeaveCommand(playerManager));
    getCommand("party").setExecutor(new PartyCommand(server, playerManager));

    new GameScoreboard(instance, configurationUtil, playerManager);

    bukkitScheduler.runTaskTimer(this, () -> {
      for (Arena arena : arenaManager.getGameArenasAsSet()) {
        arena.tickArena();
      }
    }, 20L, 20L);
  }

  @Override
  public void onDisable() {
    if (skyWarsManager != null) {
      for (Arena arena : skyWarsManager.getArenaManager().getGameArenasAsSet()) {
        worldUtil.kickPlayers(arena.getWorld(), SkyWars.getSpawn());
        worldUtil.unload(arena.getWorld());
        worldUtil.delete(arena.getWorld());
      }
    }
  }
}
