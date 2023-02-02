package arcana.common.items.spell

import arcana.common.aspects.AspectList
import arcana.common.aspects.Aspects
import arcana.utils.Util.eyePosition
import arcana.utils.Util.scale
import net.minecraft.block.Blocks
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemUseContext
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.math.vector.Vector3i
import net.minecraft.world.World
import kotlin.math.abs
import kotlin.math.sign

class QuickWall : Spell() {
    override val pressCost = AspectList().add(Aspects.EARTH, 1).toArray()

    override fun press(world: World, caster: LivingEntity) = false

    override fun pressBlock(caster: LivingEntity, ctx: ItemUseContext): Boolean {
        if (!trySpendMana(caster, pressCost)) return false
        val world = caster.level
        val pos = ctx.clickedPos
        val heightDir = ctx.clickedFace.normal

        val posExact = Vector3d(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5)
        var rel = caster.eyePosition().subtract(posExact)
        rel = rel.multiply(1.0 - abs(heightDir.x), 1.0 - abs(heightDir.y), 1.0 - abs(heightDir.z)) //remove "height" coord
        val relAbs = doubleArrayOf(abs(rel.x), abs(rel.y), abs(rel.z))
        var maxAbs = 0.0
        var maxIdx = 0
        for (i in (0..2)) {
            if (relAbs[i] > maxAbs) {
                maxAbs = relAbs[i]
                maxIdx = i
            }
        }
        val relAbsInt = IntArray(3) {if (it == maxIdx) sign(relAbs[it]).toInt() else 0}
        val playerDir = Vector3i(relAbsInt[0], relAbsInt[1], relAbsInt[2])
        val widthDir = playerDir.cross(heightDir)

        for (i in (1..2)) {
            for (j in (-1..1)) {
                val placePos = pos.offset(heightDir.scale(i)).offset(widthDir.scale(j))
                if (world.getBlockState(placePos).isAir) {
                    world.setBlockAndUpdate(placePos, Blocks.STONE.defaultBlockState())
                }
            }
        }
        return true
    }
}