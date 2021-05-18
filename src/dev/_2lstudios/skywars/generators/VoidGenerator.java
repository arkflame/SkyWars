package dev._2lstudios.skywars.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class VoidGenerator extends ChunkGenerator {
  private final List<BlockPopulator> defaultPopulators = new ArrayList<>();
  
  public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int ChunkX, int ChunkZ, ChunkGenerator.BiomeGrid biome) {
    return createChunkData(world);
  }
  
  public List<BlockPopulator> getDefaultPopulators(World world) {
    return this.defaultPopulators;
  }
}
