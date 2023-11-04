package tga.gaming.engine.render

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import tga.gaming.engine.camera.Camera
import tga.gaming.engine.dispatcher.GameObjects
import tga.gaming.engine.model.Drawable
import tga.gaming.engine.stat.CommonMetrics

class HtmlCanvas2dRenderer(
    val canvas: HTMLCanvasElement,
    val gameObjects: GameObjects,
    val camera: Camera,
    val isAbsolutePositioning: Boolean = true
) : GameRenderer {

    val ctx: CanvasRenderingContext2D = canvas.getContext("2d")!! as CanvasRenderingContext2D
    val width = canvas.width
    val height = canvas.height

    override fun paint() {
        CommonMetrics.FPS.add(1)
        canvas.width = canvas.width

        var n = 0
        var e = 0


        gameObjects.objects.forEach {
            try {
                if (it is Drawable) {
                    if (it.isAlwaysVisible || camera.isInVisibleArea(it)) {
                        n++
                        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
                        ctx.scale(camera.xScale, camera.yScale)
                        ctx.translate(-camera.visibleWordFrame.p0.x, -camera.visibleWordFrame.p0.y)
                        it.draw(ctx)
                    }
                }
            } catch (t: Throwable) {
                e++
                console.error(t)
            }
        }

        CommonMetrics.paintObjectsMetric.add(n)
        CommonMetrics.paintErrorsMetric.add(e)

        if (isDebugUiEnabled) renderDebugUI()
    }

    private fun renderDebugUI() {
        renderLogs()
    }

    private fun renderLogs() {
        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
        logY = 3.0

        debug(      CommonMetrics.FPS.aggregateAsString())
        debug(       CommonMetrics.TPS.aggregateAsString())
        debug("---------------------------------------------------")
        debug(CommonMetrics.paintObjectsMetric.aggregateAsString())
        debug( CommonMetrics.paintErrorsMetric.aggregateAsString())


        warn("camera.visibleWordFrame = ${camera.visibleWordFrame} w=${camera.visibleWordFrame.width} h=${camera.visibleWordFrame.height}")
        warn("camera.activeWordZone = ${camera.activeWordZone} w=${camera.activeWordZone.width} h=${camera.activeWordZone.height}")
        debug("camera.scale { x=${camera.xScale}, y=${camera.yScale} }")
        info("camera.screenFrame = ${camera.screenFrame} w=${camera.screenFrame.width} h=${camera.screenFrame.height}")

    }

    override var isDebugUiEnabled: Boolean = false

    private var logY = 5.0
    private val infoYInc = 16.0
    private fun info(text: String)  { ctx.fillStyle = "lightgreen"; log(text) }
    private fun debug(text: String) { ctx.fillStyle = "white";      log(text) }
    private fun warn(text: String)  { ctx.fillStyle = "#EAE791FF";  log(text) }
    private fun error(text: String) { ctx.fillStyle = "#FF6C6CFF";  log(text) }

    private fun log(text: String) {
        logY += infoYInc
        ctx.font = "0.8em Arial"
        ctx.fillText(text, 10.0, logY)
    }
}
