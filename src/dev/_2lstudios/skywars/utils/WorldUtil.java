package dev._2lstudios.skywars.utils;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.skywars.tasks.CopyMapTask;
import dev._2lstudios.skywars.tasks.GenerateMapTask;

public class WorldUtil {
  private final Plugin plugin;
  
  public WorldUtil(Plugin plugin) {
    this.plugin = plugin;
  }
  
  public void create(Runnable callback, String worldName, AtomicReference<World> atomicWorld) {
    World serverWorld = this.plugin.getServer().getWorld(worldName);
    if (serverWorld == null) {
      BukkitUtil.runAsync(this.plugin, () -> {
            CopyMapTask copyMapTask = new CopyMapTask(this.plugin, (Runnable)new GenerateMapTask(this.plugin, callback, worldName, atomicWorld), worldName);
            copyMapTask.run();
          });
    } else {
      atomicWorld.set(serverWorld);
      if (callback != null)
        callback.run(); 
      this.plugin.getLogger().info("Created world " + worldName + "!");
    } 
  }
  
  public void delete(Runnable callback, World world, Location fallback) {
    if (world == null) {
      if (callback != null)
        callback.run(); 
      return;
    } 
    BukkitUtil.runSync(this.plugin, () -> {
          String worldName = world.getName();
          Collection<Player> worldPlayers = world.getPlayers();
          for (Player player : worldPlayers) {
            if (player.isDead())
              player.spigot().respawn(); 
            player.teleport(fallback);
          } 
          Bukkit.unloadWorld(world, false);
          BukkitUtil.runAsync(this.plugin, callback);
        });
  }
  
  public void save(Runnable callback, World world) {
    BukkitUtil.runSync(this.plugin, () -> {
          world.save();
          BukkitUtil.runAsync(this.plugin, callback);
        });
  }
}
