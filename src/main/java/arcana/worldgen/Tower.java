package arcana.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

import java.util.Random;

public class Tower extends Structure<TowerConfig> {
    public static final StructureSeparationSettings separation = new StructureSeparationSettings(10, 5, 880055535);
    public Tower(Codec<TowerConfig> codec) {
        super(codec);
        setRegistryName("tower");
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public IStartFactory<TowerConfig> getStartFactory() {
        return Start::new;
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator gen, BiomeProvider biomes, long seed, SharedSeedRandom rand, int chX, int chZ, Biome biome, ChunkPos chPos, TowerConfig config) {
        BlockPos centerOfChunk = new BlockPos(chX << 4, 0, chZ << 4);
        int landHeight = gen.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
        IBlockReader columnOfBlocks = gen.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ());
        BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));
        return topBlock.getFluidState().isEmpty();
    }

    public static class Start extends StructureStart<TowerConfig> {
        public Start(Structure<TowerConfig> struct, int chX, int chZ, MutableBoundingBox box, int references, long seedPart) {
            super(struct, chX, chZ, box, references, seedPart);
        }

        public void generatePieces(DynamicRegistries registries, ChunkGenerator gen, TemplateManager tm, int chX, int chZ, Biome biome, TowerConfig config) {
            int x = (chX << 4)  + random.nextInt(16);
            int z = (chZ << 4) + random.nextInt(16);
            BlockPos center = new BlockPos(x, 0, z); //jigsaw figures out y automatically for the first piece

            JigsawPattern pattern = registries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation("arcana:tower/middle"));
            JigsawManager.addPieces(registries,
                new VillageConfig(() -> pattern, 4),
                AbstractVillagePiece::new, gen, tm, center, pieces, random, false, true);

            int yMin = pieces.stream().mapToInt(p -> p.getBoundingBox().y0).min().getAsInt();
            int ySurface = gen.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            pieces.forEach(piece -> {
                piece.move(0, ySurface - yMin, 0);
            });
            calculateBoundingBox();
        }
    }
}
