package arcana.common.aspects.graph;

import net.minecraft.item.Item;

public class ItemNode implements IArcanaNode{
    public final Item item;

    public ItemNode(Item item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "Item{" + item.getRegistryName() + '}';
    }
}
