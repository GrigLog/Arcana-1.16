package arcana.common.aspects.graph.nodes;

import arcana.common.aspects.AspectList;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemNode {
    public final Item item;
    public AspectListCached cache;
    public List<IngredientNode> links = new ArrayList<>();

    public AspectListCached cacheCrafts;
    public ItemNode remainder = null;  //"bucket" and "milk" are "remainder" and "origin" respectively. One item can have several origins but only one remainder.
    public List<ItemNode> origins = new ArrayList<>();

    public ItemNode(Item item, AspectList list) {
        this.item = item;
        cache = list != null ? new AspectListCached(list) : new AspectListCached();
        cacheCrafts = item.hasCraftingRemainingItem() ? new AspectListCached() : cache;
    }

    public void calcBasic(AspectList list) {
        cache.update(list);
    }

    public void calcCraftsValue() {
        cacheCrafts.update(cache.list.copy().subtract(remainder.cache.list));
    }

    public boolean knownBasic() {
        return cache.value != Integer.MAX_VALUE;
    }

    public boolean knownCrafts() {
        return cacheCrafts.value != Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "Item{" + item.getRegistryName() + '}';
    }
}
