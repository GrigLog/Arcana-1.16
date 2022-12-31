package arcana.utils.wrappers

import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import java.util.function.Supplier

object TileWrapper {
    fun <T : TileEntity> wrap(
        name: String,
        factoryIn: Supplier<out T>,
        vararg validBlocks: Block
    ): TileEntityType<T> {
        return TileEntityType.Builder.of(factoryIn, *validBlocks).build(null).setRegistryName(name) as TileEntityType<T>
    }

    fun <T : TileEntity> wrap(factoryIn: Supplier<out T>, validBlock: Block): TileEntityType<T> {
        return wrap(validBlock.registryName!!.path, factoryIn, validBlock)
    }
}