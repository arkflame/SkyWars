package dev._2lstudios.skywars.tasks;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.Plugin;

public class CopyMapTask implements Runnable {
  private final Plugin plugin;
  
  private final Runnable callback;
  
  private final String worldName;
  
  public CopyMapTask(Plugin plugin, Runnable callback, String worldName) {
    this.plugin = plugin;
    this.callback = callback;
    this.worldName = worldName;
  }
  
  private void deleteDir(File file) {
    File[] contents = file.listFiles();
    if (contents != null)
      for (File f : contents)
        deleteDir(f);  
    if (file.exists())
      file.delete(); 
  }
  
  public void run() {
    try {
      File arenaWorldFolder = new File(this.worldName);
      File arenaMapFolder = new File(this.plugin.getDataFolder() + "/maps/worlds/" + this.worldName);
      if (arenaWorldFolder.exists())
        deleteDir(arenaWorldFolder); 
      if (arenaMapFolder.exists()) {
        arenaWorldFolder.mkdirs();
        FileUtils.copyDirectory(arenaMapFolder, arenaWorldFolder);
      } 
    } catch (IOException e) {
      e.printStackTrace();
    } 
    this.callback.run();
  }
}
