package arcana.common.aspects;

import arcana.Arcana;
import arcana.utils.Codecs;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class AspectList implements Iterable<AspectStack> {
    public List<AspectStack> list = new ArrayList<>();
    public AspectList(){}
    public AspectList(List<AspectStack> list){
        this.list = list;
    }

    public AspectList add(Aspect a, int amount){
        for (AspectStack as : list){
            if (as.aspect == a){
                as.amount += amount;
                return this;
            }
        }
        list.add(new AspectStack(a, amount));
        return this;
    }

    public AspectList add(AspectStack a){
        for (AspectStack as : list){
            if (as.aspect == a.aspect){
                as.amount += a.amount;
                return this;
            }
        }
        list.add(a);
        return this;
    }

    public AspectList add(Iterable<AspectStack> aspects){
        for (AspectStack as : aspects){
            add(as);
        }
        return this;
    }

    public AspectList copy(){
        return new AspectList(list.stream().map(as -> new AspectStack(as.aspect, as.amount)).collect(Collectors.toList()));
    }

    public JsonArray getJson(){
        JsonArray arr = new JsonArray();
        list.forEach(as -> arr.add(Codecs.ASPECT_STACK.encodeStart(JsonOps.INSTANCE, as).getOrThrow(false, Arcana.logger::error)));
        return arr;
    }

    public int getAmount(Aspect a){
        for (AspectStack as : list){
            if (as.aspect == a)
                return as.amount;
        }
        return 0;
    }

    public int sum(){
        int s = 0;
        for (AspectStack as : list){
            s += as.amount;
        }
        return s;
    }

    public AspectList multiply(float a){
        list = list.stream().peek(as -> as.amount = Math.round(as.amount * a))
            .filter(as -> as.amount > 0).collect(Collectors.toList());
        return this;
    }

    @Override
    public Iterator<AspectStack> iterator() {
        return list.iterator();
    }
}
