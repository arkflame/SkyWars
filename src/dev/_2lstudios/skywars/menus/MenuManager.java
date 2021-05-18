package dev._2lstudios.skywars.menus;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.managers.MainManager;

public class MenuManager {
  private Map<String, GameMenu> gMenusByTitle = new HashMap<>();
  
  private Map<MenuType, GameMenu> gMenusByType = new EnumMap<>(MenuType.class);
  
  public void addMenu(GameMenu gameMenu) {
    this.gMenusByType.put(gameMenu.getType(), gameMenu);
    this.gMenusByTitle.put(gameMenu.getTitle(), gameMenu);
  }
  
  public MenuManager(MainManager mainManager) {
    addMenu(new CageMenu(mainManager.getCageManager()));
    addMenu(new ChestMenu(mainManager.getChestManager()));
    addMenu(new TimeMenu(mainManager.getTimeManager()));
    addMenu(new KitMenu(mainManager.getKitManager()));
    addMenu(new MapMenu(mainManager.getArenaManager()));
    addMenu(new SpectatorMenu(mainManager.getArenaManager()));
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
