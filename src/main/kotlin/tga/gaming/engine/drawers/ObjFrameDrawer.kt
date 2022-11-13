package tga.gaming.engine.drawers

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.model.CompositeDrawer
import tga.gaming.engine.model.Drawer
import tga.gaming.engine.model.Obj

class ObjFrameDrawer(
    override val obj: Obj,
    private val strokeStyle: String
) : Drawer {
    override fun draw(ctx: CanvasRenderingContext2D) {
        val f = obj.frame ?: return
        val p0 = f.p0
        val p1 = f.p1

        ctx.beginPath()
        ctx.strokeStyle = strokeStyle
        ctx.rect(p0.x, p0.y, p1.x-p0.x, p1.y-p0.y)
        ctx.stroke()
    }
}

fun CompositeDrawer.withObjFrameDrawer(strokeStyle: String = "aquamarine") {
    this.drawers.add(ObjFrameDrawer(this as Obj, strokeStyle))
}
