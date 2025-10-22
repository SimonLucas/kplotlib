package sml.plotlib.core

import java.awt.BasicStroke
import java.awt.Color

/**
 * Encapsulates visual style options for a series.
 */
data class PlotStyle(
    val color: Color = Color.BLUE,
    val lineWidth: Float = 2.0f,
    val showPoints: Boolean = false,
    val pointRadius: Int = 3
) {
    fun toStroke(): BasicStroke = BasicStroke(lineWidth)
}