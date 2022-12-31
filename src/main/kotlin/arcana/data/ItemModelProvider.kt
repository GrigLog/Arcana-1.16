package arcana.data

import arcana.Arcana
import arcana.common.items.ModItems
import arcana.common.items.aspect.AspectIcon
import arcana.common.items.aspect.Crystal
import arcana.common.items.wand.CapItem
import arcana.common.items.wand.CoreItem
import arcana.utils.Util
import net.minecraft.data.DataGenerator
import net.minecraft.item.Item
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.ForgeRegistries

class ItemModelProvider(generator: DataGenerator, existingFileHelper: ExistingFileHelper)
    : ItemModelProvider(generator, Arcana.id, existingFileHelper) {

    companion object {
    var generated: List<Item> = listOf(
        ModItems.FIREWAND,
        ModItems.RESEARCH_TABLE,
        ModItems.SCRIBING_TOOLS,
        ModItems.RESEARCH_NOTE,
        ModItems.RESEARCH_NOTE_COMPLETE)
    var handheld: List<Item> = ArrayList()
    var blockItem: List<Item> = ArrayList()
    //...
    }

    override fun registerModels() {
        val items = ForgeRegistries.ITEMS.values.toList()
        handheld.forEach(::handheldItem)
        generated.forEach(::generatedItem)
        items.filterIsInstance<AspectIcon>().forEach { makeItemModel(it, "item/generated", "aspects/") }
        items.filterIsInstance<Crystal>().forEach { makeItemModel(it, "item/generated", "item/crystals/") }
        items.filterIsInstance<CapItem>().forEach { makeItemModel(it, "item/generated", "item/caps/") }
        items.filterIsInstance<CoreItem>().forEach { makeItemModel(it, "item/generated", "item/cores/") }
    }

    protected fun generatedItem(item: Item) = makeItemModel(item, "item/generated")

    protected fun handheldItem(i: Item) = makeItemModel(i, "item/handheld")

    protected fun makeItemModel(item: Item?, parent: String?) {
        val name = item!!.registryName!!.path
        try {
            singleTexture(name, mcLoc(parent), "layer0", Util.arcLoc("item/$name"))
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    protected fun makeItemModel(item: Item, parent: String?, texturePath: String) {
        val name = item.registryName!!.path
        try {
            singleTexture(name, mcLoc(parent), "layer0", Util.arcLoc(texturePath + name))
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }
}