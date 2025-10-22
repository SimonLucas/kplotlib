package sml.plotlib

import java.awt.Graphics
import javax.swing.JPanel
import javax.swing.Timer

/**
 * A panel that displays and animates a Plot at a fixed frame rate.
 * The onFrame callback can modify multiple series in the Plot each frame.
 */
class AnimatedPlotPanel(
    private val plot: Plot,
    fps: Int = 20,
    private val onFrame: (frame: Int, plot: Plot) -> Unit
) : JPanel() {

    private var frameCount = 0
    private val renderer = Renderer(plot)
    private val timer = Timer(1000 / fps) {
        frameCount++
        onFrame(frameCount, plot)
        repaint()
    }

    fun start() = timer.start()
    fun pause() = timer.stop()
    fun resume() = timer.start()
    fun stop() = timer.stop()

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        renderer.draw(g as java.awt.Graphics2D, width, height)
    }

    init {
        start()
    }
}
