package tga.gaming.games.wiggly_worm

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.PI2
import tga.gaming.engine.model.CompositeDrawer
import tga.gaming.engine.model.Drawer
import tga.gaming.engine.model.Obj
import tga.gaming.engine.model.Vector

class Food(
    p: Vector,
    r: Double = 10.0
) : Obj(p = p, r = r), CompositeDrawer {

    override val drawers = ArrayList<Drawer>(1)

    val initRadius = r

    override fun draw(ctx: CanvasRenderingContext2D) {
        ctx.beginPath()
        ctx.strokeStyle = "#EABE91FF"
        ctx.lineWidth = 5.0
        ctx.arc(0.0, 0.0, r, 0.0, PI2)
        ctx.fillStyle = "#FF6C30FF"
        ctx.fill()
        ctx.stroke()

        super.draw(ctx)
    }
}

