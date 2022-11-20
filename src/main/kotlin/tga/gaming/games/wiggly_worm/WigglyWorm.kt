package tga.gaming.games.wiggly_worm

import org.w3c.dom.*
import tga.gaming.engine.GameWord
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.model.*
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val d = PI/180

class WigglyWorm(
    canvas: HTMLCanvasElement,
    val wordSize: Vector,
    dsp: Dispatcher = ObjectsDispatcher(ObjectsSquareIndex(wordSize))
): GameWord(
    canvas = canvas,
    dispatcher = dsp,
    renderer = HtmlCanvas2dRenderer(canvas, dsp),
    turnDurationMillis = 20
) {

    lateinit var worm: Worm

    override fun startGame() {
        addObjects()
        super.startGame()
    }

    private fun addObjects() {
        worm = dispatcher.addObj( Worm( v(wordSize.x/5*3, wordSize.y/2) ) )
    }
}

class Worm(p:Vector): Obj(p=p, r=30.0),
    CompositeDrawer, Moveable
{
    override val drawers = mutableListOf<Drawer>()

    var da: Double = d*12
    private val body: MutableList<Vector> = ArrayList<Vector>().apply {
        repeat(20){ add(p.copy()) }
    }


    init {
        positions()
    }

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

    var t: Double = 0.0
    var dt: Double = 0.05

    override fun move() {
        t += dt

        val k = sin(t)
        da = (d * 17) * k

        positions()
    }

    override fun draw(ctx: CanvasRenderingContext2D) {
        drawWarm(ctx)
        //drawCircles(ctx)
        super.draw(ctx)
    }

/*
    private fun drawCircles(ctx: CanvasRenderingContext2D) {
        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
        ctx.strokeStyle = "aquamarine"
        ctx.lineWidth = 1.0
        ctx.lineJoin = CanvasLineJoin.ROUND

        for(b in body) {
            ctx.beginPath()
            ctx.arc(x=b.x, y=b.y, radius = r, startAngle = 0.0, endAngle = PI2, anticlockwise = false)
            ctx.stroke()
        }
    }
*/

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
