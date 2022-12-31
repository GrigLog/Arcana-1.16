package arcana.data

import arcana.Arcana
import net.minecraft.data.DataGenerator
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.common.data.ExistingFileHelper

class BlockstateProvider(gen: DataGenerator, exFileHelper: ExistingFileHelper)
    : BlockStateProvider(gen, Arcana.id, exFileHelper) {
    override fun registerStatesAndModels() {}
}