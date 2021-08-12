package dev._2lstudios.skywars.game.arena;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.chest.ChestManager;
import dev._2lstudios.skywars.chest.ChestType;
import dev._2lstudios.skywars.game.GameItem;
import dev._2lstudios.skywars.game.GameState;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.managers.CageManager;
import dev._2lstudios.skywars.managers.KitManager;
import dev._2lstudios.skywars.time.TimeType;
import dev._2lstudios.skywars.utils.BukkitUtil;
import dev._2lstudios.skywars.utils.WorldUtil;

public class GameArena {
  private static final int START_SECONDS = 20;

  private final ArenaChestVotes chestVotes;
  private final ArenaTimeVotes timeVotes;
  private final ArenaKills arenaKills = new ArenaKills();
  private final ArenaWorld arenaWorld;
  private final ArenaPlayers arenaPlayers;

  private GameState state = GameState.WAITING;

  private final String arenaName;

  private int seconds = 0;

  public GameArena(String arenaName) {
    this.arenaName = arenaName;
    this.chestVotes = new ArenaChestVotes(this);
    this.timeVotes = new ArenaTimeVotes(this);
    this.arenaWorld = new ArenaWorld(this);
    this.arenaPlayers = new ArenaPlayers(Bukkit.getServer(), this);
  }

  public void load(Runnable callback) {
    YamlConfiguration yamlConfiguration = SkyWars.getConfigurationUtil()
        .getConfiguration("%datafolder%/maps/data/" + this.arenaName + ".yml");
    ConfigurationSection configSpawns = yamlConfiguration.getConfigurationSection("spawns");
    ConfigurationSection configChests = yamlConfiguration.getConfigurationSection("chests");

    arenaWorld.clearSpawns();

    if (configSpawns != null)
      for (String key : configSpawns.getKeys(false))
        arenaWorld.addSpawn(configSpawns.getVector(key));

    arenaWorld.clearChests();

    if (configChests != null)
      for (String key : configChests.getKeys(false))
        arenaWorld.addChest(configChests.getVector(key));

    arenaWorld.setSpectatorSpawn(yamlConfiguration.getVector("spectator_spawn"));

    if (callback != null) {
      callback.run();
    }
  }

  public void save(Runnable callback) {
    final World world = arenaWorld.getWorld();

    arenaWorld.setSpectatorSpawn(world);
    arenaWorld.setChestsAndSpawns();

    YamlConfiguration config = new YamlConfiguration();
    int id = 0;

    for (ArenaSpawn spawn : arenaWorld.getSpawns()) {
      id++;
      config.set("spawns." + id, spawn.getVector());
    }

    for (Vector chest : arenaWorld.getChests()) {
      id++;
      config.set("chests." + id, chest);
    }

    config.set("spectator_spawn", arenaWorld.getSpectatorVector());
    SkyWars.getConfigurationUtil().saveConfiguration(config, "%datafolder%/maps/data/" + this.arenaName + ".yml");
    SkyWars.getWorldUtil().save(callback, world);
  }

  public void setWorld(World world) {
    arenaWorld.setWorld(world);
  }

  private TimeType getMostVotedTime() {
    return timeVotes.getMostVotedTime();
  }

  public void addTimeVote(UUID uuid, TimeType timeType) {
    timeVotes.addTimeVote(uuid, timeType);
  }

  public void removeTimeVote(UUID uuid) {
    timeVotes.removeTimeVote(uuid);
  }

  public ChestType getMostVotedChest() {
    return chestVotes.getMostVoted();
  }

  public void addChestVote(GamePlayer gamePlayer, ChestType chestType) {
    chestVotes.addChestVote(gamePlayer, chestType);
  }

  public void removeChestVote(UUID uuid) {
    chestVotes.removeChestVote(uuid);
  }

  public void tickArena() {
    if (this.state == GameState.WAITING) {
      if (arenaPlayers.size() > 1) {
        if (this.seconds < START_SECONDS) {
          final World world = arenaWorld.getWorld();
          int timeLeft = START_SECONDS - this.seconds;
          boolean showTime = !(this.seconds % 5 != 0 && this.seconds <= 15);
          for (Player player : world.getPlayers()) {
            player.setLevel(timeLeft);
            if (showTime) {
              player.sendMessage(ChatColor.YELLOW + "El juego comienza en " + ChatColor.RED + timeLeft
                  + ChatColor.YELLOW + " segundos!");
              BukkitUtil.sendTitle(player, ChatColor.translateAlternateColorCodes('&', "&c&l" + timeLeft), "", 20, 20,
                  20);
            }
          }
          if (showTime)
            sendSound("NOTE_PLING", 2.0F);
          this.seconds++;
        } else {
          if (this.seconds == 20)
            setState(null, GameState.PLAYING);
          this.seconds = 0;
        }
      } else if (this.seconds != 0) {
        this.seconds = 0;
      }
    } else if (this.state == GameState.PLAYING && arenaPlayers.size() < 2) {
      setState(null, GameState.WAITING);
    }
  }

  public void sendMessage(String message) {
    final World world = arenaWorld.getWorld();

    for (Player player : world.getPlayers())
      player.sendMessage(message);
  }

  public void sendSound(String soundString, float pitch) {
    try {
      final World world = arenaWorld.getWorld();
      Sound sound = Sound.valueOf(soundString);
      world.playSound(world.getSpawnLocation(), sound, 300.0F, pitch);
    } catch (Exception ignored) {
      SkyWars.getPlugin().getLogger().info("The sound ".concat(soundString).concat(" wasn't found!"));
    }
  }

  public Collection<Vector> getChests() {
    return arenaWorld.getChests();
  }

  public Collection<ArenaSpawn> getSpawns() {
    return arenaWorld.getSpawns();
  }

  public void addPlayer(final GamePlayer gamePlayer) {
    arenaPlayers.addPlayer(gamePlayer);
  }

  public void addSpectator(final GamePlayer gamePlayer) {
    arenaPlayers.addSpectator(gamePlayer);
  }

  private void populateChests(ChestType chestType) {
    Collection<GameItem> gameItems;
    int chanceIndex;
    ChestManager chestManager = SkyWars.getMainManager().getChestManager();
    if (chestType == ChestType.INSANE) {
      chanceIndex = chestManager.getInsaneChanceIndex();
      gameItems = chestManager.getInsaneGameItems();
    } else if (chestType == ChestType.BASIC) {
      chanceIndex = chestManager.getBasicChanceIndex();
      gameItems = chestManager.getBasicGameItems();
    } else {
      chanceIndex = chestManager.getNormalChanceIndex();
      gameItems = chestManager.getNormalGameItems();
    }
    for (Location location : getChestLocations()) {
      Block block = location.getBlock();
      if (block != null && block.getType() == Material.CHEST) {
        BlockState blockState = block.getState();
        if (blockState instanceof Chest) {
          Chest chest = (Chest) blockState;
          Inventory inventory = chest.getBlockInventory();
          inventory.clear();
          for (int x = 0; x < 9; x++) {
            int slot = (int) (Math.random() * 27.0D);
            int randomIndex = (int) (Math.random() * chanceIndex);
            for (GameItem gameItem : gameItems) {
              if (randomIndex >= gameItem.getMinChance() && randomIndex <= gameItem.getMaxChance()) {
                if (inventory.getItem(slot) == null) {
                  inventory.setItem(slot, gameItem.getItemStack());
                  break;
                }
                inventory.addItem(new ItemStack[] { gameItem.getItemStack() });
                break;
              }
            }
          }
        }
      }
    }
  }

  private Collection<Location> getChestLocations() {
    return arenaWorld.getChestLocations();
  }

  public String getName() {
    return this.arenaName;
  }

  public World getWorld() {
    return arenaWorld.getWorld();
  }

  public GameState getState() {
    return this.state;
  }

  public void setState(Runnable callback, GameState newState) {
    if (this.state == GameState.EDITING && newState != GameState.EDITING) {
      save(() -> resetArena(null));
    } else if (newState == GameState.EDITING && this.state != GameState.EDITING) {
      Server server = Bukkit.getServer();
      removePlayers();
      removeSpectators();
      AtomicReference<World> atomicWorld = new AtomicReference<>(null);
      WorldUtil worldUtil = SkyWars.getWorldUtil();
      worldUtil.delete(() -> worldUtil.create(null, this.arenaName, atomicWorld), arenaWorld.getWorld(),
          server.getWorlds().get(0).getSpawnLocation());
    } else if (newState == GameState.PLAYING) {
      CageManager cageManager = SkyWars.getMainManager().getCageManager();
      KitManager kitManager = SkyWars.getMainManager().getKitManager();
      ChestType chestType = getMostVotedChest();
      TimeType timeType = getMostVotedTime();

      arenaWorld.generateCages(cageManager.getAirCage());
      arenaWorld.setTime(timeType);

      populateChests(chestType);

      final String title = ChatColor.translateAlternateColorCodes('&', "&c&lMODO " + chestType.getName().toUpperCase());
      final String subtitle = ChatColor.translateAlternateColorCodes('&', "&7El team no esta permitido!");

      for (final GamePlayer gamePlayer : arenaPlayers.getPlayers()) {
        Player player = gamePlayer.getPlayer();
        String kitName = gamePlayer.getSelectedKit();
        PlayerInventory inventory = player.getInventory();

        gamePlayer.clear(GameMode.SURVIVAL);

        if (kitName != null) {
          kitManager.getKit(kitName).giveItems(inventory);
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, 10));
        BukkitUtil.sendTitle(player, title, subtitle, 20, 20, 20);
      }

      for (final GamePlayer gamePlayer : arenaPlayers.getSpectators()) {
        final Player player = gamePlayer.getPlayer();

        player.setLevel(0);

        BukkitUtil.sendTitle(player, title, subtitle, 20, 20, 20);
      }

      addChestVote(null, chestType);
      sendSound("ENDERDRAGON_GROWL", 1.0F);
    } else if (newState == GameState.WAITING) {
      final GamePlayer winner = arenaPlayers.getFirstPlayer();

      if (winner != null) {
        Player player = winner.getPlayer();
        
        if (player != null) {
          BukkitUtil.sendTitle(player, ChatColor.translateAlternateColorCodes('&', "&6&lVICTORIA"),
              ChatColor.GRAY + "Has sido el ultimo superviviente", 20, 20, 20);
          BukkitUtil.sendSound(player, "LEVEL_UP", 1, 1);
          winner.addWin();
        }

        final PlayerKills gamePlayerKills1 = arenaKills.getKills(0);
        final PlayerKills gamePlayerKills2 = arenaKills.getKills(1);
        final PlayerKills gamePlayerKills3 = arenaKills.getKills(2);

        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',
            "&7" + winner.getDisplayName() + "&e ha ganado en el mapa " + "&c" + getName() + "&e!"));
        sendMessage(ChatColor.translateAlternateColorCodes('&',
            "\n\n&e&lGanador&7 - " + winner.getDisplayName() + "\n\n" + "&e&l1er Asesino&7 - "
                + gamePlayerKills1.getName() + "&7 - " + gamePlayerKills1.amount() + "\n" + "&6&l2do Asesino&7 - "
                + gamePlayerKills2.getName() + "&7 - " + gamePlayerKills2.amount() + "\n" + "&c&l3er Asesino&7 - "
                + gamePlayerKills3.getName() + "&7 - " + gamePlayerKills3.amount() + "\n\n"));
      }
      removePlayers();
      removeSpectators();
      resetArena(null);
    }
    if (callback != null)
      callback.run();
    this.state = newState;
  }

  public void addKill(String playerName) {
    arenaKills.addKill(playerName);
  }

  public PlayerKills getKills(String playerName) {
    return arenaKills.getKills(playerName);
  }

  public Location getSpectatorSpawn(GamePlayer gamePlayer) {
    return arenaWorld.getSpectatorSpawn(gamePlayer);
  }

  public void resetArena(Runnable callback) {
    arenaWorld.resetArena(callback);
  }

  public void clearChestVotes() {
    chestVotes.clear();
  }

  public void clearArenaKills() {
    arenaKills.clear();
  }

  public ArenaWorld getArenaWorld() {
    return arenaWorld;
  }

  public void removePlayers() {
    arenaPlayers.removePlayers();
  }

  public void removeSpectators() {
    arenaPlayers.removeSpectators();
  }

  public ArenaPlayers getPlayers() {
    return arenaPlayers;
  }

  public void remove(GamePlayer gamePlayer) {
    arenaPlayers.remove(gamePlayer);
  }
}
