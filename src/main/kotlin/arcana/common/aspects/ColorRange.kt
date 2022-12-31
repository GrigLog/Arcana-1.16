package arcana.common.aspects

/**
 * Stores a set of colours.
 */
class ColorRange constructor(val colors: IntArray) {
    operator fun get(color: Int): Int {
        return colors[Math.min(color, colors.size - 1)]
    }

    companion object {
        fun create(vararg colors: Int): ColorRange {
            return ColorRange(colors)
        }
    }
}