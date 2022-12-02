package tga.gaming.engine.drawers

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.PI2
import tga.gaming.engine.model.CompositeDrawer
import tga.gaming.engine.model.Drawer
import tga.gaming.engine.model.Obj


inline fun <reified T: CompositeDrawer> T.withCircleDrawer(radius: Int, color: String): T {
    this.drawers.add(
        CircleDrawer(this as Obj, radius.toDouble(), color)
    )
    return this
}

class CircleDrawer(
    override val obj: Obj,
    val radius: Double,
    val color: String
) : Drawer {

    override fun draw(ctx: CanvasRenderingContext2D) {
        ctx.beginPath()
        ctx.strokeStyle = color
        ctx.arc(x = 0.0, y = 0.0, radius = radius, startAngle = 0.0, endAngle = PI2)
        ctx.stroke()
    }

}