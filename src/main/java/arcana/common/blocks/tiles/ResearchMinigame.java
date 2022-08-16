package arcana.common.blocks.tiles;

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
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;

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
    Map<Integer, List<Integer>> rootPaths = new HashMap<>();

    public ResearchMinigame(ResearchTable table){
        this.table = table;
        items = table.minigameItems;
    }

    public boolean isActive(){
        return active;
    }

    public void update(){
        canBeFinished = !items.getStackInSlot(0).isEmpty();
        for (int root : rootPaths.keySet()) {
            try {
                rootPaths.put(root, findPath(ImmutableList.of(root), getAspect(root)));
            } catch (IndexOutOfBoundsException e){
                Arcana.logger.error("outOfBounds");
            }
        }
    }

    public void toggle(long seed){
        if (active)
            finish();
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
                rootPaths.put(index, ImmutableList.of());
                placeRoots--;
            }
        }
    }

    public void finish(){
        active = false;
        rootPaths.clear();
        InventoryUtils.clear(items);
    }

    public void renderPaths(MatrixStack ms, ResearchTableContainer container){
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buf = tes.getBuilder();
        for (List<Integer> path : rootPaths.values()) {
            if (path.size() == 0)
                continue;
            buf.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for (int i : path) {
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
        AspectList list = new AspectList();
        for (int i = 0; i < GRID_WIDTH * GRID_HEIGHT; i++){
            list.add(getAspects(i));
        }
        int entries = list.list.size();
        for (int i = 0; i < entries; i++){
            float yTop = (-entries / 2f + i) * 20;
            AspectStack as = list.list.get(i);
            ClientUtil.renderAspect(ms, as.getAspect(), 0, (int) yTop);
            ClientUtil.drawString(ms, Minecraft.getInstance().font, String.valueOf(as.amount), 20, (int) yTop, 0xffffffff);
        }
    }

    public void updateRoots(){
        rootPaths.clear();
        for (int i = 0; i < GRID_WIDTH * GRID_HEIGHT; i++){
            Item item = items.getStackInSlot(i).getItem();
            if (item instanceof AspectIcon){
                rootPaths.put(i, ImmutableList.of());
            }
        }
    }

    List<Integer> findPath(List<Integer> path, Aspect lastAspect){
        int index = path.get(path.size() - 1);
        //if (items.getStackInSlot(index).isEmpty())
        //    return path;
        if (rootPaths.containsKey(index) && path.size() > 1)
            return path;
        List<Integer> res = path;
        for (int diff : new int[]{1, -1, GRID_WIDTH, -GRID_WIDTH}){
            int newIndex = index + diff;
            if (newIndex < 0 || newIndex >= GRID_WIDTH * GRID_HEIGHT)
                continue;
            if (path.contains(newIndex))
                continue;

            AspectList newAspects = getAspects(newIndex);
            for (AspectStack a : newAspects){
                Aspect aspect = a.getAspect();
                if (aspect == lastAspect || AspectUtils.areAspectsConnected(aspect, lastAspect)){
                    List<Integer> newPath = ImmutableList.<Integer>builder().addAll(path).add(newIndex).build();
                    newPath = findPath(newPath, aspect);
                    if (newPath.size() > res.size()){
                        res = newPath;
                        break;
                    }
                }
            }
        }
        return res;
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
}
