package sml.plotlib.examples

import sml.plotlib.core.line
import sml.plotlib.core.plot
import kotlin.math.*

fun main() {
    val x = (0..200).map { it / 20.0 }.toMutableList()
    val y1 = x.map { sin(it) }.toMutableList()
    val y2 = x.map { cos(it) }.toMutableList()
    val y3 = x.map { sin(it) * cos(it) }.toMutableList()
    val y4 = x.map { sin(it) * sin(it) }.toMutableList()
    val y5 = x.map { sin(it * it) }.toMutableList()

    val p = plot("Sine, Cosine, and Product") {
        xlabel = "x"
        ylabel = "f(x)"

        line("sin(x)", x, y1)
        line("cos(x)", x, y2)
        line("sin(x)*cos(x)", x, y3)
        line("sin(x)*sin(x)", x, y4)
        line("sin(x * x)", x, y5)
    }

    p.show()
    // p.save("sine_cosine_legend.png")
}
