package dev._2lstudios.skywars.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.GameArena;
import dev._2lstudios.skywars.utils.WorldUtil;

public class ArenaManager {
  private final Map<String, GameArena> gameArenasMap = new HashMap<>();
  
  public synchronized Collection<GameArena> getGameArenasAsSet() {
    return this.gameArenasMap.values();
  }
  
  public GameArena getMaxPlayerAvailableArena() {
    Collection<GameArena> gameArenas = getGameArenasAsList();
    GameArena selectedGameArena = null;
    int lastArenaPlayers = -1;
    for (GameArena gameArena : gameArenas) {
      int gameArenaPlayers = gameArena.getPlayers().size();
      if (gameArenaPlayers > lastArenaPlayers && gameArenaPlayers < gameArena.getSpawns().size() && gameArena
        .getState() == GameState.WAITING) {
        selectedGameArena = gameArena;
        lastArenaPlayers = gameArenaPlayers;
      } 
    } 
    return selectedGameArena;
  }
  
  public Collection<GameArena> getGameArenasAsList() {
    List<GameArena> values = new ArrayList<>(this.gameArenasMap.values());
    Collections.shuffle(values);
    return values;
  }
  
  public GameArena getArena(String arena) {
    if (arena != null)
      return this.gameArenasMap.getOrDefault(arena, null); 
    return null;
  }
  
  public void addGameArena(Runnable callback, String arenaName) {
    if (!this.gameArenasMap.containsKey(arenaName)) {
      GameArena gameArena = new GameArena(arenaName);
      this.gameArenasMap.put(arenaName, gameArena);
      gameArena.load(() -> gameArena.resetArena(callback));
    } 
  }
  
  public void removeGameArena(String arenaName) {
    if (this.gameArenasMap.containsKey(arenaName)) {
      WorldUtil worldUtil = SkyWars.getWorldUtil();
      Server server = Bukkit.getServer();
      GameArena gameArena = this.gameArenasMap.get(arenaName);
      gameArena.removePlayers();
      gameArena.removeSpectators();
      this.gameArenasMap.remove(arenaName);
      worldUtil.delete(() -> {
            try {
              File dataFolder = SkyWars.getPlugin().getDataFolder();
              FileUtils.deleteDirectory(new File(dataFolder + "/maps/worlds/" + arenaName));
              (new File(dataFolder + "/maps/data/" + arenaName + ".yml")).delete();
            } catch (IOException e) {
              e.printStackTrace();
            } 
          },gameArena.getWorld(), server.getWorlds().get(0).getSpawnLocation());
    } 
  }
}
