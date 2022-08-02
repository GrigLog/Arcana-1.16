package arcana.utils.wrappers;

import net.minecraft.inventory.container.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.network.IContainerFactory;

public class ContainerWrapper {
    public static <T extends Container> ContainerType<T> simple(String name, ContainerType.IFactory<T> factory){
        return (ContainerType<T>) new ContainerType<>(factory).setRegistryName(name);
    }

    public static <T extends Container> ContainerType<T> withExtraData(String name, IContainerFactory<T> factory){
        return (ContainerType<T>) new ContainerType<>(factory).setRegistryName(name);
    }

    private static final ITextComponent empty = new StringTextComponent("");
    public static INamedContainerProvider namelessProvider(IContainerProvider provider){
        return new SimpleNamedContainerProvider(provider, empty);
    }
}
