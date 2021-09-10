package dev._2lstudios.skywars.menus;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import dev._2lstudios.skywars.SkyWarsManager;
import dev._2lstudios.skywars.game.GameMenu;

public class MenuManager {
  private Map<MenuType, GameMenu> gMenusByType = new EnumMap<>(MenuType.class);

  public void addMenuNew(GameMenu gameMenu) {
    this.gMenusByType.put(gameMenu.getType(), gameMenu);
  }

  public void setup(final SkyWarsManager skyWarsManager) {
    addMenuNew(new KitMenu(skyWarsManager));
    addMenuNew(new CageMenu(skyWarsManager));
    addMenuNew(new ChestMenu(skyWarsManager));
    addMenuNew(new TimeMenu(skyWarsManager));
    addMenuNew(new VoteMenu(skyWarsManager));
    addMenuNew(new ShopMenu(skyWarsManager));
    addMenuNew(new MapMenu(skyWarsManager));
    addMenuNew(new SpectatorMenu(skyWarsManager));
  }

  public Collection<GameMenu> getGameMenus() {
    return this.gMenusByType.values();
  }

  public GameMenu getMenu(MenuType menuType) {
    return this.gMenusByType.getOrDefault(menuType, null);
  }
}
