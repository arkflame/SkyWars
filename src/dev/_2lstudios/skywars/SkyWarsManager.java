package dev._2lstudios.skywars;

import dev._2lstudios.skywars.chest.ChestManager;
import dev._2lstudios.skywars.game.arena.ArenaManager;
import dev._2lstudios.skywars.game.player.GamePlayerManager;
import dev._2lstudios.skywars.managers.CageManager;
import dev._2lstudios.skywars.managers.KitManager;
import dev._2lstudios.skywars.menus.MenuManager;
import dev._2lstudios.skywars.time.TimeManager;

public class SkyWarsManager {
  private final CageManager cageManager = new CageManager();
  private final ChestManager chestManager = new ChestManager();
  private final KitManager kitManager = new KitManager();
  private final TimeManager timeManager = new TimeManager();
  private final GamePlayerManager playerManager = new GamePlayerManager();
  private final ArenaManager arenaManager = new ArenaManager();
  private final MenuManager menuManager = new MenuManager();
  
  public SkyWarsManager() {
    menuManager.setup(this);
  }

  public ArenaManager getArenaManager() {
    return this.arenaManager;
  }
  
  public CageManager getCageManager() {
    return this.cageManager;
  }
  
  public ChestManager getChestManager() {
    return this.chestManager;
  }
  
  public KitManager getKitManager() {
    return this.kitManager;
  }
  
  public TimeManager getTimeManager() {
    return this.timeManager;
  }
  
  public MenuManager getMenuManager() {
    return this.menuManager;
  }
  
  public GamePlayerManager getPlayerManager() {
    return this.playerManager;
  }
}
