package dev._2lstudios.skywars.tasks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import dev._2lstudios.skywars.game.GameCage;

public class CreateCageTask implements Runnable {
  private final GameCage gameCage;
  private final Location location;
  
  public CreateCageTask(GameCage gameCage, Location location) {
    this.gameCage = gameCage;
    this.location = location;
  }
  
  private void setType(Block block, Material material, byte data) {
    if (block.getType() != material)
      block.setType(material); 
    if (block.getData() != data)
      block.setData(data); 
  }
  
  public void run() {
    Material primaryMaterial = this.gameCage.getPrimaryMaterial();
    Material secondaryMaterial = this.gameCage.getSecondaryMaterial();
    byte data = this.gameCage.getData();
    try {
      for (int y = 0; y < 4; y++) {
        Material material;
        if (y > 0)
          this.location.add(0.0D, 1.0D, 0.0D); 
        Block block = this.location.getBlock();
        if (y == 0 || y == 3) {
          material = secondaryMaterial;
          setType(block, material, data);
        } else {
          material = primaryMaterial;
        } 
        this.location.add(1.0D, 0.0D, 0.0D);
        block = this.location.getBlock();
        setType(block, material, data);
        this.location.add(-2.0D, 0.0D, 0.0D);
        block = this.location.getBlock();
        setType(block, material, data);
        this.location.add(1.0D, 0.0D, 1.0D);
        block = this.location.getBlock();
        setType(block, material, data);
        this.location.add(0.0D, 0.0D, -2.0D);
        block = this.location.getBlock();
        setType(block, material, data);
        this.location.add(0.0D, 0.0D, 1.0D);
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
}
