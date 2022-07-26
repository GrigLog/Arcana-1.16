package arcana.common.containers;

import arcana.common.blocks.tiles.ResearchTable;
import arcana.utils.wrappers.ContainerWrapper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;

import java.util.function.Consumer;

public class ResearchTableContainer extends Container {
    ResearchTable table;
    public static ContainerType<ResearchTableContainer> type = ContainerWrapper.withExtraData("research_table", ResearchTableContainer::new);
    public ResearchTableContainer(int containerId, ResearchTable table){
        super(type, containerId);
        this.table = table;
    }

    public ResearchTableContainer(int containerId, PlayerInventory playerInv, PacketBuffer buf){
        super(type, containerId);
        if (buf == null)
            return;
        table = (ResearchTable) playerInv.player.level.getBlockEntity(buf.readBlockPos());
    }

    public static INamedContainerProvider serverProvider(ResearchTable table){
        return ContainerWrapper.namelessProvider(
            (id, playerInv, player) -> new ResearchTableContainer(id, table)
        );
    }

    public static Consumer<PacketBuffer> clientDataHolder(ResearchTable table){
        return buf -> buf.writeBlockPos(table.getBlockPos());
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return player.blockPosition().distManhattan(table.getBlockPos()) <= 5;
    }
}
