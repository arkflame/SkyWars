package dev._2lstudios.skywars.tasks;

import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import dev._2lstudios.skywars.generators.VoidGenerator;
import dev._2lstudios.skywars.utils.BukkitUtil;

public class GenerateMapTask implements Runnable {
  private final Plugin plugin;
  
  private final Runnable callback;
  
  private final String worldName;
  
  private final AtomicReference<World> atomicWorld;
  
  public GenerateMapTask(Plugin plugin, Runnable callback, String worldName, AtomicReference<World> atomicWorld) {
    this.plugin = plugin;
    this.callback = callback;
    this.worldName = worldName;
    this.atomicWorld = atomicWorld;
  }
  
  public void run() {
    BukkitUtil.runSync(this.plugin, () -> {
          World world = (new WorldCreator(this.worldName)).generateStructures(false).generator((ChunkGenerator)new VoidGenerator()).createWorld();
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
          world.setSpawnLocation(0, 50, 0);
          this.atomicWorld.set(world);
          this.callback.run();
        });
  }
}
