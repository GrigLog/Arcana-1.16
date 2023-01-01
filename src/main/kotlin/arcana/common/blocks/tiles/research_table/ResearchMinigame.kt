package arcana.common.blocks.tiles.research_table

import arcana.Arcana
import arcana.common.aspects.*
import arcana.common.containers.ResearchTableContainer
import arcana.common.items.aspect.AspectIcon
import arcana.utils.ClientUtil
import arcana.utils.InventoryUtils
import com.google.common.collect.ImmutableList
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ColorHelper.PackedColor
import net.minecraft.util.Util
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.items.ItemStackHandler
import net.minecraftforge.registries.ForgeRegistries
import org.lwjgl.opengl.GL11
import java.util.*
import kotlin.math.pow

class ResearchMinigame(var table: ResearchTable) {
    var canBeFinished = false
    var isActive = false
    var items: ItemStackHandler = table.minigameItems

    //root elements are the ones you need to connect. They cannot be moved.
    //root path is a list of indexes.
    var rootPaths: MutableMap<Int, RootPath> = HashMap()

    fun update() {
        val counter = aspectCounter
        canBeFinished = if (counter.list.size == 0) {
            false
        } else {
            val firstAmount = counter.list[0].amount
            val sameAmounts = counter.list.stream().allMatch { ass: AspectStack -> ass.amount == firstAmount }
            val allConnected = rootPaths.values.stream().allMatch { rp: RootPath -> rp.finished.toInt() == 1 }
            sameAmounts and allConnected
        }
        for (root in rootPaths.keys) {
            try {
                rootPaths[root] = bestPath(RootPath(root, getAspect(root)))
            } catch (e: IndexOutOfBoundsException) {
                Arcana.logger.error("outOfBounds")
            }
        }
    }

    fun toggle(seed: Long, player: ServerPlayerEntity?) {
        if (isActive) finish(player) else start(seed)
    }

    fun start(seed: Long) {
        val r = Random(seed)
        isActive = true
        table.items.extractItem(ResearchTable.PAPER, 1, false)
        var placeRoots = 4
        while (placeRoots > 0) {
            val index = r.nextInt(GRID_WIDTH * GRID_HEIGHT)
            if (!rootPaths.containsKey(index)) {
                val a = ARRAY[r.nextInt(ARRAY.size)]
                val aspectIcon = ForgeRegistries.ITEMS.getValue(a.id)!!
                items.setStackInSlot(index, ItemStack(aspectIcon))
                rootPaths[index] = RootPath(index, a)
                placeRoots--
            }
        }
    }

    fun finish(player: ServerPlayerEntity?) {
        val counter = aspectCounter
        isActive = false
        rootPaths.clear()
        InventoryUtils.clear(items)
        if (player == null)
            return
        var totalScore = 0f
        val mult = counter.list.size.toDouble().pow(1.5).toFloat()
        for (ass in counter.list) {
            totalScore += (ass.amount.toDouble().pow(0.5) * mult).toFloat()
        }
        player.sendMessage(StringTextComponent("You have scored $totalScore points"), Util.NIL_UUID)
    }

    fun updateRoots() {
        rootPaths.clear()
        for (i in 0 until GRID_WIDTH * GRID_HEIGHT) {
            val item = items.getStackInSlot(i).item
            if (item is AspectIcon) {
                rootPaths[i] = RootPath(i, item.aspect)
            }
        }
    }

    val aspectCounter: AspectList
        get() {
            val res = AspectList()
            val set = HashSet<Int>()
            for (rp in rootPaths.values) {
                set.addAll(rp.path)
            }
            for (idx in set) {
                res.add(getAspects(idx))
            }
            return res
        }

    fun bestPath(trace: RootPath): RootPath {
        val idx = trace.last
        if (rootPaths.containsKey(idx) && trace.path.size > 1) {
            trace.finished = 1
            return trace
        }
        var best = trace
        var test: RootPath
        for (diff in intArrayOf(1, -1, GRID_WIDTH, -GRID_WIDTH)) {
            val newIndex = idx + diff
            if (newIndex < 0 || newIndex >= GRID_WIDTH * GRID_HEIGHT) continue
            if (trace.path.contains(newIndex)) continue
            val newAspects = getAspects(newIndex)
            var newAspect: Aspect? = null
            for (a in newAspects) {
                val aspect = a.aspect
                if (aspect === trace.lastAspect || AspectUtils.areAspectsConnected(aspect, trace.lastAspect)) {
                    newAspect = aspect
                    break
                }
            }
            if (newAspect == null) continue
            test = bestPath(trace.withAdded(newIndex, newAspect))
            if (test.finished > best.finished || test.finished == best.finished && test.path.size > best.path.size) {
                best = test
            }
        }
        return best
    }

    fun getAspects(index: Int): AspectList {
        return ItemAspectRegistry[items.getStackInSlot(index)]
    }

    fun getAspect(index: Int): Aspect {
        val list = getAspects(index).list
        return if (list.isNotEmpty()) list[0].aspect else Aspects.EMPTY
    }

    fun load(tag: CompoundNBT) {
        isActive = tag.getBoolean("active")
    }

    fun save(tag: CompoundNBT): CompoundNBT {
        tag.putBoolean("active", isActive)
        return tag
    }

    class Level(var rand: Random, var roots: Int)

    class RootPath(var path: ImmutableList<Int>, var lastAspect: Aspect) {
        var finished: Byte = 0 //actually boolean
        val start = path[0]
        val last = path[path.size - 1]

        fun withAdded(elem: Int, lastAspect: Aspect): RootPath {
            return RootPath(ImmutableList.builder<Int>().addAll(path).add(elem).build(), lastAspect)
        }

        constructor(root: Int, lastAspect: Aspect) : this(ImmutableList.of<Int>(root), lastAspect)
    }

    fun renderPaths(ms: MatrixStack, container: ResearchTableContainer) {
        val tes = Tessellator.getInstance()
        val buf = tes.builder
        for (rp in rootPaths.values) {
            if (rp.path.size <= 1) continue
            buf.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR)
            for (i in rp.path) {
                if (getAspect(i) === Aspects.EMPTY) break
                val color = getAspect(i).colors[2]
                val s = container.slots[i + 2]
                buf.vertex(ms.last().pose(), (s.x + 8).toFloat(), (s.y + 8).toFloat(), 0f).color(
                    PackedColor.red(color) / 256f,
                    PackedColor.green(color) / 256f,
                    PackedColor.blue(color) / 256f,
                    1f
                ).endVertex()
            }
            tes.end()
        }
    }

    fun renderAspectCounter(ms: MatrixStack) {
        val tes = Tessellator.getInstance()
        val buf = tes.builder
        val counter = aspectCounter
        val entries = counter.list.size
        for (i in 0 until entries) {
            val yTop = (-entries / 2f + i) * 20
            val ass = counter.list[i]
            ClientUtil.renderAspect(ms, ass.aspect, 0, yTop.toInt())
            ClientUtil.drawString(
                ms,
                Minecraft.getInstance().font,
                ass.amount.toString(),
                20,
                yTop.toInt(),
                -0x1
            )
        }
    }

    companion object {
        const val GRID_WIDTH = 13
        const val GRID_HEIGHT = 8
        val ARRAY = Aspects.primal
    }
}