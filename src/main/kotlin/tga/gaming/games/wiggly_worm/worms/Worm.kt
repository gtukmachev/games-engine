package tga.gaming.games.wiggly_worm.worms

import org.w3c.dom.BEVEL
import org.w3c.dom.CanvasLineJoin
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.Path2D
import tga.gaming.engine.*
import tga.gaming.engine.model.*
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import tga.gaming.games.wiggly_worm.WigglyWormGame
import tga.gaming.games.wiggly_worm.objects.Food
import kotlin.math.PI
import kotlin.math.max
import kotlin.random.Random

abstract class Worm(
    p: Vector,
    private val initialRadius: Double,
    private var fillStyles: Array<String>,
    private var strokeStyles: Array<String>,
    private val electricCharge: Boolean = Random.nextBoolean(),
    val game: WigglyWormGame
): Obj(p=p), CompositeDrawer, Moveable, Actionable, CompositeMover {

    var isCurrentPalyer: Boolean = false
    var onDead: ((Worm) -> Unit)? = null

    override val isAlwaysVisible: Boolean = true
    override val drawers = ArrayList<Drawer>()
    override val movers = ArrayList<Mover>()

    private var actionDistance: Double = initialRadius
    private var actionDistance2: Double = actionDistance*actionDistance
    override var r: Double = initialRadius
        set(value) {
            field = value
            r2Cache = null
            desiredBodyLength = calculateWormLength(r)
            correctFrame()
        }

    private fun correctFrame() {
        actionDistance = r * 3
        actionDistance2 = actionDistance*actionDistance
        frame?.apply {
            p0.set(-actionDistance, -actionDistance)
            p1.set( actionDistance,  actionDistance)
        }
    }

    var desiredBodyLength: Int = calculateWormLength(initialRadius); private set
    val body: MutableList<Body> = ArrayList<Body>(desiredBodyLength).apply {
        repeat(desiredBodyLength) { add(Body(p.copy(), this@Worm)) }
    }

    init {
        correctFrame()
    }

    private fun eat(food: Food) {
        val distance = food.p - this.p

        if (distance.len2 < actionDistance2) {
            val k = when (electricCharge) {
                food.electricCharge -> 0.05
                else -> -0.05
            }
            food.speed += distance.norm() * k
        }

        val radiusToEat = (this.r + food.r) - distance.len
        if (radiusToEat <= 0) return


        food.r = distance.len - this.r
        if (food.r <= 0.0) {
            food.r = 0.0
            dispatcher.delObj(food)
            game.addFood()
        }

        this.r += radiusToEat * snakeRadiusIncreasePerOneFoodItem

        while (desiredBodyLength > body.size) {
            increaseWormBodyLength()
        }


        if (isCurrentPalyer) scaleGameView()
    }

    private fun scaleGameView() {
        with((game.renderer as HtmlCanvas2dRenderer).camera) {
            val desiredScale = max(0.5, initialRadius / r)
            changeScaleTo(desiredScale)
        }
    }

    open fun increaseWormBodyLength() {
        body.add(Body(body.last().p.copy() - v(0.01, 0.01), this))
    }

    override fun act() {
        val nearestObjects = dispatcher.index.objectsOnTheSamePlaceWith(this)
        for (obj in nearestObjects)
            when (obj) {
                is Food -> eat(obj)
                is Body -> if (checkClash(obj)) { clash(); break }
            }
    }

    private fun clash() {
        body.forEach {
            dispatcher.delObj(it)
            game.addFood(it.p + Vector.random1() * Random.nextDouble(r, 2*r), isOverFoodAllowed = true )
        }

        dispatcher.delObj(this)
        onDead?.invoke(this)
    }

    private fun checkClash(bodyCeil: Body): Boolean {
        if (bodyCeil.worm == this) return false

        val touchPoint = p + (p - body[1].p)
        val distance2 = (bodyCeil.p - touchPoint).len2

        if (distance2 > bodyCeil.r2) return false

        return true
    }



    override fun move() {
        super.move()
        moveWormBody()
        body.forEach {
            dispatcher.index.update(it)
        }
    }

    abstract fun moveWormBody()

    override fun draw(ctx: CanvasRenderingContext2D) {
        //drawWarmWithStroke(ctx)
        drawWarmBackgroundAsCircles(ctx)
        drawWarmAsCircles(ctx, strokes = false)
        drawEyes(ctx)
        drawPath(ctx)
        //drawMover(ctx)
        super.draw(ctx)
    }

    private fun drawPath(ctx: CanvasRenderingContext2D) {
        ctx.beginPath()
        ctx.moveTo(body[1].p.x, body[1].p.y)
        for (i in 2 until body.size) {
            ctx.lineTo(body[i].p.x, body[i].p.y)
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

    private fun drawWarmBackgroundAsCircles(ctx: CanvasRenderingContext2D) {
        ctx.lineWidth = r/3
        val strokesOffset = strokeStyles.size/2

        for(i in body.size - 1 downTo 0 ) {

            if (!(game.camera.isInVisibleArea(body[i].p, r))) continue

            ctx.strokeStyle = strokeStyles[ (i+strokesOffset) %  strokeStyles.size]

            val b = body[i]
            ctx.beginPath()
            ctx.arc(
                x = b.p.x, y = b.p.y, radius = r,
                startAngle = 0.0,
                endAngle = PI2
            )
            ctx.stroke()
        }

    }

    private fun drawWarmAsCircles(ctx: CanvasRenderingContext2D, strokes: Boolean = true) {
        ctx.lineWidth = r/12

        for(i in body.size - 1 downTo 0 ) {

            if (!(game.camera.isInVisibleArea(body[i].p, r))) continue

            ctx.strokeStyle = strokeStyles[i %  strokeStyles.size]
            ctx.fillStyle = fillStyles[i % fillStyles.size]

            val b = body[i]
            ctx.beginPath()
            ctx.arc(
                x = b.p.x, y = b.p.y, radius = r,
                startAngle = 0.0,
                endAngle = PI2
            )
            ctx.fill()
            if (strokes) ctx.stroke()
        }

    }

    private fun drawEyes(ctx: CanvasRenderingContext2D) {

        val d = (p - body[1].p). let {
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
        ctx.lineWidth = r/3
        ctx.lineJoin = CanvasLineJoin.BEVEL

        val path = Path2D()

        ctx.beginPath()
        val centerFirst = body[0].p
        val centerSecond = body[1].p
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
            val center = body[i].p
            val nexCenter = body[i+1].p

            val aStart = aOldEnd -pi_3

            val vec = (center - nexCenter).norm()
            val aEnd =  pi2_3 + vec.angle()

            path.arc(x = center.x, y = center.y, radius = r, startAngle = aStart, endAngle = aEnd)
            aOldEnd = aEnd
        }

        // tail
        val centerLast = body[body.size-1].p
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
            val center = body[i].p
            val prevCenter = body[i-1].p


            val aStart = aOldEnd - pi_3

            val vec = (prevCenter-center).norm()
            val a = vec.angle()
            val aEnd = pi5_3 + a

            path.arc(x = center.x, y = center.y, radius = r, startAngle = aStart, endAngle = aEnd)
            aOldEnd = aEnd
        }

        ctx.strokeStyle = strokeStyles[strokeStyles.size/2]

        //ctx.strokeStyle = "#D8B08C"
        ctx.fillStyle = "#0F6466"
        //ctx.fill(path)
        ctx.stroke(path)
    }

    companion object {
        const val eyeAngle = PI / 8
        const val eyeAngleMinus = -eyeAngle
        const val snakeRadiusIncreasePerOneFoodItem: Double = 0.003

        private fun calculateWormLength(radius: Double): Int {
            return (radius * 3).toInt() - 40
        }

    }
}


class Body(
    p: Vector,
    val worm: Worm
): Obj(p = p) {

    override var r: Double
        get() = worm.r
        set(value) { worm.r = value }


    override val r2: Double
        get() = worm.r2
}