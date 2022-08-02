package arcana.data;

import arcana.Arcana;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static arcana.common.blocks.ModBlocks.*;

public class BlockstateProvider extends BlockStateProvider {
    public BlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Arcana.id, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }
}
