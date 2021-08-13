package dev._2lstudios.skywars.game.arena;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.utils.WorldUtil;

public class ArenaManager {
  private final Map<String, Arena> arenasMap = new HashMap<>();
  
  public synchronized Collection<Arena> getGameArenasAsSet() {
    return this.arenasMap.values();
  }
  
  public Arena getMaxPlayerAvailableArena() {
    Collection<Arena> arenas = getGameArenasAsList();
    Arena selectedGameArena = null;
    int lastArenaPlayers = -1;
    for (Arena arena : arenas) {
      int arenaPlayers = arena.getPlayers().getPlayers().size();
      if (arenaPlayers > lastArenaPlayers && arenaPlayers < arena.getSpawns().size() && arena
        .getState() == GameState.WAITING) {
        selectedGameArena = arena;
        lastArenaPlayers = arenaPlayers;
      } 
    } 
    return selectedGameArena;
  }
  
  public Collection<Arena> getGameArenasAsList() {
    List<Arena> values = new ArrayList<>(this.arenasMap.values());
    Collections.shuffle(values);
    return values;
  }
  
  public Arena getArena(String arena) {
    if (arena != null)
      return this.arenasMap.getOrDefault(arena, null); 
    return null;
  }
  
  public void addGameArena(Runnable callback, String arenaName) {
    if (!this.arenasMap.containsKey(arenaName)) {
      Arena arena = new Arena(arenaName);
      this.arenasMap.put(arenaName, arena);
      arena.load(() -> arena.resetArena(callback));
    } 
  }
  
  public void removeGameArena(String arenaName) {
    if (this.arenasMap.containsKey(arenaName)) {
      File dataFolder = SkyWars.getInstance().getDataFolder();
      WorldUtil worldUtil = SkyWars.getWorldUtil();
      Arena arena = this.arenasMap.get(arenaName);
      
      arena.removePlayers();
      arena.removeSpectators();
      this.arenasMap.remove(arenaName);
      worldUtil.kickPlayers(arena.getWorld(), SkyWars.getSpawn());
      worldUtil.unload(arena.getWorld());
      worldUtil.delete(arena.getWorld());
      FileUtils.deleteQuietly(new File(dataFolder + "/maps/worlds/" + arenaName));
      FileUtils.deleteQuietly(new File(dataFolder + "/maps/data/" + arenaName + ".yml"));
    } 
  }
}
