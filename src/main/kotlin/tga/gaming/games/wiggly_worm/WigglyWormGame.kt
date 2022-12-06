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
import tga.gaming.engine.movers.*
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import tga.gaming.engine.shapes.Pointer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

const val d = PI/180

private var showHiddenMagic = false

var ws: Vector = v(10,10)
var wArea: Frame = Frame(v(), v())

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

    val snakeSpeed = 2.0

    val colorThemes = arrayOf(
        arrayOf( "#146152", "#44803F", "#B4CF66", "#FFEC5C", "#FF5A33" ),
        arrayOf( "#2C3532", "#D2E8E3", "#0F6466", "#FFCB9A", "#D8B08C" ),
        arrayOf( "#105057", "#898C8B", "#FF81D0", "#400036", "#919151" ),
    )


    var colors = colorThemes[Random.nextInt(colorThemes.size)]
    lateinit var mover1: KeyboardArrowsMover
    lateinit var mover2: KeyboardArrowsMover

    override fun startGame() {
        ws = wordSize
        wArea = Frame(p0 = v(0,0), p1 = wordSize.copy())
        addObjects()
        super.startGame()
    }

    private fun addObjects() {
        val center = wordSize / 2

        pointer = Pointer(showHiddenMagic, center)

        val clockPointer1  = ClockPointer(nextRandomRadius(50, 150), nextRandomSpeed(), colors[2])
        val clockPointer11 = ClockPointer(nextRandomRadius(50,100), nextRandomSpeed(), colors[2]).apply {
            centerPlace = { clockPointer1.hand }
        }
        val clockPointer2  = ClockPointer(nextRandomRadius(50, 150), nextRandomSpeed(), colors[1])
        val clockPointer22 = ClockPointer(nextRandomRadius(50,100), nextRandomSpeed(), colors[1]).apply {
            centerPlace = { clockPointer2.hand }
        }

        dispatcher.addObjs(clockPointer1, clockPointer2, clockPointer11, clockPointer22)

        val worm1 = Worm(
            center - v(0, wordSize.y/10),
            fillStyles = SnakesPalette.colors[0].fillStyles,
            strokeStyles = SnakesPalette.colors[0].strokeStyles
        ).withConstantSpeedMover(
                speed = snakeSpeed,
                rotationSpeed = PI / 180 * 3,
                bounds = wArea
            ){
                pointer.p
            }

        var c = 0
        val worm2: Worm = Worm(center + v(0, wordSize.y/10),              SnakesPalette.colors[++c].fillStyles, SnakesPalette.colors[c].strokeStyles)

        val worm3: Worm = Worm(worm1.p + v(-150, -70),  SnakesPalette.colors[++c].fillStyles, SnakesPalette.colors[c].strokeStyles)
            .withFollowMover(snakeSpeed){ clockPointer11.hand }

        val worm4: Worm = Worm(worm2.p + v(-150, +70), SnakesPalette.colors[++c].fillStyles, SnakesPalette.colors[c].strokeStyles)
            .withFollowMover(snakeSpeed){ clockPointer22.hand }


        val areaBounds = Frame(p0=v(0,0), p1=ws.copy())
        mover1 = worm1.addKeyboardAwsdMover  (snakeSpeed, areaBounds)
        mover2 = worm2.addKeyboardArrowsMover(snakeSpeed, areaBounds)

        dispatcher.addObjs(worm1, worm2, worm3, worm4)

        clockPointer1.centerPlace = { worm1.body.last() }
        clockPointer2.centerPlace = { worm2.body.last() }

        repeat(60){
            dispatcher.addFood()
        }

        dispatcher.addObj(pointer)
    }

    override fun propagateOnKeyPress(ke: KeyboardEvent) {
        when (ke.code) {
            "KeyH" -> { showHiddenMagic = !showHiddenMagic }
        }

        mover1.onKeyDown(ke)
        mover2.onKeyDown(ke)

        super.propagateOnKeyPress(ke)
    }

    override fun propagateOnKeyUp(ke: KeyboardEvent) {
        mover1.onKeyUp(ke)
        mover2.onKeyUp(ke)
        super.propagateOnKeyUp(ke)
    }

    override fun paint(t: Double) {
        super.paint(t)
    }
}

fun GameObjects.addFood() {
    val off = 27.0
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

private fun nextRandomRadius(r1: Int, r2: Int): Double = r1.toDouble() + Random.nextDouble(r2.toDouble())