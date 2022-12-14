package tga.gaming.games.wiggly_worm

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.KeyboardEvent
import tga.gaming.engine.GameWord
import tga.gaming.engine.PI2
import tga.gaming.engine.camera.Camera
import tga.gaming.engine.camera.withCameraMover
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.dispatcher.GameObjects
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.model.*
import tga.gaming.engine.movers.KeyboardArrowsMover
import tga.gaming.engine.movers.addKeyboardAwsdMover
import tga.gaming.engine.movers.withConstantSpeedMover
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import tga.gaming.engine.shapes.Pointer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private val snakeInitialRadius: Double = 20.0
private val snakeSpeed: Double = 5.0
private val snakeMaxTurnAngle: Double = PI / 180 * 160
private val snakeRotationSpeed = PI / 180 * 3

var ws: Vector = v(10,10)
var wArea: Frame = Frame(v(), v())

class WigglyWorm(
    canvas: HTMLCanvasElement,
    wordSize: Vector,
    camera: Camera,
    dsp: Dispatcher = ObjectsDispatcher(ObjectsSquareIndex(wordSize)),
): GameWord(
    canvas = canvas,
    wordSize = wordSize,
    dispatcher = dsp,
    camera = camera,
    renderer = HtmlCanvas2dRenderer(canvas, dsp, camera),
    turnDurationMillis = 20,
) {
    override val isDebugUiAllowed = true

    private lateinit var pointer: Pointer
    private lateinit var player: Worm

    init {
        ws = wordSize
        wArea = Frame(p0 = v(0,0), p1 = wordSize.copy())

    }

    private lateinit var mover21: KeyboardArrowsMover

    override fun startGame() {
        addObjects()
        super.startGame()
    }

    private fun addObjects() {
        dispatcher.addObj(CirclesBackground(this))

        val center = wordSize / 2

        pointer = Pointer(camera, center)

        val clocks1: Pair<ClockPointer, ClockPointer> = createClockPointersChain()
        val clocks2: Pair<ClockPointer, ClockPointer> = createClockPointersChain()

        val worm1: Worm = createPlayerWorm(v(0, -100)).withConstantSpeedMover(snakeSpeed, snakeRotationSpeed, wArea){ pointer.p }
        val worm2: Worm = createWorm(v(0, +100))
        val worm11: Worm = createWorm(v(-150, -70)).withConstantSpeedMover(snakeSpeed, snakeRotationSpeed, wArea){ clocks1.second.hand }
        val worm22: Worm = createWorm(v(-150, +70)).withConstantSpeedMover(snakeSpeed, snakeRotationSpeed, wArea){ clocks2.second.hand }

        player = worm1
        player.game = this

        mover21 = worm2.addKeyboardAwsdMover(snakeSpeed, wArea)

        dispatcher.addObjs(worm1, worm2, worm11, worm22)

        clocks1.first.centerPlace = { worm1.body.last() }
        clocks2.first.centerPlace = { worm2.body.last() }

        repeat(1000){
            dispatcher.addFood()
        }

        dispatcher.addObj(pointer)
        //dispatcher.addObjs(CameraObjDrawer())
    }

    private fun createClockPointersChain(): Pair<ClockPointer, ClockPointer> {
        val p1  = ClockPointer(this, nextRandomRadius(50, 150), nextRandomSpeed())
        val p2 = ClockPointer(this, nextRandomRadius(50,100), nextRandomSpeed()).apply {
            centerPlace = { p1.hand }
        }
        val p3 = ClockPointer(this, nextRandomRadius(50,150), nextRandomSpeed()).apply {
            centerPlace = { p2.hand }
        }
        dispatcher.addObjs(p1, p2, p3)
        return p1 to p3
    }

    private var wormsCounter = 0
    private fun createWorm(centerOffset: Vector): Worm {
        val worm = WormWithFollowBodyMover(
            p = ws/2 + centerOffset,
            initialRadius = snakeInitialRadius,
            fillStyles =  SnakesPalette.colors[wormsCounter].fillStyles,
            strokeStyles = SnakesPalette.colors[wormsCounter].strokeStyles
        )
        wormsCounter++
        return worm
    }

    private fun createPlayerWorm(centerOffset: Vector): Worm {
        val worm = WormWithFollowBodyMover(
                p = ws/2 + centerOffset,
                initialRadius = snakeInitialRadius,
                fillStyles =  SnakesPalette.colors[wormsCounter].fillStyles,
                strokeStyles = SnakesPalette.colors[wormsCounter].strokeStyles,
                //maxCurveAngle = snakeMaxTurnAngle
            )
            //.withObjFrameDrawer()
            .withCameraMover(camera)
        wormsCounter++
        return worm
    }


    override fun propagateOnKeyPress(ke: KeyboardEvent) {
        mover21.onKeyDown(ke)
        super.propagateOnKeyPress(ke)
    }

    override fun propagateOnKeyUp(ke: KeyboardEvent) {
        mover21.onKeyUp(ke)
        super.propagateOnKeyUp(ke)
    }

}

fun GameObjects.addFood() {
    val off = 27.0
    addObj(
        Food(
            p =  v(off,off) + v(Random.nextDouble(ws.x-off), Random.nextDouble(ws.y-off))
        )
    )
}


class ClockPointer(
    val game: GameWord,
    r: Double,
    private var tSpeed: Double,
    private val color: String = "#EAE791FF"
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
        if (game.isDebugUiEnabled) {
            ctx.beginPath()
            ctx.moveTo(p.x, p.y)
            ctx.lineTo(hand.x, hand.y)
            ctx.strokeStyle = color
            ctx.lineWidth = 2.5
            ctx.stroke()
        }
    }
}

private fun nextRandomSpeed(): Double {
    val sign = if (Random.nextInt(100) > 50) 1 else -1
    return (PI2/360) * Random.nextDouble(0.5, 1.2) * sign
}

private fun nextRandomRadius(r1: Int, r2: Int): Double = r1.toDouble() + Random.nextDouble(r2.toDouble())