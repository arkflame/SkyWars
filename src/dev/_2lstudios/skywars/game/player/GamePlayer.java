package dev._2lstudios.skywars.game.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.arena.ArenaSpawn;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.menus.MenuManager;
import dev._2lstudios.skywars.menus.MenuType;

public class GamePlayer {
  private final Player player;

  private GamePlayerMode playerMode = null;
  private GamePlayerParty party = null;
  private ArenaSpawn spawn = null;
  private Arena arena = null;
  private Arena lastArena = null;

  private String selectedKit = null;
  private String selectedCage = null;

  private int wins;

  GamePlayer(Player player) {
    this.player = player;
  }

  public GamePlayerMode getPlayerMode() {
    return playerMode;
  }

  public void setPlayerMode(GamePlayerMode playerMode) {
    this.playerMode = playerMode;
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

  public Arena getArenaName() {
    return this.arena;
  }

  public Arena getLastArenaName() {
    return this.lastArena;
  }

  public Arena getArena() {
    return arena;
  }

  public Arena getLastArena() {
    return lastArena;
  }

  public void setArena(final Arena arena) {
    this.lastArena = this.arena;
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

  public GamePlayerParty getParty() {
    return this.party;
  }

  public GamePlayerParty setParty(GamePlayerParty gameParty) {
    if (this.party != null) {
      this.party.remove(this);
    }

    if (gameParty != null) {
      gameParty.add(this);
    }

    return this.party = gameParty;
  }

  public void clear(GameMode gameMode) {
    if (this.player.getOpenInventory() != null) {
      this.player.closeInventory();
    }

    if (this.player.getGameMode() != gameMode) {
      this.player.setGameMode(gameMode);
    }

    PlayerInventory playerInventory = this.player.getInventory();

    for (PotionEffect potionEffect : this.player.getActivePotionEffects()) {
      this.player.removePotionEffect(potionEffect.getType());
    }

    playerInventory.clear();
    playerInventory.setHelmet(null);
    playerInventory.setChestplate(null);
    playerInventory.setLeggings(null);
    playerInventory.setBoots(null);
    this.player.setLevel(0);
    this.player.setExp(0.0F);
    this.player.setHealth(this.player.getMaxHealth());
    this.player.setFoodLevel(20);
  }

  public void update() {
    boolean spectating = isSpectating();

    player.setAllowFlight(spectating);
    player.setFlying(spectating);
    player.spigot().setCollidesWithEntities(!spectating);

    if (arena != null && spectating) {
      for (GamePlayer gamePlayer1 : arena.getPlayers().getPlayers()) {
        Player player1 = gamePlayer1.getPlayer();

        player1.hidePlayer(player);
      }
    } else {
      for (final Player player1 : Bukkit.getOnlinePlayers()) {
        player1.showPlayer(player);
      }
    }
  }

  public void giveItems(final int type) {
    final Inventory inventory = player.getInventory();
    final MenuManager menuManager = SkyWars.getSkyWarsManager().getMenuManager();

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

  public boolean isSpectating() {
    return playerMode == GamePlayerMode.SPECTATOR;
  }

  public GamePlayerParty createParty() {
    return setParty(new GamePlayerParty(this));
  }
}
