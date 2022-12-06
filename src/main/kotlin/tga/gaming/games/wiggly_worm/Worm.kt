package tga.gaming.games.wiggly_worm

import org.w3c.dom.BEVEL
import org.w3c.dom.CanvasLineJoin
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.Path2D
import tga.gaming.engine.*
import tga.gaming.engine.model.*
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

class Worm(
    p: Vector,
    var fillStyles: Array<String>,
    var strokeStyles: Array<String>,
    val electricCharge: Boolean = Random.nextBoolean()
): Obj(p=p, r=8.0),
    CompositeDrawer, Moveable, Actionable, CompositeMover
{
    override val drawers = ArrayList<Drawer>()
    override val movers = ArrayList<Mover>()

    private val desiredBodyLength: Int get() = (r * 7).toInt() - 20

    var da: Double = d *10
    val body: MutableList<Vector> = ArrayList<Vector>().apply {
        repeat(desiredBodyLength){ add(p.copy()) }
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

            food.speed += distance.norm() * k
        }


        val toEat = (this.r + food.r) - distance.len
        if ( toEat <= 0 ) return


        food.r = distance.len - this.r
        if (food.r <= 0.0) {
            food.r = 0.0
            dispatcher.delObj(food)
            dispatcher.addFood()
        }

        this.r += (toEat / food.initRadius) * 0.1
        this.frame!!.p0.set(-r,-r)
        this.frame.p1.set( r, r)

        while (desiredBodyLength > body.size) body.add( body.last().copy() - v(1,1) )

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
        drawEyes(ctx)
        //drawMover(ctx)
        super.draw(ctx)
    }

/*
    private fun drawMover(ctx: CanvasRenderingContext2D) {
        movers.asSequence()
            .filter { it is ConstantSpeedMover  }
            .forEach { (it as ConstantSpeedMover).draw(ctx) }
    }
*/

    private fun drawSimpleWarm(ctx: CanvasRenderingContext2D) {
        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
        ctx.lineWidth = r/10
        ctx.lineJoin = CanvasLineJoin.BEVEL

        for(i in body.size - 1 downTo 0 ) {
            ctx.strokeStyle = strokeStyles[i %  strokeStyles.size]
            ctx.fillStyle = fillStyles[i % fillStyles.size]

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

    }

    private fun drawEyes(ctx: CanvasRenderingContext2D) {

        val d = p - body[1]

        ctx.lineJoin = CanvasLineJoin.BEVEL
        ctx.strokeStyle = strokeStyles[0]
        ctx.lineWidth = 1.5

        val baseAngle = d.norm().angle()
        val tr1 = r / 4
        val cX = tr1  * 3

        draw1Eye(ctx, baseAngle,  eyeAngle, tr1, cX)
        draw1Eye(ctx, baseAngle, eyeAnglem, tr1, cX)
    }

    private fun draw1Eye(ctx: CanvasRenderingContext2D, baseAngle: Double, angle: Double, tr1: Double, cX: Double) {

        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
        ctx.translate(p.x, p.y)
        ctx.rotate(baseAngle + angle)
        ctx.translate(cX, 0.0)
        ctx.fillStyle = "white"
        ctx.beginPath()
        ctx.arc(x = 0.0, y = 0.0, radius = tr1, startAngle = 0.0, endAngle = PI2)
        ctx.fill()
        ctx.stroke()


        ctx.fillStyle = "black"
        val rIn = tr1/2
        val cIn = tr1/2
        ctx.beginPath()
        ctx.rotate(-angle)
        ctx.translate(cIn, 0.0)
        ctx.arc(x = 0.0, y = 0.0, radius = rIn, startAngle = 0.0, endAngle = PI2)
        ctx.fill()

    }

    private fun drawWarm(ctx: CanvasRenderingContext2D) {
        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)

        ctx.lineWidth = 5.0
        ctx.lineJoin = CanvasLineJoin.BEVEL

        val path = Path2D()

        ctx.beginPath()
        val centerFirst = body[0]
        val centerSecond = body[1]
        val angleSecondToFirst = (centerFirst - centerSecond).norm().angle()
        val aStartFirst = - pi2_3 + angleSecondToFirst
        val aFinishFirst = pi2_3 + angleSecondToFirst

        // head
        path.arc(
            x = centerFirst.x, y = centerFirst.y, radius = r,
            startAngle = aStartFirst,
            endAngle = aFinishFirst,
        )

        // top line
        var aOldEnd = aFinishFirst
        for(i in 1 until body.size-1) {
            val center = body[i]
            val nexCenter = body[i+1]

            val aStart = aOldEnd -pi_3

            val vec = (center - nexCenter).norm()
            val aEnd =  pi2_3 + vec.angle()

            path.arc(x = center.x, y = center.y, radius = r, startAngle = aStart, endAngle = aEnd)
            aOldEnd = aEnd
        }

        // tail
        val centerLast = body[body.size-1]
        val aStartLast = aOldEnd - pi_3
        val aFinishLast = aStartLast + pi4_3
        path.arc(
            x = centerLast.x, y = centerLast.y, radius = r,
            startAngle = aStartLast,
            endAngle = aFinishLast,
        )

        // bottom line
        aOldEnd = aFinishLast
        for(i in body.size-2 downTo 1 ) {
            val center = body[i]
            val prevCenter = body[i-1]


            val aStart = aOldEnd - pi_3

            val vec = (prevCenter-center).norm()
            val a = vec.angle()
            val aEnd = pi5_3 + a

            path.arc(x = center.x, y = center.y, radius = r, startAngle = aStart, endAngle = aEnd)
            aOldEnd = aEnd
        }

/*

        path.arc(
            x = bs.x, y = bs.y, radius = r,
            startAngle = `2 pi / 3`,
            endAngle = 0.0,
            anticlockwise = true
        )

*/
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