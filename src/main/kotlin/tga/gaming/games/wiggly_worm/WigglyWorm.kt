package tga.gaming.games.wiggly_worm

import org.w3c.dom.*
import org.w3c.dom.events.KeyboardEvent
import tga.gaming.engine.GameWord
import tga.gaming.engine.PI2
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.model.*
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import tga.gaming.engine.shapes.Pointer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private const val d = PI/180

private var showHiddenMagic = false

class WigglyWorm(
    canvas: HTMLCanvasElement,
    private val wordSize: Vector,
    dsp: Dispatcher = ObjectsDispatcher(ObjectsSquareIndex(wordSize))
): GameWord(
    canvas = canvas,
    dispatcher = dsp,
    renderer = HtmlCanvas2dRenderer(canvas, dsp),
    turnDurationMillis = 20
) {

    lateinit var pointer: Pointer

    val colorThemes = arrayOf(
        arrayOf( "#146152", "#44803F", "#B4CF66", "#FFEC5C", "#FF5A33" ),
        arrayOf( "#2C3532", "#D2E8E3", "#0F6466", "#FFCB9A", "#D8B08C" ),
        arrayOf( "#105057", "#898C8B", "#FF81D0", "#400036", "#919151" ),
    )

    var colors = colorThemes[Random.nextInt(colorThemes.size)]

    override fun startGame() {
        addObjects()
        super.startGame()
    }

    private fun addObjects() {
        val center = wordSize / 2

        pointer = Pointer(showHiddenMagic, center)

        val clockPointer1 = ClockPointer(nextRandomRadius(), nextRandomSpeed(), colors[2])
        val clockPointer2 = ClockPointer(nextRandomRadius(), nextRandomSpeed(), colors[0])
        val clockPointer3 = ClockPointer(nextRandomRadius(), nextRandomSpeed(), colors[1])
        val clockPointer4 = ClockPointer(nextRandomRadius(), nextRandomSpeed(), colors[4])
        dispatcher.addObjs(clockPointer1, clockPointer2, clockPointer3, clockPointer4)

        val worm1 = Worm(center,                                   colors[1], colors[0]){ clockPointer1.hand }
        val worm2 = Worm(center + v(0, -wordSize.y/4),             colors[2], colors[1]){ clockPointer2.hand }
        val worm3 = Worm(center + v(0, +wordSize.y/4),             colors[0], colors[4]){ clockPointer3.hand }
        val worm4 = Worm(center + v(-wordSize.y/4, +wordSize.y/4), colors[3], colors[2]){ clockPointer4.hand }

        dispatcher.addObjs(worm1, worm2, worm3, worm4)

        clockPointer1.centerPlace = { pointer.p }
        clockPointer2.centerPlace = { worm1.body.last() }
        clockPointer3.centerPlace = { worm2.body.last() }
        clockPointer4.centerPlace = { worm3.body.last() }

        dispatcher.addObj(pointer)
    }


    override fun propagateOnKeyPress(ke: KeyboardEvent) {
        when (ke.code) {
            "KeyH" -> { showHiddenMagic = !showHiddenMagic }
        }

        super.propagateOnKeyPress(ke)
    }
}


class ClockPointer(
    r: Double,
    var tSpeed: Double,
    val color: String
): Obj(r = r), Actionable, Drawable {

    var t = Random.nextDouble() * PI
    var hand: Vector = v(r,0)
    var centerPlace: () -> Vector = {p}

    override fun act() {
        t += tSpeed
        p.set(centerPlace())
        hand.set(p.x + cos(t) * r, p.y + sin(t) * r)
    }

    override fun draw(ctx: CanvasRenderingContext2D) {
        if (showHiddenMagic) {
            ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
            ctx.beginPath()
            ctx.strokeStyle = color
            ctx.lineWidth = 2.5
            ctx.moveTo(p.x, p.y)
            ctx.lineTo(hand.x, hand.y)
            ctx.stroke()
        }
    }
}

class Worm(
    p: Vector,
    var fillStyle: String = "#BF8360",
    var strokeStyle: String = "#8C4830",
    var target: () -> Vector
): Obj(p=p, r=20.0),
    CompositeDrawer, Moveable
{
    override val drawers = mutableListOf<Drawer>()

    var da: Double = d*10
    val body: MutableList<Vector> = ArrayList<Vector>().apply {
        repeat(10){ add(p.copy()) }
    }

    init {
        positions()
    }

    private fun positions() {
        var center = p.copy()
        var offset = v(-r,0.0)

        for (i in 0 until body.size) {
            body[i].set(center)
            center = center + offset
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

    val maxSpeed = 2.0
    override fun move() {
//        p.set(target.p)


        val tp = target()

        var d = tp - p
        if (d.len > maxSpeed) d = d.norm() * maxSpeed
        p += d

        body[0] = p
        for (i in 1 until body.size) {
            val prev = body[i-1]
            val curr = body[i]
            val backV = (curr - prev).assignLength(r)
            body[i] = prev + backV
        }



        //wigle()
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

    fun drawSimpleWarm(ctx: CanvasRenderingContext2D) {
        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
        ctx.lineWidth = 5.0
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
    }

    fun drawWarm(ctx: CanvasRenderingContext2D) {
        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)

        val `2 pi / 3` = 2.0*PI/3
        val `pi / 3` = PI/3

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
}


private fun nextRandomSpeed(): Double {
    val sign = if (Random.nextInt(100) > 50) 1 else -1
    return (PI2/360) * Random.nextDouble(0.5, 1.2) * sign
}

private fun nextRandomRadius(): Double = 50.0 + Random.nextDouble(200.0)