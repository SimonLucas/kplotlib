package sml.plotlib.examples

import sml.plotlib.core.line
import sml.plotlib.core.plot
import kotlin.math.*

fun main() {
    val x = (0..200).map { it / 20.0 }
    val y1 = x.map { sin(it) }
    val y2 = x.map { cos(it) }

    val p = plot("SVG Export Test") {
        xlabel = "x"
        ylabel = "f(x)"
        line("sin(x)", x, y1)
        line("cos(x)", x, y2)
    }

    // Test auto-detection by extension
    p.save("./docs/img/test-auto.svg")

    // Test explicit SVG method
    p.saveSVG("./docs/img/test-explicit.svg")

    // Test PNG still works
    p.save("./docs/img/test-png.png")

    println("SVG export test complete!")
}
