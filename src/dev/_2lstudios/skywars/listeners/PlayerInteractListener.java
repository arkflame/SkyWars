package dev._2lstudios.skywars.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.game.arena.ArenaManager;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;
import dev._2lstudios.skywars.game.player.GamePlayerMode;
import dev._2lstudios.skywars.menus.MenuManager;

public class PlayerInteractListener implements Listener {
  private final MenuManager menuManager;
  private final ArenaManager arenaManager;
  private final GamePlayerManager playerManager;

  public PlayerInteractListener(final MenuManager menuManager, final ArenaManager arenaManager, final GamePlayerManager playerManager) {
    this.menuManager = menuManager;
    this.arenaManager = arenaManager;
    this.playerManager = playerManager;
  }

  @EventHandler
  public void onPlayerInteract(final PlayerInteractEvent event) {
    final Action action = event.getAction();
    if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK) {
      final Player player = event.getPlayer();
      final GamePlayer gamePlayer = this.playerManager.getPlayer(player);

      if (gamePlayer == null) {
        event.setCancelled(true);
      } else {
        if (gamePlayer.hasInteractCooldown()) {
          return;
        }
  
        gamePlayer.updateInteractCooldown();

        final ItemStack itemStack = event.getItem();
        if (gamePlayer.isSpectating()) {
          final Block block = event.getClickedBlock();
          if (block != null) {
            final Material blockType = block.getType();
            final String typeString = blockType.toString();
            if (typeString.endsWith("CHEST") || typeString.endsWith("DOOR") || typeString.endsWith("GATE")
                || typeString.endsWith("PLATE") || typeString.endsWith("LEVER") || typeString.endsWith("BUTTON")
                || typeString.endsWith("STRING"))
              event.setCancelled(true);
          }
        }
        if (itemStack != null && itemStack.hasItemMeta()) {
          final ItemMeta itemMeta = itemStack.getItemMeta();
          if (itemMeta != null && itemMeta.hasDisplayName()) {
            final String displayName = itemMeta.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
              if (itemStack.isSimilar(SkyWars.getRandomMapItem())) {
                final Arena arena = this.arenaManager.getMaxPlayerAvailableArena();
                if (arena != null) {
                  player.sendMessage(ChatColor.GREEN + "Entrando a una partida al azar!");

                  gamePlayer.updateArena(arena, GamePlayerMode.PLAYER);
                } else {
                  player.sendMessage(ChatColor.RED + "No se encontro una arena disponible!");
                }
              } else if (itemStack.isSimilar(SkyWars.getLeaveItem())) {
                final Arena arena = gamePlayer.getArena();
                if (arena != null) {
                  gamePlayer.updateArena(null, null);
                }
              } else {
                for (final GameMenu gameMenu : this.menuManager.getGameMenus()) {
                  if (gameMenu != null && itemStack.isSimilar(gameMenu.getOpenItem())) {
                    gameMenu.getInventory(gamePlayer);
                    event.setCancelled(true);
                    break;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
