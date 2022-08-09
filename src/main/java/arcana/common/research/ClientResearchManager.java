package arcana.common.research;

import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClientResearchManager {
    //TODO: use Path's or something similar?
    public static Map<ResourceLocation, Map<String, Map<String, BookEntry>>> books = new HashMap<>(); //arcana:arcanum -> alchemy -> [root, ores, ...]

    public static void add(ResourceLocation fullPath, BookEntry entry){
        String[] path = fullPath.getPath().split("/");
        ResourceLocation book = new ResourceLocation(fullPath.getNamespace(), path[0]);
        String category = path[1];
        String entryName = path[2];
        books.computeIfAbsent(book, k -> new HashMap<>()).computeIfAbsent(category, k -> new HashMap<>()).put(entryName, entry);
    }

    public static BookEntry get(ResourceLocation fullPath){
        String[] path = fullPath.getPath().split("/");
        ResourceLocation book = new ResourceLocation(fullPath.getNamespace(), path[0]);
        String category = path[1];
        String entryName = path[2];
        return books.get(book).get(category).get(entryName);
    }

    public static Collection<BookEntry> categoryEntries(ResourceLocation book, String category){
        return books.get(book).get(category).values();
    }
}
