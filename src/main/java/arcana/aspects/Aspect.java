package arcana.aspects;

import arcana.Arcana;
import net.minecraft.util.ResourceLocation;

public class Aspect {
	public final ResourceLocation id;
	public final ColorRange colors;
	public final String translationKey;

	private Aspect(ResourceLocation id, ColorRange colors){
		this.id = id;
		this.colors = colors;
		translationKey = "aspect." + id.getNamespace() + "." + id.getPath();
	}

	@Override
	public String toString() {
		return "Aspect: " + id;
	}

	public ResourceLocation getVisMeterTexture(){
		// only valid for primals
		return new ResourceLocation(Arcana.id, "textures/gui/hud/wand/vis/" + id + ".png");
	}

	// Static Methods

	public static Aspect create(String name, ColorRange colors){
		Aspect aspect = new Aspect(new ResourceLocation(Arcana.id, name), colors);
		Aspects.ASPECTS.put(aspect.id, aspect);
		return aspect;
	}

	public static Aspect fromId(ResourceLocation id) {
		return Aspects.ASPECTS.get(id);
	}
}