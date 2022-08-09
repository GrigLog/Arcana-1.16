package arcana.common.research;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;

public class BookEntry {
    public Item[] icons;
    public byte x, y;
    public BookSection[] sections; //includes requirements
    public ImmutableList<BookEntry> parents;
}
