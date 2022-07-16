package arcana.server.worldgen;

import arcana.Arcana;
import arcana.common.aspects.Aspect;
import arcana.common.aspects.AspectUtils;
import arcana.common.aspects.Aspects;
import arcana.common.capability.Marks;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.command.impl.LocateCommand;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;

import static java.lang.Math.*;
import java.util.Random;

public class Tower extends Structure<NoFeatureConfig> {
    public StructureFeature<?, ?> configured;
    Aspect aspect;
    public Tower(Aspect aspect) {
        super(NoFeatureConfig.CODEC);
        this.aspect = aspect;
        setRegistryName(new ResourceLocation(aspect.id.getNamespace(), "tower_" + aspect.id.getPath()));
        configured = this.configured(new NoFeatureConfig());
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return (struct, chX, chZ, box, references, seedPart) -> new Start(aspect, struct, chX, chZ, box, references, seedPart);
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator gen, BiomeProvider biomes, long seed, SharedSeedRandom rand, int chX, int chZ, Biome biome, ChunkPos chPos, NoFeatureConfig config) {
        BlockPos center = new BlockPos(chX << 4, 0, chZ << 4);
        int landHeight = gen.getFirstOccupiedHeight(center.getX(), center.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
        IBlockReader columnOfBlocks = gen.getBaseColumn(center.getX(), center.getZ());
        BlockState topBlock = columnOfBlocks.getBlockState(center.above(landHeight));
        return topBlock.getFluidState().isEmpty();
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        Aspect aspect;
        public Start(Aspect aspect, Structure<NoFeatureConfig> struct, int chX, int chZ, MutableBoundingBox box, int references, long seedPart) {
            super(struct, chX, chZ, box, references, seedPart);
            this.aspect = aspect;
        }

        public void generatePieces(DynamicRegistries registries, ChunkGenerator gen, TemplateManager tm, int chX, int chZ, Biome biome, NoFeatureConfig config) {
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
            int xCenter = (boundingBox.x0 + boundingBox.x1) / 2;
            int zCenter = (boundingBox.z0 + boundingBox.z1) / 2;
            pieces.add(new FakePiece(new BlockPos(xCenter, boundingBox.y0, zCenter), aspect)); //it spawns marks when World becomes available
        }
    }

    public static class FakePiece extends StructurePiece{
        protected static IStructurePieceType type = IStructurePieceType.setPieceId(FakePiece::new, "arcana:fake_tower_piece");

        int x, z;
        int primalIndex;
        public FakePiece(BlockPos pos, Aspect aspect) {
            super(type, 0);
            this.x = pos.getX();
            this.z = pos.getZ();
            boundingBox = new MutableBoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
            primalIndex = ArrayUtils.indexOf(AspectUtils.primalAspects, aspect);
        }

        public FakePiece(TemplateManager tm, CompoundNBT tag) {
            super(type, tag);
            primalIndex = tag.getInt("primalIndex");
        }

        @Override
        protected void addAdditionalSaveData(CompoundNBT tag) {
            tag.putInt("primalIndex", primalIndex);
        }

        @Override
        public boolean postProcess(ISeedReader world, StructureManager sm, ChunkGenerator gen, Random rand, MutableBoundingBox box, ChunkPos chPos, BlockPos pos) {
            Marks cap = world.getLevel().getCapability(Marks.CAPABILITY).resolve().orElse(null);
            int r = 100;
            int rings = 6;
            double a0 = rand.nextDouble() * PI;
            for (int i = 1; i <= rings; i++){
                double dn = 2 * PI / acos(1 - 0.5 / (i*i)); //this formula makes distance between marks in one ring = r
                int n = (int)pow(2, ceil(log(dn) / log(2))); //but we want the power of 2
                //Arcana.logger.info("ring " + i + ": " + n + " marks");
                for (int k = 0; k < n; k++){
                    int markX = (int) (boundingBox.x0 + r * i * cos(a0 + 2*PI * k/n));
                    int markZ = (int) (boundingBox.z0 + r * i * sin(a0 + 2*PI * k/n));
                    int markY = gen.getBaseHeight(markX, markZ, Heightmap.Type.WORLD_SURFACE);
                    BlockPos bp = new BlockPos(markX, markY, markZ);
                    cap.positions[primalIndex].add(bp);
                }
            }
            return true;
        }
    }
}
