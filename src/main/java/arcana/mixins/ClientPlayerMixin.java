package arcana.mixins;

import arcana.common.items.spell.Spell;
import arcana.common.items.wand.WandItem;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovementInput;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerMixin {
    @Redirect(method="aiStep", at=@At(value="FIELD", target="Lnet/minecraft/util/MovementInput;forwardImpulse:F", opcode=Opcodes.PUTFIELD))
    private void setLeftImpulse(MovementInput input, float original) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        ItemStack stack = player.getUseItem();
        if (!(stack.getItem() instanceof WandItem)) {
            return;
        }
        Spell spell = WandItem.getSpell(stack);
        input.leftImpulse *= 5 * spell.getCastSpeedMult();
        input.forwardImpulse *= 5 * spell.getCastSpeedMult();
    }
}
