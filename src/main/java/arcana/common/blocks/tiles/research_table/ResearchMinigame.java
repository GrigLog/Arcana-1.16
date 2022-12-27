package arcana.common.blocks.tiles.research_table;

import arcana.Arcana;
import arcana.common.aspects.*;
import arcana.common.containers.ResearchTableContainer;
import arcana.common.items.aspect.AspectIcon;
import arcana.utils.ClientUtil;
import arcana.utils.InventoryUtils;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.*;

import static net.minecraft.util.ColorHelper.PackedColor.*;

public class ResearchMinigame {
    public static final int GRID_WIDTH = 13;
    public static final int GRID_HEIGHT = 8;
    public static final Aspect[] ARRAY = Aspects.primal;

    public boolean canBeFinished = false;
    ResearchTable table;
    boolean active = false;
    ItemStackHandler items;
    //root elements are the ones you need to connect. They cannot be moved.
    //root path is a list of indexes.
    Map<Integer, RootPath> rootPaths = new HashMap<>();

    public ResearchMinigame(ResearchTable table){
        this.table = table;
        items = table.minigameItems;
    }

    public boolean isActive(){
        return active;
    }

    public void update(){
        AspectList counter = getAspectCounter();
        if (counter.list.size() == 0) {
            canBeFinished = false;
        } else {
            int firstAmount = counter.list.get(0).amount;
            boolean sameAmounts = counter.list.stream().allMatch(as -> as.amount == firstAmount);
            boolean allConnected = rootPaths.values().stream().allMatch(rp->rp.finished == 1);
            canBeFinished = sameAmounts & allConnected;
        }
        for (int root : rootPaths.keySet()) {
            try {
                rootPaths.put(root, bestPath(new RootPath(root, getAspect(root))));
            } catch (IndexOutOfBoundsException e){
                Arcana.logger.error("outOfBounds");
            }
        }
    }

    public void toggle(long seed, @Nullable ServerPlayerEntity player){
        if (active)
            finish(player);
        else
            start(seed);
    }

    public void start(long seed){
        Random r = new Random(seed);
        active = true;
        table.items.extractItem(ResearchTable.PAPER, 1, false);
        int placeRoots = 4;
        while (placeRoots > 0) {
            int index = r.nextInt(GRID_WIDTH * GRID_HEIGHT);
            if (!rootPaths.containsKey(index)) {
                Aspect a = ARRAY[r.nextInt(ARRAY.length)];
                Item aspectIcon = ForgeRegistries.ITEMS.getValue(a.id);
                items.setStackInSlot(index, new ItemStack(aspectIcon));
                rootPaths.put(index, new RootPath(index, a));
                placeRoots--;
            }
        }
    }

    public void finish(@Nullable ServerPlayerEntity player){
        AspectList counter = getAspectCounter();
        active = false;
        rootPaths.clear();
        InventoryUtils.clear(items);
        if (player == null)
            return;
        float totalScore = 0;
        float mult = (float) Math.pow(counter.list.size(), 1.5);
        for (AspectStack as : counter.list) {
            totalScore += Math.pow(as.amount, 0.5) * mult;
        }
        player.sendMessage(new StringTextComponent("You have scored " + totalScore + " points"), Util.NIL_UUID);
    }

    public void updateRoots(){
        rootPaths.clear();
        for (int i = 0; i < GRID_WIDTH * GRID_HEIGHT; i++){
            Item item = items.getStackInSlot(i).getItem();
            if (item instanceof AspectIcon){
                rootPaths.put(i, new RootPath(i, ((AspectIcon) item).aspect));
            }
        }
    }

    AspectList getAspectCounter() {
        AspectList res = new AspectList();
        HashSet<Integer> set = new HashSet<>();
        for (RootPath rp : rootPaths.values()) {
            set.addAll(rp.path);
        }
        for (int idx : set) {
            res.add(getAspects(idx));
        }
        return res;
    }

    RootPath bestPath(RootPath trace) {
        int idx = trace.getLast();
        if (rootPaths.containsKey(idx) && trace.path.size() > 1) {
            trace.finished = 1;
            return trace;
        }
        RootPath best = trace;
        RootPath test;
        for (int diff : new int[]{1, -1, GRID_WIDTH, -GRID_WIDTH}) {
            int newIndex = idx + diff;
            if (newIndex < 0 || newIndex >= GRID_WIDTH * GRID_HEIGHT)
                continue;
            if (trace.path.contains(newIndex))
                continue;
            AspectList newAspects = getAspects(newIndex);
            Aspect newAspect = null;
            for (AspectStack a : newAspects){
                Aspect aspect = a.getAspect();
                if (aspect == trace.lastAspect || AspectUtils.areAspectsConnected(aspect, trace.lastAspect)){
                    newAspect = aspect;
                    break;
                }
            }
            if (newAspect == null)
                continue;

            test = bestPath(trace.withAdded(newIndex, newAspect));
            if (test.finished > best.finished ||
                (test.finished == best.finished && test.path.size() > best.path.size())) {
                best = test;
            }
        }
        return best;
    }


    protected AspectList getAspects(int index){
        return ItemAspectRegistry.get(items.getStackInSlot(index));
    }

    protected Aspect getAspect(int index){
        List<AspectStack> list = getAspects(index).list;
        return !list.isEmpty() ? list.get(0).getAspect() : Aspects.EMPTY;
    }

    public void load(CompoundNBT tag){
        active = tag.getBoolean("active");
    }

    public CompoundNBT save(CompoundNBT tag){
        tag.putBoolean("active", active);
        return tag;
    }

    public static class Level {
        public Random rand;
        public int roots;

        public Level(Random rand, int roots) {
            this.rand = rand;
            this.roots = roots;
        }
    }

    public static class RootPath {
        public ImmutableList<Integer> path;
        public Aspect lastAspect;
        public byte finished = 0; //actually boolean

        public int getStart() {
            return path.get(0);
        }

        public int getLast() {
            return path.get(path.size() - 1);
        }

        public RootPath withAdded(int elem, Aspect lastAspect) {
            return new RootPath(ImmutableList.<Integer>builder().addAll(path).add(elem).build(), lastAspect);
        }

        public RootPath(int root, Aspect lastAspect) {
            this(ImmutableList.of(root), lastAspect);
        }

        public RootPath(ImmutableList<Integer> path, Aspect lastAspect) {
            this.path = path;
            this.lastAspect = lastAspect;
        }
    }

    public void renderPaths(MatrixStack ms, ResearchTableContainer container){
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buf = tes.getBuilder();
        for (RootPath rp : rootPaths.values()) {
            if (rp.path.size() <= 1)
                continue;
            buf.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for (int i : rp.path) {
                if (getAspect(i) == Aspects.EMPTY)
                    break;
                int color = getAspect(i).colors.get(2);
                Slot s = container.slots.get(i + 2);
                buf.vertex(ms.last().pose(), s.x + 8, s.y + 8, 0).color(red(color) / 256f, green(color) / 256f, blue(color) / 256f, 1f).endVertex();
            }
            tes.end();
        }
    }

    public void renderAspectCounter(MatrixStack ms){
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buf = tes.getBuilder();
        AspectList counter = getAspectCounter();
        int entries = counter.list.size();
        for (int i = 0; i < entries; i++){
            float yTop = (-entries / 2f + i) * 20;
            AspectStack as = counter.list.get(i);
            ClientUtil.renderAspect(ms, as.getAspect(), 0, (int) yTop);
            ClientUtil.drawString(ms, Minecraft.getInstance().font, String.valueOf(as.amount), 20, (int) yTop, 0xffffffff);
        }
    }
}
