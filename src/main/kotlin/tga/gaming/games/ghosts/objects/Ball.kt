package tga.gaming.games.ghosts.objects

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.PI2
import tga.gaming.engine.model.*

class Ball(
    p: Vector,
    val speed: Vector,
    acceleration: Double = 3.5
): Obj(p = p, r = 15.0), Moveable, Drawable, Actionable {

    private var turnsToLive = 100
    private val accelerationVector = speed.norm() * acceleration

    private val len = 10
    private val tailX = DoubleArray(len){p.x}
    private val tailY = DoubleArray(len){p.y}
    private val tailLife = DoubleArray(len){1.0}
    private val tailLifeDecrease = 0.1
    private var headIndex = 0


    override fun draw(ctx: CanvasRenderingContext2D) {
        drawWithOpacity(ctx)
        //drawAsChain(ctx)
    }


    fun drawWithOpacity(ctx: CanvasRenderingContext2D) {
        ctx.strokeStyle = "orange"
        ctx.lineWidth = 2.5

        var tailIndex = headIndex-1; if (tailIndex == -1) tailIndex = len-1

        val center = v( tailX[tailIndex], tailY[tailIndex])
        val n = 100
        val step = (p - center) / n

        ctx.globalAlpha = 0.1
        repeat(n) {
            ctx.drawBall(center)
            center += step
        }

        ctx.globalAlpha = 0.99
        ctx.drawBall(p)

//        ctx.beginPath()
//        ctx.strokeStyle = "red"
//        ctx.moveTo(tailX[tailIndex], tailY[tailIndex])
//        ctx.lineTo(p.x, p.y)
//        ctx.stroke()

    }

    fun CanvasRenderingContext2D.drawBall(center: Vector) {
        val ctx = this
        val grd = ctx.createRadialGradient(center.x, center.y, 0.0, center.x, center.y, r)
        grd.addColorStop(0.0, "white")
        grd.addColorStop(0.4, "orange")
        grd.addColorStop(0.98, "transparent")

        ctx.beginPath()
        ctx.fillStyle = grd
        ctx.arc(center.x, center.y, r, 0.0, PI2)
        ctx.fill()
    }


    fun drawAsChain(ctx: CanvasRenderingContext2D) {
        ctx.strokeStyle = "orange"
        ctx.lineWidth = 2.5

        var  i = headIndex-1; if (i == -1) i = len-1
        val start = v(tailX[i], tailY[i])
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
        }

        ctx.beginPath()
        ctx.strokeStyle = "red"
        ctx.moveTo(start.x, start.y)
        ctx.lineTo(p.x, p.y)
        ctx.stroke()

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