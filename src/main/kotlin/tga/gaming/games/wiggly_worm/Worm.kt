package tga.gaming.games.wiggly_worm

import org.w3c.dom.BEVEL
import org.w3c.dom.CanvasLineJoin
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.Path2D
import tga.gaming.engine.PI2
import tga.gaming.engine.model.*
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

class Worm(
    p: Vector,
    var fillStyle: String = "#BF8360",
    var strokeStyle: String = "#8C4830",
    val electricCharge: Boolean = Random.nextBoolean()
): Obj(p=p, r=20.0),
    CompositeDrawer, Moveable, Actionable, CompositeMover
{
    override val drawers = ArrayList<Drawer>()
    override val movers = ArrayList<Mover>()

    var da: Double = d *10
    val body: MutableList<Vector> = ArrayList<Vector>().apply {
        repeat(10){ add(p.copy()) }
    }

    init {
        positions()

    }
    private fun positions() {
        var center = p.copy()
        var offset = v(-r, 0.0)

        for (i in 0 until body.size) {
            body[i].set(center)
            center = center + offset
        }

    }

    private fun eat(food: Food) {
        val distance = food.p - this.p


        if ( distance.len < (this.r * 3) ) {
            val k = when (electricCharge) {
                food.electricCharge ->   0.05
                else                -> - 0.05
            }

            food.speed += distance.copy().norm() * k
        }


        val toEat = (this.r + food.r) - distance.len
        if ( toEat <= 0 ) return


        food.r = distance.len - this.r
        if (food.r <= 0.0) {
            food.r = 0.0
            dispatcher.delObj(food)
            dispatcher.addFood()
        }

        this.r += (toEat / food.initRadius) * 0.3
        this.frame!!.p0.set(-r,-r)
        this.frame!!.p1.set( r, r)

        val ri = r.toInt() - 20
        val desiredBodyLength = 10 + (ri/7)
        while (desiredBodyLength > body.size) body.add( body.last().copy() )

    }


    override fun act() {
        dispatcher.index.objectsOnTheSamePlaceWith(this)
            .filter { it is Food }
            .forEach {
                eat(it as Food)
            }

    }


    /*
        private fun positions() {
            var center = p.copy()
            val rotateTo = -da
            var a = PI

            for (i in 0 until body.size) {
                body[i].set(center)
                val offset = v(cos(a), sin(a)) * r
                center = center + offset
                a += rotateTo
            }

        }
    */

    override fun move() {
        super.move()

        body[0] = p
        for (i in 1 until body.size) {
            val prev = body[i-1]
            val curr = body[i]
            val backV = (curr - prev).assignLength(r)
            body[i] = prev + backV
        }
    }

    var t: Double = 0.0
    var dt: Double = 0.005
    private fun wigle() {
        t += dt

        val k = sin(t)
        da = (d * 30) * k

        positions()
    }


    override fun draw(ctx: CanvasRenderingContext2D) {
        //drawWarm(ctx)
        drawSimpleWarm(ctx)
        super.draw(ctx)
    }

    private fun drawSimpleWarm(ctx: CanvasRenderingContext2D) {
        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
        ctx.lineWidth = 2.9
        ctx.lineJoin = CanvasLineJoin.BEVEL
        ctx.strokeStyle = strokeStyle
        ctx.fillStyle = fillStyle

        for(i in body.size - 1 downTo 0 ) {
            val b = body[i]
            ctx.beginPath()
            ctx.arc(
                x = b.x, y = b.y, radius = r,
                startAngle = 0.0,
                endAngle = PI2
            )
            ctx.fill()
            ctx.stroke()
        }

        drawEyes(ctx)
    }

    private fun drawEyes(ctx: CanvasRenderingContext2D) {

        val d = p - body[1]
        val t1 = d / 4.0 * 3.0
        val tr =  d.len / 4.0

        ctx.lineWidth = 1.5
        ctx.lineJoin = CanvasLineJoin.BEVEL
        ctx.strokeStyle = strokeStyle
        ctx.fillStyle = "white"


        draw1Eye(ctx,  eyeAngle, t1, tr)
        draw1Eye(ctx, eyeAnglem, t1, tr)
    }

    private fun draw1Eye(ctx: CanvasRenderingContext2D, angle: Double, t1: Vector, tr: Double) {
        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
        ctx.translate(p.x, p.y)
        ctx.rotate(angle)
        ctx.beginPath()
        ctx.arc(
            x = t1.x, y = t1.y, radius = tr,
            startAngle = 0.0,
            endAngle = PI2
        )
        ctx.fill()
        ctx.stroke()
    }

    private fun drawWarm(ctx: CanvasRenderingContext2D) {
        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)

        val `2 pi / 3` = 2.0* PI /3
        val `pi / 3` = PI /3

        ctx.lineWidth = 5.0
        ctx.lineJoin = CanvasLineJoin.BEVEL

        val path = Path2D()

        ctx.beginPath()
        val bs = body.first()
        path.arc(
            x = bs.x, y = bs.y, radius = r,
            startAngle = `2 pi / 3`,
            endAngle = -`2 pi / 3`,
            anticlockwise = true
        )

        var a = 0.0

        for(i in 1 until body.size-1) {
            val b = body[i]
            path.arc(
                x = b.x, y = b.y, radius = r,
                startAngle = -`pi / 3` - a,
                endAngle = -`2 pi / 3` - (a+da),
                anticlockwise = true
            )
            a += da
        }

        val be = body.last()
        path.arc(
            x = be.x, y = be.y, radius = r,
            startAngle = - `pi / 3` - a,
            endAngle = `pi / 3` - a,
            anticlockwise = true
        )

        for(i in body.size-2 downTo 1 ) {
            val b = body[i]
            path.arc(
                x = b.x, y = b.y, radius = r,
                startAngle = `2 pi / 3` - a,
                endAngle = `pi / 3` - (a-da),
                anticlockwise = true
            )
            a -= da
        }

        path.arc(
            x = bs.x, y = bs.y, radius = r,
            startAngle = `2 pi / 3`,
            endAngle = 0.0,
            anticlockwise = true
        )

        ctx.strokeStyle = "#D8B08C"
        ctx.fillStyle = "#0F6466"
        ctx.fill(path)
        ctx.stroke(path)
    }


    companion object {
        val eyeAngle = PI / 8
        val eyeAnglem = -eyeAngle
    }
}