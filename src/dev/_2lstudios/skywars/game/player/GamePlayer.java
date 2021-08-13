package dev._2lstudios.skywars.game.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.arena.ArenaSpawn;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.arena.Arena;
import dev._2lstudios.skywars.menus.MenuManager;
import dev._2lstudios.skywars.menus.MenuType;

public class GamePlayer {
  private final Player player;

  private GamePlayerMode mode = null;
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
    return mode;
  }

  public void setPlayerMode(GamePlayerMode playerMode) {
    this.mode = playerMode;
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

  private void setArena(final Arena arena) {
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

  public void updateArena(final Arena newArena, final GamePlayerMode newMode) {
    if (this.arena != null) {
      if (this.arena.getPlayers().getPlayers().size() > 1) {
        if (arena.getState() == GameState.PLAYING && this.mode == GamePlayerMode.PLAYER && !player.isDead()
            && player.getHealth() > 0) {
          player.setHealth(0);
        } else {
          if (arena.getState() == GameState.WAITING) {
            final GamePlayerParty party = getParty();

            if (party != null && party.getOwner() == this) {
              party.updateArena(newArena, newMode);
            }

            arena.sendMessage(ChatColor.GRAY + player.getDisplayName() + ChatColor.YELLOW + " salio de la partida ("
                + ChatColor.AQUA + (arena.getPlayers().getPlayers().size() - 1) + ChatColor.YELLOW + "/"
                + ChatColor.AQUA + arena.getSpawns().size() + ChatColor.YELLOW + ")!");
          }
        }
      }

      this.arena.getPlayers().remove(this);
      this.arena.removeChestVote(getUUID());
      this.arena.removeTimeVote(getUUID());
    }

    if (newArena != null) {
      if (newMode == GamePlayerMode.SPECTATOR) {
        if (player.getWorld() != newArena.getArenaWorld() || player.getLastDamageCause() == null
            || player.getLastDamageCause().getCause() == DamageCause.VOID) {
          player.teleport(newArena.getArenaWorld().getSpectatorSpawn(this));
        }

        clear(GameMode.ADVENTURE);
        update();
        giveItems(0);
        newArena.getPlayers().add(this);

        setArena(newArena);
        this.mode = newMode;
      } else {
        if (newArena.getState() == GameState.WAITING) {
          final ArenaSpawn arenaSpawn = newArena.getArenaWorld().getFirstSpawn();

          if (arenaSpawn != null) {
            setGameSpawn(arenaSpawn);
            arenaSpawn.createCage(SkyWars.getSkyWarsManager().getCageManager().getCage(getSelectedCage()));
            player.teleport(arenaSpawn.getLocation());
            newArena.getPlayers().add(this);
            clear(GameMode.ADVENTURE);
            update();
            giveItems(1);
            setArena(newArena);
            this.mode = newMode;

            newArena.sendMessage(ChatColor.GRAY + player.getDisplayName() + ChatColor.YELLOW + " entro a la partida ("
                + ChatColor.AQUA + newArena.getPlayers().getPlayers().size() + ChatColor.YELLOW + "/" + ChatColor.AQUA
                + newArena.getSpawns().size() + ChatColor.YELLOW + ")!");
          } else {
            player.sendMessage(ChatColor.RED + "La arena esta llena!");
          }
        } else {
          player.sendMessage(ChatColor.RED + "La arena esta en juego!");
        }
      }
    } else {
      player.teleport(SkyWars.getSpawn());
      setArena(newArena);
      this.mode = null;
      clear(GameMode.ADVENTURE);
      update();
      giveItems(0);
    }
  }

  public boolean isSpectating() {
    return mode == GamePlayerMode.SPECTATOR;
  }

  public GamePlayerParty createParty() {
    return setParty(new GamePlayerParty(this));
  }

  public String getName() {
    return player.getName();
  }
}
