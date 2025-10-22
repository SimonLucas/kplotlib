package sml.plotlib.core

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
    x: List<Double>,
    y: List<Double>,
    color: Color? = null
) {
    addSeries(
        Series(
            name = name,
            x = x.toMutableList(),
            y = y.toMutableList(),
            style = PlotStyle(color = color ?: Color.BLUE)
        )
    )
}

fun Plot.scatter(
    name: String = "series",
    x: List<Double>,
    y: List<Double>,
    color: Color? = null
) {
    addSeries(
        Series(
            name = name,
            x = x.toMutableList(),
            y = y.toMutableList(),
            style = PlotStyle(color = color ?: Color.RED, showPoints = true, lineWidth = 0f)
        )
    )
}
