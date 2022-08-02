package arcana.common.aspects;

import arcana.common.ArcanaGroup;
import arcana.common.items.aspect.AspectIcon;
import arcana.common.items.aspect.Crystal;
import arcana.utils.Pair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.*;
import java.util.stream.Collectors;


public class AspectUtils {
	public static final List<Item> aspectItems = new ArrayList<>();
	public static final Map<Aspect, Item> aspectCrystalItems = new HashMap<>();
	//do not change order!
	public static final Aspect[] primalAspects = new Aspect[]{Aspects.AIR, Aspects.WATER, Aspects.EARTH, Aspects.FIRE, Aspects.ORDER, Aspects.CHAOS};
	public static final Aspect[] sinAspects = new Aspect[]{Aspects.ENVY, Aspects.LUST, Aspects.SLOTH, Aspects.PRIDE, Aspects.GREED, Aspects.WRATH, Aspects.GLUTTONY};
	public static List<ItemStack> aspectStacks;

	public static void registerItems(IForgeRegistry<Item> registry){
		for(Aspect aspect : Aspects.getList())
			if(aspect != Aspects.EMPTY){
				AspectIcon item = new AspectIcon(aspect);
				Item crystal = new Crystal(ArcanaGroup.itemProps(), aspect);
				registry.registerAll(item, crystal);
			}
		aspectStacks = aspectItems.stream().map(ItemStack::new).collect(Collectors.toList());
	}
	
	public static boolean areAspectsConnected(Aspect a, Aspect b){
		if (a == null || b == null)
			return false;
		return Aspects.COMBINATIONS.inverse().getOrDefault(a, Pair.of(null, null)).contains(b)
			|| Aspects.COMBINATIONS.inverse().getOrDefault(b, Pair.of(null, null)).contains(a);
	}

	public static void add(Collection<AspectStack> aspects, Aspect a, int amount){
		for (AspectStack as : aspects){
			if (as.getAspect() == a){
				as.amount += amount;
				return;
			}
		}
		aspects.add(new AspectStack(a, amount));
	}

	public static void add(Collection<AspectStack> aspects, AspectStack a){
		for (AspectStack as : aspects){
			if (as.getAspect() == a.getAspect()){
				as.amount += a.amount;
				return;
			}
		}
		aspects.add(a);
	}

	public static void add(Collection<AspectStack> list1, Collection<AspectStack> list2){
		for (AspectStack as : list2){
			add(list1, as);
		}
	}
}
