package dev._2lstudios.skywars.listeners;

import java.util.HashSet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldUnloadListener implements Listener {
  @EventHandler(ignoreCancelled = true)
  public void onWorldUnload(WorldUnloadEvent event) {
    for (Entity entity : new HashSet<>(event.getWorld().getEntities())) {
      if (entity instanceof LivingEntity && !(entity instanceof Player) && entity.isValid())
        entity.remove(); 
    } 
  }
}
