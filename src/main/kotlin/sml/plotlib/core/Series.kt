package sml.plotlib.core

/**
 * Represents a single data series with mutable values.
 * Intended for both static and animated plots.
 */
data class Series(
    val name: String,
    val x: MutableList<Double>,
    val y: MutableList<Double>,
    val style: PlotStyle = PlotStyle(),
    val yLower: MutableList<Double>? = null,
    val yUpper: MutableList<Double>? = null
) {
    init {
        require(x.size == y.size) { "x and y must be the same length" }
        if (yLower != null) require(yLower.size == x.size)
        if (yUpper != null) require(yUpper.size == x.size)
    }

    val hasErrorRegion: Boolean get() = (yLower != null && yUpper != null)
}

/**
 * Convenience function to create a Series from immutable lists.
 * The lists are converted to MutableList internally to support animation.
 */
fun Series(
    name: String,
    x: List<Double>,
    y: List<Double>,
    style: PlotStyle = PlotStyle(),
    yLower: List<Double>? = null,
    yUpper: List<Double>? = null
): Series = Series(
    name = name,
    x = x.toMutableList(),
    y = y.toMutableList(),
    style = style,
    yLower = yLower?.toMutableList(),
    yUpper = yUpper?.toMutableList()
)