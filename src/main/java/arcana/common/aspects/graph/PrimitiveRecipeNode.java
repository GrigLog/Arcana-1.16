package arcana.common.aspects.graph;


import arcana.common.aspects.AspectList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


import java.util.*;

public class PrimitiveRecipeNode implements IArcanaNode{
    public ItemStack output;
    public List<ItemStack> inputs;

    public AspectList inputAspects;
    public int value;

    public PrimitiveRecipeNode(ItemStack output, List<ItemStack> inputs) {
        this.inputs = inputs;
        this.output = output;
    }

    public void calcValue(Map<Item, AspectList> aspectInfo){
        inputAspects = new AspectList();
        for (ItemStack is : inputs){
            AspectList temp = aspectInfo.get(is.getItem()).copy();
            temp.list.forEach(as -> as.amount *= is.getCount());
            inputAspects.add(temp);
        }
        inputAspects.multiply(1f / output.getCount());
        value = inputAspects.sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Recipe{(");
        for (ItemStack is: inputs){
            sb.append(is.getCount()).append('x').append(is.getItem().getRegistryName()).append(',');
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(")=>");
        sb.append(output.getCount()).append('x').append(output.getItem().getRegistryName()).append('}');
        return sb.toString();
    }
}
