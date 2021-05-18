package dev._2lstudios.skywars.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.GameKit;

public class KitManager {
  private final Map<String, GameKit> kitsMap = new HashMap<>();
  
  public KitManager() {
    for (String kitName : SkyWars.getConfigurationUtil().getConfiguration("%datafolder%/kits.yml").getStringList("kits"))
      addKit(kitName); 
  }
  
  public void addKit(String kitName) {
    if (!this.kitsMap.containsKey(kitName))
      this.kitsMap.put(kitName, new GameKit(kitName)); 
  }
  
  public GameKit getKit(String kitName) {
    return this.kitsMap.getOrDefault(kitName, null);
  }
  
  public synchronized Collection<GameKit> getKits() {
    return this.kitsMap.values();
  }
}
