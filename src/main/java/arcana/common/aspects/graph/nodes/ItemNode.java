package arcana.common.aspects.graph.nodes;

import arcana.common.aspects.AspectList;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemNode implements IArcanaNode{
    public final Item item;
    public AspectListCached cache;
    public List<IngredientNode> links = new ArrayList<>();

    public ItemNode(Item item, AspectList list) {
        this.item = item;
        cache = list != null ? new AspectListCached(list) : new AspectListCached();
    }

    @Override
    public String toString() {
        return "Item{" + item.getRegistryName() + '}';
    }
}
