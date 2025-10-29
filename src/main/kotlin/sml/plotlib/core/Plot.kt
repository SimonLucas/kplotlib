package sml.plotlib.core


import java.awt.Color
import javax.swing.JFrame

/**
 * Represents a single 2D plot with axes, labels, and data series.
 */
class Plot @JvmOverloads constructor(
    var title: String = "",
    var xlabel: String = "",
    var ylabel: String = "",
    var theme: Theme = Theme.getDefault()
) {
    private val seriesList = mutableListOf<Series>()
    val xAxis = Axis(xlabel)
    val yAxis = Axis(ylabel)

    private var colorIndex = 0

    private fun nextColor(): Color {
        val color = theme.colors.getSeriesColor(colorIndex)
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

    @JvmOverloads
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

