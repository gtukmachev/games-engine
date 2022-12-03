package tga.gaming.games.wiggly_worm

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.KeyboardEvent
import tga.gaming.engine.GameWord
import tga.gaming.engine.PI2
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.dispatcher.GameObjects
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.model.*
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import tga.gaming.engine.shapes.Pointer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

const val d = PI/180

private var showHiddenMagic = false

private var ws: Vector = v(10,10)

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
        ws = wordSize
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

        val worm1 = Worm(center,                                   colors[1], colors[0]){ /*clockPointer1.hand*/ pointer.p }
        val worm2 = Worm(center + v(0, -wordSize.y/4),             colors[2], colors[1]){ clockPointer2.hand }
        val worm3 = Worm(center + v(0, +wordSize.y/4),             colors[0], colors[4]){ clockPointer3.hand }
        val worm4 = Worm(center + v(-wordSize.y/4, +wordSize.y/4), colors[3], colors[2]){ clockPointer4.hand }

        dispatcher.addObjs(worm1, worm2, worm3, worm4)

        clockPointer1.centerPlace = { pointer.p }
        clockPointer2.centerPlace = { worm1.body.last() }
        clockPointer3.centerPlace = { worm2.body.last() }
        clockPointer4.centerPlace = { worm3.body.last() }

        dispatcher.addFood()
        dispatcher.addFood()

        dispatcher.addObj(pointer)


    }

    override fun propagateOnKeyPress(ke: KeyboardEvent) {
        when (ke.code) {
            "KeyH" -> { showHiddenMagic = !showHiddenMagic }
        }

        super.propagateOnKeyPress(ke)
    }
}

fun GameObjects.addFood() {
    val off = 100.0
    addObj(
        Food(
            p =  v(off,off) + v(Random.nextDouble(ws.x-off), Random.nextDouble(ws.y-off)),
            r = 20.0
        )
    )
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

private fun nextRandomSpeed(): Double {
    val sign = if (Random.nextInt(100) > 50) 1 else -1
    return (PI2/360) * Random.nextDouble(0.5, 1.2) * sign
}

private fun nextRandomRadius(): Double = 50.0 + Random.nextDouble(200.0)