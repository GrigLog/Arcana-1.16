package arcana.server.worldgen

import arcana.common.aspects.Aspect
import arcana.common.aspects.Aspects
import arcana.common.capability.getMarks
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.util.registry.DynamicRegistries
import net.minecraft.util.registry.Registry
import net.minecraft.world.ISeedReader
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.feature.jigsaw.JigsawManager
import net.minecraft.world.gen.feature.structure.*
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import net.minecraft.world.gen.feature.template.TemplateManager
import org.apache.commons.lang3.ArrayUtils
import java.util.*
import java.util.function.Consumer
import kotlin.math.*

class Tower(var aspect: Aspect) : Structure<NoFeatureConfig>(NoFeatureConfig.CODEC) {
    var configured: StructureFeature<*, *>

    init {
        registryName = ResourceLocation(aspect.id.namespace, "tower_" + aspect.id.path)
        configured = configured(NoFeatureConfig())
    }

    override fun step() = GenerationStage.Decoration.SURFACE_STRUCTURES

    override fun getStartFactory(): IStartFactory<NoFeatureConfig> {
        return IStartFactory { struct, chX, chZ, box, references, seedPart ->
            Start(aspect, struct, chX, chZ, box, references, seedPart)
        }
    }

    override fun isFeatureChunk(
        gen: ChunkGenerator,
        biomes: BiomeProvider,
        seed: Long,
        rand: SharedSeedRandom,
        chX: Int,
        chZ: Int,
        biome: Biome,
        chPos: ChunkPos,
        config: NoFeatureConfig
    ): Boolean {
        val center = BlockPos(chX shl 4, 0, chZ shl 4)
        val landHeight = gen.getFirstOccupiedHeight(center.x, center.z, Heightmap.Type.WORLD_SURFACE_WG)
        val columnOfBlocks = gen.getBaseColumn(center.x, center.z)
        val topBlock = columnOfBlocks.getBlockState(center.above(landHeight))
        return topBlock.fluidState.isEmpty
    }

    class Start(
        var aspect: Aspect,
        struct: Structure<NoFeatureConfig>,
        chX: Int,
        chZ: Int,
        box: MutableBoundingBox,
        references: Int,
        seedPart: Long
    ) : StructureStart<NoFeatureConfig>(struct, chX, chZ, box, references, seedPart) {

        override fun generatePieces(
            registries: DynamicRegistries,
            gen: ChunkGenerator,
            tm: TemplateManager,
            chX: Int,
            chZ: Int,
            biome: Biome,
            config: NoFeatureConfig
        ) {
            val x = (chX shl 4) + random.nextInt(16)
            val z = (chZ shl 4) + random.nextInt(16)
            val center = BlockPos(x, 0, z) //jigsaw figures out y automatically for the first piece
            val pattern = registries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)[ResourceLocation("arcana:tower/middle")]
            JigsawManager.addPieces(registries,
                VillageConfig({ pattern }, 4), ::AbstractVillagePiece,
                gen, tm, center, pieces, random, false, true
            )
            val yMin = pieces.stream().mapToInt { p: StructurePiece -> p.boundingBox.y0 }.min().asInt
            val ySurface = gen.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG)
            pieces.forEach(Consumer { piece: StructurePiece -> piece.move(0, ySurface - yMin, 0) })
            calculateBoundingBox()
            val xCenter = (boundingBox.x0 + boundingBox.x1) / 2
            val zCenter = (boundingBox.z0 + boundingBox.z1) / 2
            pieces.add(
                FakePiece(
                    BlockPos(xCenter, boundingBox.y0, zCenter),
                    aspect
                )
            ) //it spawns marks when World becomes available
        }
    }

    class FakePiece : StructurePiece {
        var x = 0
        var z = 0
        var primalIndex: Int

        constructor(pos: BlockPos, aspect: Aspect) : super(TYPE, 0) {
            x = pos.x
            z = pos.z
            boundingBox = MutableBoundingBox(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z)
            primalIndex = ArrayUtils.indexOf(Aspects.PRIMAL, aspect)
        }

        constructor(tm: TemplateManager, tag: CompoundNBT) : super(TYPE, tag) {
            primalIndex = tag.getInt("primalIndex")
        }

        override fun addAdditionalSaveData(tag: CompoundNBT) {
            tag.putInt("primalIndex", primalIndex)
        }

        override fun postProcess(
            world: ISeedReader,
            sm: StructureManager,
            gen: ChunkGenerator,
            rand: Random,
            box: MutableBoundingBox,
            chPos: ChunkPos,
            pos: BlockPos
        ): Boolean {
            val cap = world.level.getMarks()
            val r = 100
            val rings = 6
            val a0 = rand.nextDouble() * Math.PI
            for (i in 1..rings) {
                val dn =
                    2 * Math.PI / acos(1 - 0.5 / (i * i)) //this formula makes distance between marks in one ring = r
                val n = 2.0.pow(ceil(log2(dn))).toInt() //but we want the power of 2
                //Arcana.logger.info("ring " + i + ": " + n + " marks");

                for (k in 0 until n) {
                    val markX = (boundingBox.x0 + r * i * cos(a0 + 2 * Math.PI * k / n)).toInt()
                    val markZ = (boundingBox.z0 + r * i * sin(a0 + 2 * Math.PI * k / n)).toInt()
                    val markY = gen.getBaseHeight(markX, markZ, Heightmap.Type.WORLD_SURFACE)
                    val bp = BlockPos(markX, markY, markZ)
                    cap.positions[primalIndex] = cap.positions[primalIndex].plusElement(bp) //should not hurt performance much
                }
            }
            return true
        }

        companion object {
            protected var TYPE =
                IStructurePieceType.setPieceId(Tower::FakePiece, "arcana:fake_tower_piece")
        }
    }
}