package arcana.utils.wrappers

import net.minecraft.inventory.container.*
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.fml.network.IContainerFactory

object ContainerWrapper {
    fun <T : Container?> simple(name: String, factory: ContainerType.IFactory<T>): ContainerType<T> {
        return ContainerType(factory).setRegistryName(name) as ContainerType<T>
    }

    fun <T : Container?> withExtraData(name: String, factory: IContainerFactory<T>): ContainerType<T> {
        return ContainerType(factory).setRegistryName(name) as ContainerType<T>
    }

    private val empty: ITextComponent = StringTextComponent("")
    fun namelessProvider(provider: IContainerProvider): INamedContainerProvider {
        return SimpleNamedContainerProvider(provider, empty)
    }
}