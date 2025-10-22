package sml.plotlib.examples

import sml.plotlib.core.PlotStyle
import sml.plotlib.core.Series
import sml.plotlib.core.plot
import kotlin.math.*

fun main() {
    val x = (0..500).map { it / 50.0 }
    val y = x.map { sin(it) }
    val yLower = x.map { sin(it) - 0.1 - 0.1 * abs(sin(it)) }
    val yUpper = x.map { sin(it) + 0.1 + 0.1 * abs(sin(it)) }

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

//    p.show()
    p.save("./docs/img/SineWithErrors.png")
    p.save("./docs/img/SineWithErrors.svg")  // Also save as SVG
}
