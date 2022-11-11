package tga.gaming.engine.shapes

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.model.Drawable
import tga.gaming.engine.model.Obj
import tga.gaming.engine.model.Vector

open class Rect(
    p: Vector,
    r: Double,
) : Obj(p=p, r=r), Drawable {

    override fun draw(ctx: CanvasRenderingContext2D) {
        ctx.strokeStyle = "aquamarine"
        ctx.rect(-r, -r, 2*r, 2*r)
        ctx.stroke()
    }
}
