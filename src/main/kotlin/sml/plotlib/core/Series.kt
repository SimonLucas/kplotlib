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