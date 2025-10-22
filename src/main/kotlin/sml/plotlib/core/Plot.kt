package sml.plotlib.core


import java.awt.Color
import javax.swing.JFrame

/**
 * Represents a single 2D plot with axes, labels, and data series.
 */
class Plot(
    var title: String = "",
    var xlabel: String = "",
    var ylabel: String = ""
) {
    private val seriesList = mutableListOf<Series>()
    val xAxis = Axis(xlabel)
    val yAxis = Axis(ylabel)

    private var colorIndex = 0
    private val colorCycle = listOf(
        Color(31, 119, 180), // blue
        Color(255, 127, 14), // orange
        Color(44, 160, 44),  // green
        Color(214, 39, 40),  // red
        Color(148, 103, 189),// purple
        Color(140, 86, 75),  // brown
        Color(227, 119, 194),// pink
        Color(127, 127, 127),// gray
        Color(188, 189, 34), // olive
        Color(23, 190, 207)  // cyan
    )

    private fun nextColor(): Color {
        val color = colorCycle[colorIndex % colorCycle.size]
        colorIndex++
        return color
    }

    fun updateAxesRanges() {
        val allX = seriesList.flatMap { it.x }
        val allY = seriesList.flatMap { it.y }
        xAxis.min = allX.minOrNull() ?: 0.0
        xAxis.max = allX.maxOrNull() ?: 1.0
        yAxis.min = allY.minOrNull() ?: 0.0
        yAxis.max = allY.maxOrNull() ?: 1.0
    }

    fun addSeries(series: Series) {
        // Assign next colour if the series was created without an explicit one
        if (series.style.color == PlotStyle().color) {
            val newStyle = series.style.copy(color = nextColor())
            seriesList.add(series.copy(style = newStyle))
        } else {
            seriesList.add(series)
        }
        xAxis.updateRange(series.x)
        yAxis.updateRange(series.y)
    }

//    fun addSeries(series: Series) {
//        seriesList.add(series)
//        xAxis.updateRange(series.x)
//        yAxis.updateRange(series.y)
//    }

    fun addSeries(name: String, x: List<Double>, y: List<Double>, color: Color? = null) {
        val c = color ?: nextColor()
        addSeries(Series(name, x.toMutableList(), y.toMutableList(), PlotStyle(color = c)))
    }

//    fun getSeries(): List<Series> = seriesList.toList()

    fun getSeries(): List<Series> = seriesList


    fun show() {
        Renderer(this).display()
    }

    fun save(path: String, width: Int = 800, height: Int = 600) {
        when {
            path.endsWith(".svg", ignoreCase = true) -> SVGRenderer(this).save(path, width, height)
            else -> Renderer(this).save(path, width, height)
        }
    }

    fun saveSVG(path: String, width: Int = 800, height: Int = 600) {
        SVGRenderer(this).save(path, width, height)
    }
}

fun Plot.animate(fps: Int = 20, onFrame: (frame: Int, plot: Plot) -> Unit) {
    val frame = JFrame(title.ifEmpty { "Animated Plot" })
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    val panel = AnimatedPlotPanel(this, fps, onFrame)
    frame.contentPane.add(panel)
    frame.setSize(800, 600)
    frame.isVisible = true
}

