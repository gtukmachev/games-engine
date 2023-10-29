package tga.gaming.engine.drawers

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.PI2
import tga.gaming.engine.model.CompositeDrawer
import tga.gaming.engine.model.Drawer
import tga.gaming.engine.model.Obj
import kotlin.math.cos
import kotlin.math.sin


inline fun <reified T: CompositeDrawer> T.withObjFrameDrawer(strokeStyle: String = "aquamarine"): T {
    this.drawers.add(ObjFrameDrawer(this as Obj, strokeStyle))
    return this
}

fun CompositeDrawer.addObjFrameDrawer(strokeStyle: String = "aquamarine") {
    this.drawers.add(ObjFrameDrawer(this as Obj, strokeStyle))
}


class ObjFrameDrawer(
    override val obj: Obj,
    private val strokeStyle: String
) : Drawer {
    override fun draw(ctx: CanvasRenderingContext2D) {
        val f = obj.frame ?: return
        val p = obj.p
        val p0 = f.p0
        val p1 = f.p1

        ctx.save()
        //ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
        ctx.beginPath()
        ctx.strokeStyle = strokeStyle
        ctx.rect(p.x+p0.x, p.y+p0.y, p1.x-p0.x, p1.y-p0.y)
        ctx.stroke()

        ctx.beginPath()
        ctx.arc(p.x, p.y, obj.r, 0.0, PI2)
        ctx.stroke()

        val angleX = cos(obj.angle) * obj.r
        val angleY = sin(obj.angle) * obj.r

        ctx.beginPath()
        ctx.moveTo(p.x, p.y)
        ctx.lineTo(p.x+angleX, p.y+angleY)
        ctx.stroke()

        ctx.restore()
    }
}
