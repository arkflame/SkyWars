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

  public PlayerInteractListener(MenuManager menuManager, ArenaManager arenaManager, GamePlayerManager playerManager) {
    this.menuManager = menuManager;
    this.arenaManager = arenaManager;
    this.playerManager = playerManager;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Action action = event.getAction();
    if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK) {
      Player player = event.getPlayer();
      GamePlayer gamePlayer = this.playerManager.getPlayer(player);
      if (gamePlayer == null) {
        event.setCancelled(true);
      } else {
        ItemStack itemStack = event.getItem();
        if (gamePlayer.isSpectating()) {
          Block block = event.getClickedBlock();
          if (block != null) {
            Material blockType = block.getType();
            String typeString = blockType.toString();
            if (typeString.endsWith("CHEST") || typeString.endsWith("DOOR") || typeString.endsWith("GATE")
                || typeString.endsWith("PLATE") || typeString.endsWith("LEVER") || typeString.endsWith("BUTTON")
                || typeString.endsWith("STRING"))
              event.setCancelled(true);
          }
        }
        if (itemStack != null && itemStack.hasItemMeta()) {
          ItemMeta itemMeta = itemStack.getItemMeta();
          if (itemMeta != null && itemMeta.hasDisplayName()) {
            String displayName = itemMeta.getDisplayName();
            if (displayName != null && !displayName.isEmpty())
              if (itemStack.isSimilar(SkyWars.getRandomMapItem())) {
                Arena arena = this.arenaManager.getMaxPlayerAvailableArena();
                if (arena != null) {
                  player.sendMessage(ChatColor.GREEN + "Entrando a una partida al azar!");

                  gamePlayer.updateArena(arena, GamePlayerMode.PLAYER);
                } else {
                  player.sendMessage(ChatColor.RED + "No se encontro una arena disponible!");
                }
              } else if (itemStack.isSimilar(SkyWars.getLeaveItem())) {
                Arena arena = gamePlayer.getArena();
                if (arena != null) {
                  gamePlayer.updateArena(null, null);
                }
              } else {
                for (GameMenu gameMenu : this.menuManager.getGameMenus()) {
                  if (itemStack.isSimilar(gameMenu.getOpenItem())) {
                    player.openInventory(gameMenu.getInventory(gamePlayer));
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
