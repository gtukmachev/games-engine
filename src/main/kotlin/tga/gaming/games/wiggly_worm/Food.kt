package tga.gaming.games.wiggly_worm

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.PI2
import tga.gaming.engine.model.*
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

class Food(
    p: Vector,
    r: Double = Random.nextDouble(15.0, 50.0),
    val electricCharge: Boolean = Random.nextBoolean()
) : Obj(p = p, r = r), CompositeDrawer, Actionable, Moveable {

    override val drawers = ArrayList<Drawer>(1)

    val speed = v()
    private val color = colors[Random.nextInt(colors.size)]

    var t: Double = Random.nextDouble(PI/2)
    private val dt = 0.04
    private val rk: Double = 0.5 + Random.nextDouble(0.5)
    private var visibleRadius: Double = r

    override fun act() {
        t += dt
        val sint = sin(t)
        visibleRadius = r + r*rk*sint*sint
    }

    override fun move() {
        p += speed
        if (p.x < wArea.p0.x && speed.x < 0) { speed.x = -speed.x }
        if (p.x > wArea.p1.x && speed.x > 0) { speed.x = -speed.x }

        if (p.y < wArea.p0.y && speed.y < 0) { speed.y = -speed.y }
        if (p.y > wArea.p1.y && speed.y > 0) { speed.y = -speed.y }
    }

    override fun draw(ctx: CanvasRenderingContext2D) {

        val grd = ctx.createRadialGradient(p.x, p.y, 0.0, p.x, p.y, visibleRadius)
        grd.addColorStop(0.0, "white")
        grd.addColorStop(0.4, color)
        grd.addColorStop(0.95, "transparent")

        ctx.beginPath()
        ctx.arc(p.x, p.y, visibleRadius, 0.0, PI2)
        ctx.fillStyle = grd
        ctx.fill()

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

