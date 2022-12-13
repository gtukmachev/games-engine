package tga.gaming.engine.render

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import tga.gaming.engine.camera.Camera
import tga.gaming.engine.dispatcher.GameObjects
import tga.gaming.engine.model.Drawable
import tga.gaming.engine.stat.Metric

class HtmlCanvas2dRenderer(
    val canvas: HTMLCanvasElement,
    val gameObjects: GameObjects,
    val camera: Camera
) : GameRenderer {

    val ctx: CanvasRenderingContext2D = canvas.getContext("2d")!! as CanvasRenderingContext2D
    val width = canvas.width
    val height = canvas.height

    val paintObjectsMetric = Metric("painted objects per Frame")
    val paintErrorsMetric = Metric("paint errors per Frame")
    val framesMetric = Metric("frames per second")


    override fun paint() {
        framesMetric.add(1)
        canvas.width = canvas.width

        var n = 0
        var e = 0
        gameObjects.objects.forEach {
            try {
                if (it is Drawable /*&& camera.isInVisibleArea(it)*/) {
                        n++
                        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
                        ctx.scale(camera.xScale, camera.yScale)
                        ctx.translate(-camera.visibleWordFrame.p0.x, -camera.visibleWordFrame.p0.y)
                        if (it.angle != 0.0) ctx.rotate(it.angle)
                        it.draw(ctx)
                }
            } catch (t: Throwable) {
                e++
                console.error(t)
            }
        }

        paintObjectsMetric.add(n)
        paintErrorsMetric.add(e)


        renderLogs()
    }

    private fun renderLogs() {
        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
        logY = 0.0

        info(paintObjectsMetric.getForSeveralLastSecondsAsString(5))
        info( paintErrorsMetric.getForSeveralLastSecondsAsString(5))
        warn(      framesMetric.getForSeveralLastSecondsAsString(5))
        error("camera { xScale=${camera.xScale}, yScale=${camera.yScale} }")

    }


    private var logY = 0.0
    private val infoYInc = 20.0
    private fun info(text: String)  { ctx.fillStyle = "lightgreen";   ctx.strokeStyle = "lightgreen";  log(text) }
    private fun debug(text: String) { ctx.fillStyle = "white";        ctx.strokeStyle = "white";       log(text) }
    private fun warn(text: String)  { ctx.fillStyle = "lightyellow";  ctx.strokeStyle = "lightyellow"; log(text) }
    private fun error(text: String) { ctx.fillStyle = "#FF6C6CFF";    ctx.strokeStyle = "#FF6C6CFF";   log(text)  }

    private fun log(text: String) {
        logY += infoYInc
        ctx.font = "1em Arial"
        ctx.fillText(text, 10.0, logY)
        ctx.strokeText(text, 10.0, logY)
    }
}
