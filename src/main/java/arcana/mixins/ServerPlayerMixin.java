package arcana.mixins;

import arcana.mixin_interfaces.IQuantumInventory;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin extends PlayerEntity {
    public ServerPlayerMixin(World pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Inject(method="restoreFrom", at=@At("TAIL"))
    void copyQuantumInventory(ServerPlayerEntity pThat, boolean pKeepEverything, CallbackInfo ci) {
        ((IQuantumInventory)this).arcana$set(((IQuantumInventory)pThat).arcana$get());
    }
}
