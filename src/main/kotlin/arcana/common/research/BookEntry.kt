package arcana.common.research

import com.google.common.collect.ImmutableList
import net.minecraft.item.Item

class BookEntry {
    lateinit var icons: Array<Item>
    var x: Byte = 0
    var y: Byte = 0
    lateinit var sections: Array<BookSection>  //includes requirements
    lateinit var parents: ImmutableList<BookEntry>
}