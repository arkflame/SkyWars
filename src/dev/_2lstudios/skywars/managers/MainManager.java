package dev._2lstudios.skywars.managers;

import dev._2lstudios.skywars.chest.ChestManager;
import dev._2lstudios.skywars.menus.MenuManager;
import dev._2lstudios.skywars.time.TimeManager;

public class MainManager {
  private final CageManager cageManager = new CageManager();
  
  private final ChestManager chestManager = new ChestManager();
  
  private final KitManager kitManager = new KitManager();
  
  private final TimeManager timeManager = new TimeManager();
  
  private final PlayerManager playerManager = new PlayerManager();
  
  private final ArenaManager arenaManager = new ArenaManager();
  
  private final MenuManager menuManager = new MenuManager(this);
  
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
  
  public PlayerManager getPlayerManager() {
    return this.playerManager;
  }
}
