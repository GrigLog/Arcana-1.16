package arcana.common.items;

import arcana.common.aspects.Aspect;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectIcon extends Item {
	Aspect aspect;
	public AspectIcon(Aspect aspect){
		super(new Properties());
		setRegistryName(new ResourceLocation(aspect.id.getNamespace(), aspect.id.getPath()));
		this.aspect = aspect;
	}

	@Override
	public String getDescriptionId() {
		return aspect.translationKey;
	}
}