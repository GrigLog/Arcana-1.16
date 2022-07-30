package arcana.common.aspects;

import arcana.Arcana;
import arcana.common.items.aspect.Crystal;
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
        processing = true;
        // add new data
        files.forEach(ItemAspectRegistry::parse);

        // compute aspect assignment
        // for every item defined, give them aspects
        itemAssociations.forEach((item, list) -> itemAspects.merge(item, list, AspectList::add));

        // for every item tag, give them aspects
        itemTagAssociations.forEach((tag, list) -> {
            for (Item item : tag.getValues())
                itemAspects.merge(item, list, AspectList::add);
        });

        // TODO: for every item not already given aspects in this way, give according to recipes
        //for (IRecipe<?> recipe : recipes.getRecipes()) {
        //    Item item = recipe.getResultItem().getItem();
        //    if (!itemAspects.containsKey(item))
        //        itemAspects.put(item, getGenerating(item));
        //    // generating avoids getting stuck in recursive loops.
        //    generating.clear();
        //}

        Arcana.logger.info("Assigned aspects to {} items", itemAspects.size());
        processing = false;
        files.clear();
    }


    /*
    private AspectList getFromRecipes(Item item) {
        generating.add(item);
        AspectList ret;
        List<AspectList> allGenerated = new ArrayList<>();
        // consider every recipe that produces this item
        for (IRecipe<?> recipe : recipes.getRecipes())
            if (recipe.getResultItem().getItem() == item) {
                AspectList generated = new AspectList();
                for (Ingredient ingredient : recipe.getIngredients()) {
                    if (ingredient.getItems().length > 0) {
                        ItemStack first = ingredient.getItems()[0];
                        if (!generating.contains(first.getItem())) {
                            AspectList ingredients = getGenerating(first);
                            for (AspectStack stack : ingredients.list)
                                generated.add(new AspectStack(stack.getAspect(), Math.max(stack.amount / recipe.getResultItem().getCount(), 1)));
                        }
                    }
                }
                if (recipe instanceof AspectInfluencingRecipe)
                    ((AspectInfluencingRecipe) recipe).influence(generated);
                allGenerated.add(generated);
            }
        ret = allGenerated.stream()
            // pair of aspects:total num of aspects
            .map(stacks -> Pair.of(stacks, stacks.stream().mapToDouble(AspectStack::getAmount).sum()))
            // filter combos with 0 aspects
            .filter(pair -> pair.getSecond() > 0)
            // sort by least total aspects
            .min(Comparator.comparingDouble(Pair::getSecond))
            // get aspects
            .map(Pair::getFirst).orElse(new ArrayList<>());
        return ret;
    }

    private AspectList getGenerating(Item item) {
        if (itemAspects.containsKey(item))
            return itemAspects.get(item);
        else
            return getFromRecipes(item);
    }

    private AspectList getGenerating(ItemStack stack) {
        if (itemAspects.containsKey(stack.getItem()))
            return get(stack.getItem());
        else
            return getFromRecipes(stack.getItem());
    }
     */

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