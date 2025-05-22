package arcana.mixins;

import arcana.common.blocks.tiles.QuantumChestInventory;
import arcana.mixin_interfaces.IQuantumInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//This mixin could be easily replaced with a Capability, but I thought the mixin would be more elegant (I might have been mistaken...)
@Mixin(PlayerEntity.class)
public class PlayerMixin implements IQuantumInventory {
    @Unique
    public QuantumChestInventory arcana$quantumInventory = new QuantumChestInventory();

    public QuantumChestInventory arcana$get() {
        return arcana$quantumInventory;
    }

    public void arcana$set(QuantumChestInventory inv) {
        arcana$quantumInventory = inv;
    }

    //Don't forget (de)serialization
    @Inject(method = "addAdditionalSaveData", at=@At("TAIL"))
    private void save(CompoundNBT tag, CallbackInfo ci) {
        tag.put("arcana:QuantumInventory", arcana$quantumInventory.createTag());
    }

    @Inject(method = "readAdditionalSaveData", at=@At("TAIL"))
    private void load(CompoundNBT tag, CallbackInfo ci) {
        if (tag.contains("arcana:QuantumInventory")) {
            arcana$quantumInventory.fromTag(tag.getList("arcana:QuantumInventory", 10));
        }
    }
}
