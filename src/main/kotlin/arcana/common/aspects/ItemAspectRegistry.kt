package arcana.common.aspects

import arcana.Arcana
import arcana.common.aspects.graph.ItemAspectResolver
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import net.minecraft.client.resources.JsonReloadListener
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.RecipeManager
import net.minecraft.profiler.IProfiler
import net.minecraft.resources.IResourceManager
import net.minecraft.tags.ITag
import net.minecraft.tags.ItemTags
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.server.ServerLifecycleHooks
import net.minecraftforge.registries.ForgeRegistries
import java.util.*

/**
 * Associates items with aspects. Every item is associated with a set of aspect stacks, and item stack may be given extra
 * aspects.
 * <br></br>
 * Items and item tags are associated with sets of aspects in JSON files. Additionally, stack functions, defined in code,
 * can add or remove aspects to an item stack, based on NBT, damage levels, or anything else.
 */
class ItemAspectRegistry(private val recipes: RecipeManager) : JsonReloadListener(GSON, "item_aspects") {
    private var files: MutableMap<ResourceLocation, JsonElement>? = null
    public override fun apply(objects: MutableMap<ResourceLocation, JsonElement>, resourceManager: IResourceManager, profiler: IProfiler) {
        itemAssociations.clear()
        itemTagAssociations.clear()
        itemAspects.clear()
        files = objects
        finish(false)
    }

    fun finish(serverRunning: Boolean) {
        if (!serverRunning) {
            val server = ServerLifecycleHooks.getCurrentServer()
            if (server == null || !server.isRunning) //will not be called for the first time, and will not throw errors about uninitialized tags
                return
        }
        // add new data
        files!!.forEach { (fileLoc, e) -> parse(fileLoc, e) }

        // compute aspect assignment
        // for every item defined, give them aspects
        itemAssociations.forEach { (item, list) -> itemAspects.merge(item, list, AspectList::add) }

        // for every item tag, give them aspects
        itemTagAssociations.forEach { (tag, list) ->
            for (item in tag.values)
                itemAspects.merge(item, list.copy(), AspectList::add)
        }
        files!!.clear()
        Arcana.logger.info("Assigned aspects to {} items", itemAspects.size)
        //TODO: use cache to determine if resolving is necessary
        ItemAspectResolver.resolve(itemAspects, recipes, ForgeRegistries.ITEMS.values)
        itemAspects.forEach { (item, aspects) ->
            aspects.list.sortWith(Comparator.comparingInt { ass: AspectStack -> -ass.amount })
            aspects.list = aspects.list.subList(0, Math.min(5, aspects.list.size))
        }
        Arcana.logger.info("Assigned aspects to {} items", itemAspects.size)
    }

    companion object {
    val GSON = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
    var processing = false
    private var itemAssociations: MutableMap<Item, AspectList> = HashMap()
    private var itemTagAssociations: MutableMap<ITag<Item>, AspectList> = HashMap()
    private var itemAspects: MutableMap<Item, AspectList> = HashMap()
    private val generating: Set<Item> = HashSet()

    operator fun get(item: Item): AspectList = itemAspects.computeIfAbsent(item) { AspectList() }

    operator fun get(stack: ItemStack): AspectList {
        val item = stack.item
        var aspects = get(item).copy()
        if (EnchantmentHelper.getEnchantments(stack).isNotEmpty())
            aspects.add(Aspects.MANA, 5)
        //if (item instanceof BlockItem && Taint.isTainted(((BlockItem)item).getBlock()))
        //	aspects.add(Aspects.TAINT, 3);
        if (item is IOverrideAspects)
            aspects = item.getAspects(stack, aspects)
        return aspects
    }

    private fun parse(fileLoc: ResourceLocation, e: JsonElement) {
        // just go through the keys and map them to tags or items
        // then put them in the relevant maps
        // error if they don't map
        if (e.isJsonObject) {
            val json = e.asJsonObject
            for ((key, value) in json.entrySet()) {
                if (key.startsWith("#")) {
                    val itemTagLoc = ResourceLocation(key.substring(1))
                    val itemTag = ItemTags.getAllTags().getTag(itemTagLoc)
                    if (itemTag != null)
                        parseAspectStackList(fileLoc, value).ifPresent{ al -> itemTagAssociations[itemTag] = al }
                    else
                        Arcana.logger.error("Invalid item tag \"" + key.substring(1) + "\" found in file " + fileLoc + "!")
                } else {
                    val itemLoc = ResourceLocation(key)
                    val item = ForgeRegistries.ITEMS.getValue(itemLoc)
                    if (item != null)
                        parseAspectStackList(fileLoc, value).ifPresent { stacks: AspectList -> itemAssociations[item] = stacks }
                    else
                        Arcana.logger.error("Invalid item tag \"" + key.substring(1) + "\" found in file " + fileLoc + "!")
                }
            }
        }
    }

    fun parseAspectStackList(file: ResourceLocation, listElement: JsonElement): Optional<AspectList> {
        if (listElement.isJsonArray) {
            val array = listElement.asJsonArray
            val ret = AspectList()
            for (element in array) {
                if (element.isJsonObject) {
                    val json = element.asJsonObject
                    val id = ResourceLocation(json["aspect"].asString)
                    val amount = JSONUtils.getAsInt(json, "amount", 1)
                    val aspect = Aspects.ASPECTS[id]
                    if (aspect != null)
                        ret.add(AspectStack(aspect, amount))
                    else Arcana.logger.error("Invalid aspect $id referenced in file $file")
                } else Arcana.logger.error("Invalid aspect stack found in $file - not an object!")
            }
            return Optional.of(ret)
        } else Arcana.logger.error("Invalid aspect stack list found in $file - not a list!")
        return Optional.empty()
    }
    }
}