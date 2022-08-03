package arcana.common.aspects.graph.nodes;

import arcana.common.aspects.AspectList;

public class AspectListCached {
    public AspectList list = new AspectList();
    public int value = Integer.MAX_VALUE;

    public AspectListCached(){}

    public AspectListCached(AspectList aspects){
        update(aspects);
    }

    public void update(AspectList aspects){
        list = aspects;
        value = list.sum();
    }

    @Override
    public String toString() {
        return list.toString() + '|' + value;
    }
}
