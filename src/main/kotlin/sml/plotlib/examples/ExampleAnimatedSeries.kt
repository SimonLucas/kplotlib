package sml.plotlib.examples

import sml.plotlib.core.animate
import sml.plotlib.core.line
import sml.plotlib.core.plot
import sml.plotlib.core.replaceAllWith
import kotlin.math.*

fun main() {
    val x = (0..200).map { it / 20.0 }
    val y1 = x.map { sin(it) }
    val y2 = x.map { cos(it) }

    val p = plot("Animated Sine and Cosine") {
        xlabel = "x"
        ylabel = "f(x + t)"
        line("sin(x+t)", x, y1)
        line("cos(x+t)", x, y2)
    }

    var t = 0.0

    p.animate(fps = 10) { _, plot ->
        t += 0.1
        val s1 = plot.getSeries()[0]
        val s2 = plot.getSeries()[1]

        // Shift window: recompute x for the new start time
        for (i in s1.x.indices) {
            s1.x[i] = i / 20.0 + t
        }

        // Recompute y values from new x
        s1.y.replaceAllWith(s1.x.map { sin(it) })
        s2.y.replaceAllWith(s2.x.map { cos(it) })

        // Keep axes in sync
        plot.updateAxesRanges()
    }
}
