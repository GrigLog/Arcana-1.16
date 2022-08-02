package arcana.common.aspects;

import arcana.Arcana;
import arcana.common.aspects.graph.ItemAspectResolver;
import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * Associates items with aspects. Every item is associated with a set of aspect stacks, and item stack may be given extra
 * aspects.
 * <br/>
 * Items and item tags are associated with sets of aspects in JSON files. Additionally, stack functions, defined in code,
 * can add or remove aspects to an item stack, based on NBT, damage levels, or anything else.
 */
public class ItemAspectRegistry extends JsonReloadListener {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static boolean processing = false;
    private static Map<Item, AspectList> itemAssociations = new HashMap<>();
    private static Map<ITag<Item>, AspectList> itemTagAssociations = new HashMap<>();
    private static Collection<BiConsumer<ItemStack, AspectList>> stackFunctions = new ArrayList<>();
    private static Map<Item, AspectList> itemAspects = new HashMap<>();
    private RecipeManager recipes;
    private static Set<Item> generating = new HashSet<>();

    private Map<ResourceLocation, JsonElement> files;

    public ItemAspectRegistry(RecipeManager recipes) {
        super(GSON, "item_aspects");
        this.recipes = recipes;
    }


    public static AspectList get(Item item) {
        return itemAspects.computeIfAbsent(item, i -> new AspectList());
    }


    public static AspectList get(ItemStack stack) {
        Item item = stack.getItem();
        AspectList aspects = get(item).copy();
        if (EnchantmentHelper.getEnchantments(stack).size() > 0)
            aspects.add(Aspects.MANA, 5);
        //if (item instanceof BlockItem && Taint.isTainted(((BlockItem)item).getBlock()))
        //	aspects.add(Aspects.TAINT, 3);
        for (BiConsumer<ItemStack, AspectList> function : stackFunctions)
            function.accept(stack, aspects);
        if (item instanceof IOverrideAspects)
            aspects = ((IOverrideAspects) item).get(stack, aspects);
        return aspects;
    }

    public void apply(@Nonnull Map<ResourceLocation, JsonElement> objects, @Nonnull IResourceManager resourceManager, @Nonnull IProfiler profiler) {
        itemAssociations.clear();
        itemTagAssociations.clear();
        stackFunctions.clear();
        itemAspects.clear();
        files = objects;

        finish(false);
    }

    public void finish(boolean serverRunning) {
        if (!serverRunning){
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null || !server.isRunning()) //will not be called for the first time, and will not throw errors about uninitialized tags
                return;
        }
        // add new data
        files.forEach(ItemAspectRegistry::parse);

        // compute aspect assignment
        // for every item defined, give them aspects
        itemAssociations.forEach((item, list) -> itemAspects.merge(item, list, AspectList::add));

        // for every item tag, give them aspects
        itemTagAssociations.forEach((tag, list) -> {
            for (Item item : tag.getValues())
                itemAspects.merge(item, list.copy(), AspectList::add);
        });
        files.clear();

        Arcana.logger.info("Assigned aspects to {} items", itemAspects.size());
        //TODO: use cache to determine if resolving is necessary
        ItemAspectResolver.resolve(itemAspects, recipes, ForgeRegistries.ITEMS.getValues());
        itemAspects.forEach((item, aspects) -> {
            aspects.list.sort(Comparator.comparingInt(as -> -as.amount));
            aspects.list = aspects.list.subList(0, Math.min(5, aspects.list.size()));
        });
        Arcana.logger.info("Assigned aspects to {} items", itemAspects.size());
    }

    private static void parse(ResourceLocation fileLoc, JsonElement e) {
        // just go through the keys and map them to tags or items
        // then put them in the relevant maps
        // error if they don't map
        if (e.isJsonObject()) {
            JsonObject json = e.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                if (key.startsWith("#")) {
                    ResourceLocation itemTagLoc = new ResourceLocation(key.substring(1));
                    ITag<Item> itemTag = ItemTags.getAllTags().getTag(itemTagLoc);
                    if (itemTag != null)
                        parseAspectStackList(fileLoc, value).ifPresent(stacks -> itemTagAssociations.put(itemTag, stacks));
                    else
                        Arcana.logger.error("Invalid item tag \"" + key.substring(1) + "\" found in file " + fileLoc + "!");
                } else {
                    ResourceLocation itemLoc = new ResourceLocation(key);
                    Item item = ForgeRegistries.ITEMS.getValue(itemLoc);
                    if (item != null)
                        parseAspectStackList(fileLoc, value).ifPresent(stacks -> itemAssociations.put(item, stacks));
                    else
                        Arcana.logger.error("Invalid item tag \"" + key.substring(1) + "\" found in file " + fileLoc + "!");
                }
            }
        }
    }


    public static Optional<AspectList> parseAspectStackList(ResourceLocation file, JsonElement listElement) {
        if (listElement.isJsonArray()) {
            JsonArray array = listElement.getAsJsonArray();
            AspectList ret = new AspectList();
            for (JsonElement element : array) {
                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    ResourceLocation id = new ResourceLocation(object.get("aspect").getAsString());
                    int amount = JSONUtils.getAsInt(object, "amount", 1);
                    Aspect aspect = Aspects.get(id);
                    if (aspect != null)
                        ret.add(new AspectStack(aspect, amount));
                    else
                        Arcana.logger.error("Invalid aspect " + id + " referenced in file " + file);
                } else
                    Arcana.logger.error("Invalid aspect stack found in " + file + " - not an object!");
            }
            return Optional.of(ret);
        } else
            Arcana.logger.error("Invalid aspect stack list found in " + file + " - not a list!");
        return Optional.empty();
    }
}