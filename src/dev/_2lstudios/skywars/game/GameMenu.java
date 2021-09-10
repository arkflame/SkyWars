package dev._2lstudios.skywars.game;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.menus.MenuType;

public interface GameMenu {
  Inventory getInventory(GamePlayer paramGamePlayer);

  String getTitle();

  ItemStack getOpenItem();

  MenuType getType();
}
