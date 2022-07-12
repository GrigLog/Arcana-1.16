package arcana.data;

import arcana.Arcana;
import arcana.common.items.AspectIcon;
import arcana.common.items.Crystal;
import com.google.common.collect.ImmutableList;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

import static arcana.common.items.ModItems.FIREWAND;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Arcana.id, existingFileHelper);
    }

    public static List<Item> generated = new ArrayList<>(ImmutableList.of(
        FIREWAND
    ));
    public static List<Item> handheld = new ArrayList<>();
    public static List<Item> blockItem = new ArrayList<>();
    //...


    @Override
    protected void registerModels() {
        List<Item> items = ForgeRegistries.ITEMS.getEntries().stream().map(e -> e.getValue()).collect(Collectors.toList());
        handheld.forEach(this::handheldItem);
        generated.forEach(this::generatedItem);
        items.stream().filter(i -> i instanceof AspectIcon).forEach(i -> makeItemModel(i, "item/generated", "aspects/"));
        items.stream().filter(i -> i instanceof Crystal).forEach(i -> makeItemModel(i, "item/generated", "items/crystals/"));
    }

    protected void generatedItem(Item item) {
        makeItemModel(item, "item/generated");
    }

    protected void handheldItem(Item i) {
        makeItemModel(i, "item/handheld");
    }

    protected void makeItemModel(Item item, String parent) {
        String name = item.getRegistryName().getPath();
        singleTexture(name, mcLoc(parent), "layer0", new ResourceLocation(Arcana.id, "items/" + name));
    }

    protected void makeItemModel(Item item, String parent, String texturePath) {
        String name = item.getRegistryName().getPath();
        singleTexture(name, mcLoc(parent), "layer0", new ResourceLocation(Arcana.id, texturePath + name));
    }
}
