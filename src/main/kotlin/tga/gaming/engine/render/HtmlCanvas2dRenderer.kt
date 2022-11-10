package tga.gaming.engine.render

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement

class HtmlCanvas2dRenderer(
    val canvas: HTMLCanvasElement
) : GameRenderer {

    val ctx: CanvasRenderingContext2D = canvas.getContext("2d")!! as CanvasRenderingContext2D
    val width = canvas.width
    val height = canvas.height

    override fun paint() {
        ctx.beginPath()
        ctx.rect( 1.0, 1.0, width - 1.0, height - 1.0 )
        ctx.strokeStyle = "orange"
        ctx.stroke();
    }
}
