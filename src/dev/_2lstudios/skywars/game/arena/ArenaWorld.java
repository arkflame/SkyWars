package dev._2lstudios.skywars.game.arena;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.GameCage;
import dev._2lstudios.skywars.game.GamePlayer;
import dev._2lstudios.skywars.managers.CageManager;
import dev._2lstudios.skywars.time.TimeType;
import dev._2lstudios.skywars.utils.BukkitUtil;
import dev._2lstudios.skywars.utils.WorldUtil;

public class ArenaWorld {
    private final Collection<Vector> chests = new HashSet<>();
    private final Collection<ArenaSpawn> spawns = new HashSet<>();
    private final GameArena arena;
    private Vector spectatorSpawn = null;
    private World world;

    public ArenaWorld(final GameArena gameArena) {
        this.arena = gameArena;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(final World world) {
        this.world = world;
    }

    public Collection<Vector> getChests() {
        return this.chests;
    }

    public Collection<ArenaSpawn> getSpawns() {
        return this.spawns;
    }

    void setChestsAndSpawns() {
        if (world != null) {
            chests.clear();
            spawns.clear();

            byte b;
            int i;
            Chunk[] arrayOfChunk;
            for (i = (arrayOfChunk = world.getLoadedChunks()).length, b = 0; b < i;) {
                Chunk chunk = arrayOfChunk[b];
                byte b1;
                int j;
                BlockState[] arrayOfBlockState;
                for (j = (arrayOfBlockState = chunk.getTileEntities()).length, b1 = 0; b1 < j;) {
                    BlockState blockState = arrayOfBlockState[b1];
                    if (blockState instanceof Chest) {
                        chests.add(blockState.getLocation().toVector());
                    } else if (blockState instanceof org.bukkit.block.Beacon) {
                        spawns.add(new ArenaSpawn(arena, blockState.getLocation().toVector()));
                    }
                    b1++;
                }
                b++;
            }
        }
    }

    public Collection<Location> getChestLocations() {
        Collection<Location> chestsLocations = new HashSet<>();

        for (Vector vector : this.chests) {
            chestsLocations.add(vector.toLocation(world));
        }

        return chestsLocations;
    }

    public void generateCages(final GameCage gameCage) {
        BukkitUtil.runSync(SkyWars.getPlugin(), () -> {
            for (ArenaSpawn gameSpawn : getSpawns())
                gameSpawn.createCage(gameCage);
        });
    }

    public void resetArena(Runnable callback) {
        final CageManager cageManager = SkyWars.getMainManager().getCageManager();
        WorldUtil worldUtil = SkyWars.getWorldUtil();
        Server server = Bukkit.getServer();

        arena.removePlayers();
        arena.removeSpectators();
        arena.clearChestVotes();
        arena.clearArenaKills();

        worldUtil.delete(() -> {
            AtomicReference<World> atomicWorld = new AtomicReference<>(null);

            worldUtil.create(() -> {
                this.world = atomicWorld.get();

                generateCages(cageManager.getDefaultCage());
            }, arena.getName(), atomicWorld);
        }, arena.getWorld(), server.getWorlds().get(0).getSpawnLocation());
    }

    public void setTime(TimeType timeType) {
        if (timeType == TimeType.MANANA) {
            world.setTime(22009L);
        } else if (timeType == TimeType.DIA) {
            world.setTime(24000L);
        } else if (timeType == TimeType.TARDE) {
            world.setTime(12000L);
        } else if (timeType == TimeType.NOCHE) {
            world.setTime(18000L);
        }
    }

    public Location getSpectatorSpawn(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        EntityDamageEvent lastDamageCause = player.getLastDamageCause();
        if (player.getWorld() != world
                || (lastDamageCause != null && lastDamageCause.getCause() == EntityDamageEvent.DamageCause.VOID)) {
            if (spectatorSpawn != null)
                return spectatorSpawn.toLocation(world);
            return world.getSpawnLocation();
        }
        return player.getLocation();
    }

    final void setSpectatorSpawn(World world) {
        Location spawnLocation = world.getSpawnLocation();
        int maxHeight = world.getMaxHeight() / 2;
        int airBlocks = 0;
        for (int y = maxHeight; spectatorSpawn == null; y--) {
            if (y <= 2) {
                spawnLocation.setY(maxHeight);
                spectatorSpawn = spawnLocation.toVector();
            } else {
                spawnLocation.setY(y);
                Block block = spawnLocation.getBlock();
                if (block == null || block.getType() == Material.AIR) {
                    airBlocks++;
                } else if (airBlocks > 1) {
                    spectatorSpawn = spawnLocation.toVector();
                } else {
                    airBlocks = 0;
                }
            }
        }
    }

    public void setSpectatorSpawn(Vector vector) {
        spectatorSpawn = vector;
    }

    public Vector getSpectatorVector() {
        return spectatorSpawn;
    }

    public void addSpawn(final Vector vector) {
        spawns.add(new ArenaSpawn(arena, vector));
    }

    public void addChest(Vector vector) {
        chests.add(vector);
    }

    public void clearSpawns() {
        spawns.clear();
    }

    public void clearChests() {
        chests.clear();
    }

    public ArenaSpawn getFirstSpawn() {
        for (ArenaSpawn spawn : getSpawns()) {
            if (spawn.getPlayerUUID() == null) {
                return spawn;
            }
        }

        return null;
    }
}
