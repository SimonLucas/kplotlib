package sml.plotlib

import java.awt.Color

/**
 * Kotlin DSL entry point.
 */
fun plot(title: String = "", block: Plot.() -> Unit): Plot {
    val p = Plot(title)
    p.block()
    return p
}

fun Plot.line(
    name: String = "series",
    x: MutableList<Double>,
    y: MutableList<Double>,
    color: Color? = null
) {
    addSeries(
        Series(
            name = name,
            x = x,
            y = y,
            style = PlotStyle(color = color ?: Color.BLUE)
        )
    )
}

fun Plot.scatter(
    name: String = "series",
    x: MutableList<Double>,
    y: MutableList<Double>,
    color: Color? = null
) {
    addSeries(
        Series(
            name = name,
            x = x,
            y = y,
            style = PlotStyle(color = color ?: Color.RED, showPoints = true, lineWidth = 0f)
        )
    )
}
