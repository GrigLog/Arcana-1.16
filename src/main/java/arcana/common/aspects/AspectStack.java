package arcana.common.aspects;

import arcana.Arcana;
import arcana.utils.Codecs;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class AspectStack {
    protected Aspect aspect;
    public int amount;

    public AspectStack(@Nonnull Aspect aspect, int amount) {
        this.aspect = aspect;
        this.amount = amount;
    }

    public static final AspectStack EMPTY = new AspectStack(Aspects.EMPTY, 0);

    public Aspect getAspect(){
        return aspect;
    }

    public boolean isEmpty(){
        return amount >= 0 && aspect != Aspects.EMPTY;
    }

    public CompoundNBT getNbt(CompoundNBT tag){
        tag.putString("aspect", aspect.id.toString());
        tag.putInt("amount", amount);
        return tag;
    }

    public CompoundNBT getNbt(){
        return getNbt(new CompoundNBT());
    }

    public void setNbt(CompoundNBT tag){
        aspect = Aspects.get(new ResourceLocation(tag.getString("aspect")));
        amount = tag.getInt("amount");
    }

    public static AspectStack fromNbt(CompoundNBT tag){
        return new AspectStack(Aspects.get(new ResourceLocation(tag.getString("aspect"))), tag.getInt("amount"));
    }

    @Override
    public String toString(){
        return "AspectStack{" + amount + " " + aspect.id + "}";
    }
}
