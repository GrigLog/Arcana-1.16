package arcana.common.aspects;

import arcana.common.ArcanaGroup;
import arcana.common.items.AspectIcon;
import arcana.common.items.Crystal;
import arcana.utils.Pair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class AspectUtils {
	public static final List<Item> aspectItems = new ArrayList<>();
	public static final Map<Aspect, Item> aspectCrystalItems = new HashMap<>();
	public static final Aspect[] primalAspects = new Aspect[]{Aspects.AIR, Aspects.CHAOS, Aspects.EARTH, Aspects.FIRE, Aspects.ORDER, Aspects.WATER};
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

	public static ResourceLocation getAspectTextureLocation(Aspect aspect) {
		return new ResourceLocation(aspect.id.getNamespace(), "textures/aspect/" + aspect.id.getPath() + ".png");
	}


	public static void putAspect(CompoundNBT compound, String key, Aspect aspect){
		compound.putString(key, aspect.id.toString());
	}

	public static Aspect getAspect(CompoundNBT compound, String key){
		return Aspect.fromId(new ResourceLocation(compound.getString(key)));
	}

	/*
	public static int getEmptyCell(AspectHandler handler) {
		return handler.getHolders().indexOf(handler.findFirstHolderMatching(h -> h.getStack().isEmpty()));
	}

	public static String aspectHandlerToJson(AspectHandler handler) {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
		return gson.toJson(handler.getHolders());
	}

	public static List<AspectStack> squish(List<AspectStack> unSquished){
		return StreamUtils.partialReduce(unSquished, AspectStack::getAspect, (left, right) -> new AspectStack(left.getAspect(), left.getAmount() + right.getAmount()));
	}

	public static List<Aspect> castContainingAspects() {
		return Casts.castMap.values().stream().map(ICast::getSpellAspect).collect(Collectors.toList());
	}*/
}
