package tga.gaming.games.wiggly_worm

import org.w3c.dom.BEVEL
import org.w3c.dom.CanvasLineJoin
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.Path2D
import tga.gaming.engine.*
import tga.gaming.engine.model.*
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import kotlin.math.PI
import kotlin.math.log
import kotlin.random.Random

abstract class Worm(
    p: Vector,
    val initialRadius: Double,
    var fillStyles: Array<String>,
    var strokeStyles: Array<String>,
    private val electricCharge: Boolean = Random.nextBoolean()
): Obj(p=p),
    CompositeDrawer, Moveable, Actionable, CompositeMover
{
    var game: GameWord? = null
    override val drawers = ArrayList<Drawer>()
    override val movers = ArrayList<Mover>()

    private var actionDistance: Double = initialRadius
    override var r: Double = initialRadius
        set(value) {
            field = value
            desiredBodyLength = calculateWormLength(r)
            correctFrame()
        }

    private fun correctFrame() {
        frame?.apply{
            actionDistance = r + 10 * log(r,2.0)
            p0.set(-r-actionDistance, -r-actionDistance)
            p1.set(+r+actionDistance, +r+actionDistance)
        }
    }

    var desiredBodyLength: Int = calculateWormLength(initialRadius); private set
    val body: MutableList<Vector> = ArrayList<Vector>(desiredBodyLength).apply {
        repeat(desiredBodyLength){ add(p.copy()) }
    }

    init {
        correctFrame()
    }

    private fun eat(food: Food) {
        val distance = food.p - this.p

        if ( distance.len < (this.r + actionDistance) ) {
            val k = when (electricCharge) {
                food.electricCharge ->   0.05
                else                -> - 0.05
            }
            food.speed += distance.norm() * k
        }

        val radiusToEat = (this.r + food.r) - distance.len
        if ( radiusToEat <= 0 ) return


        food.r = distance.len - this.r
        if (food.r <= 0.0) {
            food.r = 0.0
            dispatcher.delObj(food)
            dispatcher.addFood()
        }

        this.r += radiusToEat * snakeRadiusIncreasePerOneFoodItem

        while (desiredBodyLength > body.size) {
            increaseWormBodyLength()
        }

        game?.let{ scaleGameView(it) }
    }

    private fun scaleGameView(gameWord: GameWord) {
        with((gameWord.renderer as HtmlCanvas2dRenderer).camera) {
            changeScaleTo(initialRadius / r)
        }
    }

    open fun increaseWormBodyLength() {
        body.add(body.last().copy() - v(0.01, 0.01))
    }

    override fun act() {
        dispatcher.index.objectsOnTheSamePlaceWith(this)
            .filter { it is Food }
            .forEach {
                eat(it as Food)
            }

    }

    override fun move() {
        super.move()
        moveWormBody()
    }

    abstract fun moveWormBody()

    override fun draw(ctx: CanvasRenderingContext2D) {
        //drawWarmWithStroke(ctx)
        drawWarmAsCircles(ctx)
        drawEyes(ctx)
        //drawPath(ctx)
        //drawMover(ctx)
        super.draw(ctx)
    }

    private fun drawPath(ctx: CanvasRenderingContext2D) {
        ctx.beginPath()
        ctx.moveTo(body[0].x, body[0].y)
        for (i in 1 until body.size) {
            ctx.lineTo(body[i].x, body[i].y)
        }
        ctx.lineWidth = 0.5
        ctx.strokeStyle = strokeStyles[0]
        ctx.stroke()

    }

    /*
        private fun drawMover(ctx: CanvasRenderingContext2D) {
            movers.asSequence()
                .filter { it is ConstantSpeedMover  }
                .forEach { (it as ConstantSpeedMover).draw(ctx) }
        }
    */

    private fun drawWarmAsCircles(ctx: CanvasRenderingContext2D) {
        ctx.lineWidth = r/12

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

        val d = (p - body[1]). let {
            if (it == ZERO_VECTOR) v(1, 0) else it
        }

        ctx.lineJoin = CanvasLineJoin.BEVEL
        ctx.strokeStyle = strokeStyles[0]
        ctx.lineWidth = 1.5

        val baseAngle = d.norm().angle()
        val tr1 = r / 4
        val cX = tr1  * 3

        draw1Eye(ctx, baseAngle,  eyeAngle, tr1, cX)
        draw1Eye(ctx, baseAngle, eyeAngleMinus, tr1, cX)
    }

    private fun draw1Eye(ctx: CanvasRenderingContext2D, baseAngle: Double, angle: Double, tr1: Double, cX: Double) {
        ctx.save()
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
        ctx.restore()
    }

    private fun drawWarmWithStroke(ctx: CanvasRenderingContext2D) {
        ctx.lineWidth = r/10
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

        ctx.strokeStyle = "#D8B08C"
        ctx.fillStyle = "#0F6466"
        ctx.fill(path)
        ctx.stroke(path)
    }

    companion object {
        const val eyeAngle = PI / 8
        const val eyeAngleMinus = -eyeAngle
        const val snakeRadiusIncreasePerOneFoodItem: Double = 0.003

        private fun calculateWormLength(radius: Double): Int {
            return (radius * 2).toInt() - 20
        }

    }
}