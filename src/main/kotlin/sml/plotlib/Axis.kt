package sml.plotlib

/**
 * Holds axis configuration and computed range, with tick generation.
 */
data class Axis(
    var label: String = "",
    var min: Double = 0.0,
    var max: Double = 1.0
) {
    fun updateRange(values: List<Double>) {
        if (values.isEmpty()) return
        val vmin = values.minOrNull() ?: min
        val vmax = values.maxOrNull() ?: max
        if (seriesCount == 0) {
            min = vmin
            max = vmax
        } else {
            if (vmin < min) min = vmin
            if (vmax > max) max = vmax
        }
        seriesCount++
    }

    fun ticks(count: Int = 6): List<Double> {
        if (max == min) return listOf(min)
        val step = (max - min) / (count - 1)
        return List(count) { i -> min + i * step }
    }

    companion object {
        private var seriesCount = 0
    }
}
