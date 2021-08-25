package dev._2lstudios.skywars.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import dev._2lstudios.skywars.game.GameCage;

public class CageManager {
  private final static GameCage AIR_CAGE = new GameCage("AIR", Material.AIR, Material.AIR, (byte) 0);
  private final static GameCage WHITE_CAGE = new GameCage(ChatColor.GREEN + "Blanco", Material.STAINED_GLASS,
      Material.STAINED_CLAY, (byte) 0);

  private final Map<String, GameCage> gameCages = new HashMap<>();

  public CageManager() {
    createCage(WHITE_CAGE);
    createCage(ChatColor.GREEN + "Naranja", Material.STAINED_GLASS, Material.STAINED_CLAY, 1);
    createCage(ChatColor.GREEN + "Magenta", Material.STAINED_GLASS, Material.STAINED_CLAY, 2);
    createCage(ChatColor.GREEN + "Celeste", Material.STAINED_GLASS, Material.STAINED_CLAY, 3);
    createCage(ChatColor.GREEN + "Amarillo", Material.STAINED_GLASS, Material.STAINED_CLAY, 4);
    createCage(ChatColor.GREEN + "Lima", Material.STAINED_GLASS, Material.STAINED_CLAY, 5);
    createCage(ChatColor.GREEN + "Rosa", Material.STAINED_GLASS, Material.STAINED_CLAY, 6);
    createCage(ChatColor.GREEN + "Gris", Material.STAINED_GLASS, Material.STAINED_CLAY, 7);
    createCage(ChatColor.GREEN + "Gris Claro", Material.STAINED_GLASS, Material.STAINED_CLAY, 8);
    createCage(ChatColor.GREEN + "Cian", Material.STAINED_GLASS, Material.STAINED_CLAY, 9);
    createCage(ChatColor.GREEN + "Purpura", Material.STAINED_GLASS, Material.STAINED_CLAY, 10);
    createCage(ChatColor.GREEN + "Azul", Material.STAINED_GLASS, Material.STAINED_CLAY, 11);
    createCage(ChatColor.GREEN + "Marron", Material.STAINED_GLASS, Material.STAINED_CLAY, 12);
    createCage(ChatColor.GREEN + "Verde", Material.STAINED_GLASS, Material.STAINED_CLAY, 13);
    createCage(ChatColor.GREEN + "Rojo", Material.STAINED_GLASS, Material.STAINED_CLAY, 14);
    createCage(ChatColor.GREEN + "Negro", Material.STAINED_GLASS, Material.STAINED_CLAY, 15);
  }

  public void createCage(final GameCage gameCage) {
    this.gameCages.put(ChatColor.stripColor(gameCage.getDisplayName()), gameCage);
  }

  public void createCage(String name, Material primaryMaterial, Material secondaryMaterial, int data) {
    createCage(new GameCage(name, primaryMaterial, secondaryMaterial, (byte) data));
  }

  public Collection<GameCage> getCages() {
    return this.gameCages.values();
  }

  public GameCage getDefaultCage() {
    return WHITE_CAGE;
  }

  public GameCage getCage(String name) {
    return this.gameCages.getOrDefault(name, getDefaultCage());
  }

  public GameCage getAirCage() {
    return AIR_CAGE;
  }
}
