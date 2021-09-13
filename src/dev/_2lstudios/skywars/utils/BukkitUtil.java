package dev._2lstudios.skywars.utils;

import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.skywars.SkyWars;

public class BukkitUtil {
  public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    player.setTitleTimes(fadeIn, stay, fadeOut);
    player.sendTitle(title, subtitle);
  }

  public static void sendSound(Player player, String soundName, int i, int i1) {
    try {
      Sound sound = Sound.valueOf(soundName);
      player.playSound(player.getLocation(), sound, i, i1);
    } catch (Exception exception) {
    }
  }

  public static ItemStack createItem(final Material material, final String displayName, final int amount, final short damage) {
    final ItemStack item = new ItemStack(material, amount, damage);
    final ItemMeta itemMeta = item.getItemMeta();

    itemMeta.setDisplayName(displayName);
    item.setItemMeta(itemMeta);

    return item;
  }

  public static ItemStack createItem(final Material material, final String displayName, final int amount) {
    return createItem(material, displayName, 1, (short) 0);
  }

  public static ItemStack createItem(final Material material, final String displayName) {
    return createItem(material, displayName, 1);
  }

  public static void runSync(Runnable runnable) {
    Plugin plugin = SkyWars.getInstance();
    Server server = plugin.getServer();

    if (plugin.isEnabled() && !server.isPrimaryThread()) {
      Objects.requireNonNull(runnable);
      server.getScheduler().runTask(plugin, runnable::run);
    } else {
      runnable.run();
    }
  }

  public static void runAsync(Runnable runnable) {
    Plugin plugin = SkyWars.getInstance();
    Server server = plugin.getServer();

    if (plugin.isEnabled() && server.isPrimaryThread()) {
      server.getScheduler().runTaskAsynchronously(plugin, runnable::run);
    } else {
      runnable.run();
    }
  }
}
