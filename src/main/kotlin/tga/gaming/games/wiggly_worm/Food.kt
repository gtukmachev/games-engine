package tga.gaming.games.wiggly_worm

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.PI2
import tga.gaming.engine.model.CompositeDrawer
import tga.gaming.engine.model.Drawer
import tga.gaming.engine.model.Obj
import tga.gaming.engine.model.Vector
import kotlin.random.Random

class Food(
    p: Vector,
    r: Double = 10.0
) : Obj(p = p, r = r), CompositeDrawer {

    override val drawers = ArrayList<Drawer>(1)

    val initRadius = r
    val color = colors[Random.nextInt(colors.size)]

    override fun draw(ctx: CanvasRenderingContext2D) {

        var grd = ctx.createRadialGradient(0.0, 0.0, 0.0, 0.0, 0.0, r);
        grd.addColorStop(0.0, "white")
        grd.addColorStop(0.4, color)
        grd.addColorStop(0.95, "transparent")

        ctx.beginPath()
        //ctx.strokeStyle = "#EABE91FF"
        ctx.lineWidth = 5.0
        ctx.arc(0.0, 0.0, r, 0.0, PI2)
        ctx.fillStyle = grd //"#FF6C30FF"
        ctx.fill()
        //ctx.stroke()

        super.draw(ctx)
    }

    companion object {
        val colors = arrayOf(
            "#FFF96CFF",
            "#A7FF7BFF",
            "#8C00FFFF",
            "#FF1F8AFF",
            "#29B4FFFF",
            "#FF5630FF",
            "#FFFA43FF",
            "#0066A4FF",
            "#3B2EFFFF",
            "#7BFF2EFF",
        )
    }
}

