package dev._2lstudios.skywars.menus;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import dev._2lstudios.skywars.SkyWarsManager;
import dev._2lstudios.skywars.game.GameMenu;

public class MenuManager {
  private Map<String, GameMenu> gMenusByTitle = new HashMap<>();
  
  private Map<MenuType, GameMenu> gMenusByType = new EnumMap<>(MenuType.class);
  
  public void addMenu(GameMenu gameMenu) {
    this.gMenusByType.put(gameMenu.getType(), gameMenu);
    this.gMenusByTitle.put(gameMenu.getTitle(), gameMenu);
  }
  
  public MenuManager(SkyWarsManager skyWarsManager) {
    addMenu(new CageMenu(skyWarsManager.getCageManager()));
    addMenu(new ChestMenu(skyWarsManager.getChestManager()));
    addMenu(new TimeMenu(skyWarsManager.getTimeManager()));
    addMenu(new KitMenu(skyWarsManager.getKitManager()));
    addMenu(new MapMenu(skyWarsManager.getArenaManager()));
    addMenu(new SpectatorMenu(skyWarsManager.getArenaManager()));
    addMenu(new VoteMenu(this));
    addMenu(new ShopMenu(this));
  }
  
  public Collection<GameMenu> getGameMenus() {
    return this.gMenusByType.values();
  }
  
  public GameMenu getMenu(MenuType menuType) {
    return this.gMenusByType.getOrDefault(menuType, null);
  }
  
  public GameMenu getMenu(String title) {
    return this.gMenusByTitle.getOrDefault(title, null);
  }
}
