package dev._2lstudios.skywars.game;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.arena.GameArena;
import dev._2lstudios.skywars.game.arena.ArenaSpawn;
import dev._2lstudios.skywars.managers.PlayerManager;
import dev._2lstudios.skywars.menus.MenuManager;
import dev._2lstudios.skywars.menus.MenuType;

public class GamePlayer {
  private final Player player;
  private GameParty party = null;
  private ArenaSpawn spawn = null;
  private GameArena arena = null;
  private GameArena lastArena = null;
  private String selectedKit = null;
  private String selectedCage = null;
  private int wins;

  public GamePlayer(Player player) {
    this.player = player;
  }

  public UUID getUUID() {
    return this.player.getUniqueId();
  }

  public void sendMessage(String message) {
    this.player.sendMessage(message);
  }

  public String getDisplayName() {
    return this.player.getDisplayName();
  }

  public Player getPlayer() {
    return this.player;
  }

  public GameArena getArenaName() {
    return this.arena;
  }

  public GameArena getLastArenaName() {
    return this.lastArena;
  }

  public GameArena getArena() {
    return arena;
  }

  public GameArena getLastArena() {
    return lastArena;
  }

  public void setArena(final GameArena arena) {
    this.arena = arena;
  }

  public String getSelectedKit() {
    return this.selectedKit;
  }

  public void setSelectedKit(String selectedKit) {
    this.selectedKit = selectedKit;
  }

  public String getSelectedCage() {
    return this.selectedCage;
  }

  public void setSelectedCage(String cage) {
    this.selectedCage = cage;
  }

  public boolean isSpectating() {
    if (arena != null)
      return arena.getPlayers().getSpectators().contains(this);
    return false;
  }

  public ArenaSpawn getGameSpawn() {
    return this.spawn;
  }

  public void setGameSpawn(ArenaSpawn gameSpawn) {
    if (gameSpawn == null && this.spawn != null) {
      this.spawn.setPlayer(null);
    } else if (gameSpawn != null) {
      gameSpawn.setPlayer(this);
    }
    this.spawn = gameSpawn;
  }

  public int getWins() {
    return this.wins;
  }

  public void addWin() {
    this.wins++;
  }

  public GameParty getParty() {
    return this.party;
  }

  public void setParty(GameParty gameParty) {
    if (this.party != null)
      this.party.remove(this);
    if (gameParty != null)
      gameParty.add(this);
    this.party = gameParty;
  }

  public void clear(GameMode gameMode, boolean collides, boolean flight, boolean updateVanish) {
    if (this.player != null) {
      boolean spectating = isSpectating();
      if (this.player.getOpenInventory() != null)
        this.player.closeInventory();
      if (this.player.getGameMode() != gameMode)
        this.player.setGameMode(gameMode);
      if (updateVanish) {
        PlayerManager playerManager = SkyWars.getMainManager().getPlayerManager();
        for (Player player1 : Bukkit.getServer().getOnlinePlayers()) {
          GamePlayer gamePlayer1 = playerManager.getPlayer(player1);
          if (gamePlayer1 == null)
            continue;
          GameArena arena1 = gamePlayer1.getArena();
          boolean spectating1 = gamePlayer1.isSpectating();
          if (this.arena != null && arena1 != null && arena1 == arena && spectating1 != spectating) {
            if (spectating) {
              player1.hidePlayer(this.player);
              this.player.showPlayer(player1);
              continue;
            }
            if (spectating1) {
              this.player.hidePlayer(player1);
              player1.showPlayer(this.player);
            }
            continue;
          }
          this.player.showPlayer(player1);
          player1.showPlayer(this.player);
        }
      }
      PlayerInventory playerInventory = this.player.getInventory();
      for (PotionEffect potionEffect : this.player.getActivePotionEffects())
        this.player.removePotionEffect(potionEffect.getType());
      if (this.arena != null && spectating)
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 10));
      playerInventory.clear();
      playerInventory.setHelmet(null);
      playerInventory.setChestplate(null);
      playerInventory.setLeggings(null);
      playerInventory.setBoots(null);
      this.player.setLevel(0);
      this.player.setExp(0.0F);
      this.player.setHealth(this.player.getMaxHealth());
      this.player.setFoodLevel(20);
      this.player.setAllowFlight(flight);
      this.player.setFlying(flight);
      this.player.spigot().setCollidesWithEntities(collides);
    }
  }

  public void giveItems(final int type) {
    final Inventory inventory = player.getInventory();
    final MenuManager menuManager = SkyWars.getMainManager().getMenuManager();

    if (type == 0) {
      inventory.setItem(0, menuManager.getMenu(MenuType.MAP).getOpenItem());
      inventory.setItem(1, SkyWars.getRandomMapItem());
      inventory.setItem(4, menuManager.getMenu(MenuType.SHOP).getOpenItem());
      inventory.setItem(8, menuManager.getMenu(MenuType.SPECTATOR).getOpenItem());
    } else if (type == 1) {
      inventory.setItem(0, menuManager.getMenu(MenuType.SHOP).getOpenItem());
      inventory.setItem(1, menuManager.getMenu(MenuType.VOTE).getOpenItem());
      inventory.setItem(8, SkyWars.getLeaveItem());
    }
  }
}
