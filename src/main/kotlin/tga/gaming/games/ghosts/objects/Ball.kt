package tga.gaming.games.ghosts.objects

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.PI2
import tga.gaming.engine.drawers.withCircleDrawer
import tga.gaming.engine.drawers.withObjFrameDrawer
import tga.gaming.engine.model.*

class Ball(
    p: Vector,
    val speed: Vector,
    acceleration: Double = 3.5
): Obj(p = p, r = 15.0), CompositeDrawer, Moveable, Actionable {

    override val drawers = ArrayList<Drawer>(1)
    private var turnsToLive = 100
    private val accelerationVector = speed.norm() * acceleration

    private val len = 10
    private val tailX = DoubleArray(len){p.x}
    private val tailY = DoubleArray(len){p.y}
    private val tailLife = DoubleArray(len){1.0}
    private val tailLifeDecrease = 0.1
    private var headIndex = 0

    init {
        withCircleDrawer(radius = r, strokeStyle = "yellow")
        withObjFrameDrawer(strokeStyle = "red")
    }

    override fun draw(ctx: CanvasRenderingContext2D) {
        ctx.strokeStyle = "orange"
        ctx.lineWidth = 2.5

        var  i = headIndex+1
        repeat(len) {
            i--; if (i == -1) i = len-1

            val grd = ctx.createRadialGradient(tailX[i], tailY[i], 0.0, tailX[i], tailY[i], r*tailLife[i])
            grd.addColorStop(0.0, "white")
            grd.addColorStop(0.4, "orange")
            grd.addColorStop(0.98, "transparent")

            ctx.beginPath()
            ctx.fillStyle = grd
            ctx.arc(tailX[i], tailY[i], r*tailLife[i], 0.0, PI2)
            ctx.fill()
            //ctx.stroke()
        }

        super.draw(ctx)
    }

    override fun move() {
        p += speed
        speed += accelerationVector

        headIndex--; if (headIndex == -1) headIndex = len-1
        tailX[headIndex] = p.x
        tailY[headIndex] = p.y
        tailLife[headIndex] = 1.0
    }

    override fun act() {
        turnsToLive--
        if (turnsToLive <= 0) { dispatcher.delObj(this); return }

        dispatcher.index.objectsOnTheSamePlaceWith(this)
            .forEach { ghost ->
                if (ghost is Ghost) {
                    ghost.disappear()
                }
            }

        for (i in tailLife.indices) tailLife[i] -= tailLifeDecrease
    }
}