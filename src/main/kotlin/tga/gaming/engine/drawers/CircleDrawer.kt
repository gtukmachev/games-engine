package tga.gaming.engine.drawers

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.PI2
import tga.gaming.engine.model.CompositeDrawer
import tga.gaming.engine.model.Drawer
import tga.gaming.engine.model.Obj

class CircleDrawer(
    override val obj: Obj,
    private val radius: Double,
    private val strokeStyle: String
) : Drawer {
    override fun draw(ctx: CanvasRenderingContext2D) {
        ctx.beginPath()
        ctx.strokeStyle = strokeStyle
        ctx.arc(0.0, 0.0, radius, 0.0, PI2)
        ctx.stroke()
    }

}

inline fun <reified T: CompositeDrawer> T.withCircleDrawer(radius: Int, strokeStyle: String = "aquamarine"): T {
    this.drawers.add(CircleDrawer(this as Obj, radius.toDouble(), strokeStyle))
    return this
}
