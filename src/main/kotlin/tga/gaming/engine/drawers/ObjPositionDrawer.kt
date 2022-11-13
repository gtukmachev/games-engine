package tga.gaming.engine.drawers

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.model.CompositeDrawer
import tga.gaming.engine.model.Drawer
import tga.gaming.engine.model.Obj
import kotlin.math.PI

class ObjPositionDrawer(
    override val obj: Obj,
    private val radius: Double,
    private val strokeStyle: String
) : Drawer {
    override fun draw(ctx: CanvasRenderingContext2D) {
        val f = obj.p

        ctx.beginPath()
        ctx.strokeStyle = strokeStyle
        ctx.arc(0.0, 0.0, radius, 0.0, PI2)
        ctx.stroke()
    }

    companion object {
        const val PI2 = PI * 2
    }
}

fun CompositeDrawer.withObjPositionDrawer(radius: Double = 5.0, strokeStyle: String = "aquamarine") {
    this.drawers.add(ObjPositionDrawer(this as Obj, radius, strokeStyle))
}
