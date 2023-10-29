package tga.gaming.engine.drawers

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.PI2
import tga.gaming.engine.model.CompositeDrawer
import tga.gaming.engine.model.Drawer
import tga.gaming.engine.model.Obj

class CircleDrawer(
    override val obj: Obj,
    private val radius: Double,
    private val strokeStyle: String,
    private val lineWidth: Double = 1.0,
    private val fillStyle: String? = null
) : Drawer {
    override fun draw(ctx: CanvasRenderingContext2D) {
        ctx.beginPath()
        ctx.strokeStyle = strokeStyle
        ctx.lineWidth = lineWidth
        ctx.arc(obj.p.x, obj.p.y, radius, 0.0, PI2)
        fillStyle?.let{
            ctx.fillStyle = fillStyle
            ctx.fill()
        }
        ctx.stroke()
    }
}

inline fun <reified T : CompositeDrawer> T.withCircleDrawer(
    radius: Double,
    strokeStyle: String = "aquamarine",
    lineWidth: Double = 1.0,
    fillStyle: String? = null
): T {
    this.drawers.add(CircleDrawer(this as Obj, radius, strokeStyle, lineWidth, fillStyle))
    return this
}

inline fun <reified T : CompositeDrawer> T.withCircleDrawer(
    radius: Int,
    strokeStyle: String = "aquamarine",
    lineWidth: Double = 1.0,
    fillStyle: String? = null
): T {
    return withCircleDrawer(radius.toDouble(), strokeStyle, lineWidth, fillStyle)
}
