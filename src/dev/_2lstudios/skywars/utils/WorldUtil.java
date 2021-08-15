package dev._2lstudios.skywars.utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.generators.VoidGenerator;

public class WorldUtil {
  private final Plugin plugin;

  public WorldUtil(Plugin plugin) {
    this.plugin = plugin;
  }

  /*
   * Copies: Map folder -> World folder
   */
  public void copyMapWorld(Plugin plugin, String mapName) {
    try {
      File arenaWorldFolder = new File(mapName);
      File arenaMapFolder = new File(this.plugin.getDataFolder() + "/maps/worlds/" + mapName);

      FileUtils.deleteQuietly(arenaWorldFolder);
      FileUtils.copyDirectory(arenaMapFolder, arenaWorldFolder);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
   * Copies: World folder -> Map folder
   */
  public void copyWorldMap(World world) {
    if (world == null) {
      return;
    }

    final String dataFolder = SkyWars.getInstance().getDataFolder().getPath();
    final File worldFolder = world.getWorldFolder();
    final File mapFolder = new File(dataFolder + "/maps/worlds/" + world.getName() + "/");

    try {
      FileUtils.deleteDirectory(mapFolder);
      FileUtils.copyDirectory(worldFolder, mapFolder);
      FileUtils.deleteDirectory(new File(worldFolder.getAbsolutePath() + "/data"));
      FileUtils.deleteDirectory(new File(worldFolder.getAbsolutePath() + "/playerdata"));
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  /*
   * Creates/Loads an empty world
   */
  public World create(final String worldName) {
    World world = new WorldCreator(worldName).generateStructures(false).generator(new VoidGenerator()).createWorld();
    WorldBorder worldBorder = world.getWorldBorder();

    worldBorder.setCenter(0.0D, 0.0D);
    worldBorder.setSize(300.0D);
    worldBorder.setDamageAmount(2.0D);
    worldBorder.setDamageBuffer(0.0D);
    world.setThundering(false);
    world.setStorm(false);
    world.setTime(6000L);
    world.setGameRuleValue("doMobSpawning", "false");
    world.setGameRuleValue("mobGriefing", "false");
    world.setGameRuleValue("doFireTick", "false");
    world.setGameRuleValue("showDeathMessages", "false");
    world.setGameRuleValue("doDaylightCycle", "false");
    world.setGameRuleValue("sendCommandFeedback", "false");
    world.setSpawnLocation(0, 63, 0);

    return world;
  }

  /*
   * Kicks players from a world
   */
  public void kickPlayers(final World world, Location fallback) {
    if (world == null) {
      return;
    }

    Collection<Player> worldPlayers = world.getPlayers();

    for (Player player : worldPlayers) {
      if (player.isDead()) {
        player.spigot().respawn();
      }

      player.teleport(fallback);
    }
  }

  /*
   * Unloads a currently loaded world
   */
  public void unload(World world) {
    if (world == null) {
      return;
    }

    Bukkit.unloadWorld(world, true);
  }

  /*
   * Deletes a world folder
   */
  public void delete(World world) {
    if (world == null) {
      return;
    }

    FileUtils.deleteQuietly(world.getWorldFolder());
  }
}
