package arcana.common.research

import net.minecraft.util.ResourceLocation

object ClientResearchManager {
    //TODO: use Path's or something similar?
    var books: HashMap<ResourceLocation, HashMap<String, HashMap<String, BookEntry>>> = HashMap()
    //arcana:arcanum -> alchemy -> [root, ores, ...]

    fun add(fullPath: ResourceLocation, entry: BookEntry) {
        val path = fullPath.path.split("/")
        val book = ResourceLocation(fullPath.namespace, path[0])
        val category = path[1]
        val entryName = path[2]
        books.getOrPut(book, ::HashMap)
            .getOrPut(category, ::HashMap)
            .put(entryName, entry)
    }

    operator fun get(fullPath: ResourceLocation): BookEntry? {
        val path = fullPath.path.split("/")
        val book = ResourceLocation(fullPath.namespace, path[0])
        val category = path[1]
        val entryName = path[2]
        return books[book]!![category]!![entryName]
    }

    fun categoryEntries(book: ResourceLocation, category: String): Collection<BookEntry> {
        return books[book]!![category]!!.values
    }
}