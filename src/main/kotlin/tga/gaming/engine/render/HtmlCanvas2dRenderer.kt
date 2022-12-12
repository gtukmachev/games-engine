package tga.gaming.engine.render

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import tga.gaming.engine.camera.Camera
import tga.gaming.engine.dispatcher.GameObjects
import tga.gaming.engine.model.Drawable

class HtmlCanvas2dRenderer(
    val canvas: HTMLCanvasElement,
    val gameObjects: GameObjects,
    val camera: Camera
) : GameRenderer {

    val ctx: CanvasRenderingContext2D = canvas.getContext("2d")!! as CanvasRenderingContext2D
    val width = canvas.width
    val height = canvas.height

    override fun paint() {
        canvas.width = canvas.width

        gameObjects.objects.forEach {
            if (it is Drawable) {
                ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
                ctx.scale(camera.xScale, camera.yScale)
                ctx.translate(-camera.visibleWordFrame.p0.x, -camera.visibleWordFrame.p0.y)
                if (it.angle != 0.0) ctx.rotate(it.angle)
                it.draw(ctx)
            }
        }
    }
}
