package sml.plotlib

import kotlin.math.*

fun main() {
    val x = (0..500).map { it / 50.0 }.toMutableList()
    val y = x.map { sin(it) }.toMutableList()
    val yLower = x.map { sin(it) - 0.1 - 0.1 * abs(sin(it)) }.toMutableList()
    val yUpper = x.map { sin(it) + 0.1 + 0.1 * abs(sin(it)) }.toMutableList()

    val p = plot("Sine with Error Region") {
        xlabel = "x"
        ylabel = "sin(x)"
        addSeries(
            Series(
                name = "sin(x)",
                x = x,
                y = y,
                yLower = yLower,
                yUpper = yUpper,
                style = PlotStyle(showPoints = false)
            )
        )
    }

    p.show()
}
