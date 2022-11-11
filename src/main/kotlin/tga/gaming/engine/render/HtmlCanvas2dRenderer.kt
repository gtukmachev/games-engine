package tga.gaming.engine.render

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import tga.gaming.engine.dispatcher.GameObjects
import tga.gaming.engine.model.Drawable

class HtmlCanvas2dRenderer(
    val canvas: HTMLCanvasElement,
    val gameObjects: GameObjects
) : GameRenderer {

    val ctx: CanvasRenderingContext2D = canvas.getContext("2d")!! as CanvasRenderingContext2D
    val width = canvas.width
    val height = canvas.height

    override fun paint() {
        canvas.width = canvas.width

        gameObjects.objects.forEach {
            if (it is Drawable) {
                ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
                ctx.translate(it.p.x, it.p.y)
                if (it.scale != 1.0) ctx.scale(it.scale, it.scale)
                if (it.angle != 0.0) ctx.rotate(it.angle)
                it.draw(ctx)
            }
        }

        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
    }
}
