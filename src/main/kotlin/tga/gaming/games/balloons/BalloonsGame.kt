package tga.gaming.games.balloons

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.MouseEvent
import tga.gaming.engine.GameWord
import tga.gaming.engine.PI2
import tga.gaming.engine.camera.Camera
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.dispatcher.SimpleEventsListener
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.*
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import tga.gaming.engine.shapes.Pointer
import tga.gaming.engine.shapes.withIndexGrid
import tga.gaming.engine.shapes.withPointer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextInt

private const val show2dIndexGrid = false
private const val surfaceCoefficient: Int = 1000

private const val speedFixPart = 0.2
private const val speedRandomPart = 1.2

private const val mouseRotationSpeed = 0.020
private const val mouseRotationRadius = 70.0

private const val growSpeed: Double = 2.5
private const val deGrowSpeed: Double = growSpeed / 2.0
private const val maxR: Double = 60.0


class BalloonsGame(
    canvas: HTMLCanvasElement,
    wordSize: Vector,
    camera: Camera =  Frame(v(0,0), v(canvas.width, canvas.height)).let{ Camera(it, it, wordSize) },
    dsp: Dispatcher = ObjectsDispatcher(ObjectsSquareIndex(wordSize)),
): GameWord(
    canvas = canvas,
    wordSize = wordSize,
    dispatcher = dsp,
    camera = camera,
    renderer = HtmlCanvas2dRenderer(
        canvas,
        dsp,
        camera,
    ),
    turnDurationMillis = 20
) {

    override fun startGame() {
        initObjects()
        super.startGame()
    }

    private fun initObjects() {
        if (show2dIndexGrid) withIndexGrid()

        val pointer = withPointer((renderer as HtmlCanvas2dRenderer).camera)

        dispatcher.addObj(
            MagnifyingGlass(wordSize/2, r=gridStepD*1.5, pointer = pointer)
        )

        generateCircles( (wordSize.x * wordSize.y).toInt() / surfaceCoefficient )
    }

    private fun generateCircles(numberOfCircles: Int) {
        val r = 5.0
        val wholeWordSize = Frame( v(r,r), wordSize.copy() - v(r,r) )
        val offset = v(gridStepD/2, gridStepD/2)
        val dx = wordSize.x - gridStepD
        val dy = wordSize.y - gridStepD
        val colors = colorsArray[nextInt(colorsArray.size)]

        for (i in 0..numberOfCircles) {
            dispatcher.addObj(
                Circle(
                    p = v(nextDouble(dx), nextDouble(dy)) + offset,
                    r = r,
                    color = colors[ nextInt(colors.size) ],
                    area = wholeWordSize,
                    speedLength = speedFixPart + nextDouble(0.0, speedRandomPart)
                )
            )
        }

    }

    companion object {
        val colorsArray = arrayOf(
            arrayOf("#FF5F5D","#3F7C85","#00CCBF","#72F2EB","#747E7E"),
            arrayOf("#012030","#13678A","#45C4B0","#9AEBA3","#DAFDBA"),
            arrayOf("#151F30","#103778","#0593A2","#FF7A48","#E3371E"),
            arrayOf("#105057","#898C8B","#FF81D0","#400036","#919151"),
            arrayOf("#146152","#44803F","#B4CF66","#FFEC5C","#FF5A33"),
            arrayOf("#662400","#B33F00","#FF6B1A","#006663","#00B3AD"),
        )
    }
}

private class Circle(
    p: Vector,
    r: Double,
    private val color: String,
    private val area: Frame,
    speedLength: Double,
) : Obj(p = p, r = r), CompositeDrawer, Moveable, Actionable {

    val minR = r
    var speed = randomNormVector() * speedLength
    var addSpeed: Vector? = null

    override val drawers = mutableListOf<Drawer>()

    override fun draw(ctx: CanvasRenderingContext2D) {
        ctx.beginPath()
        ctx.fillStyle = color
        ctx.arc(x = 0.0, y = 0.0, radius = r, startAngle = 0.0, endAngle =  PI2)
        ctx.fill()

        super.draw(ctx)
    }

    override fun move() {
        p += speed
        addSpeed?.let{ p += it }
        if (p.x < area.p0.x && speed.x < 0) { speed.x = -speed.x; addSpeed?.let{it.x = -it.x} }
        if (p.x > area.p1.x && speed.x > 0) { speed.x = -speed.x; addSpeed?.let{it.x = -it.x} }
        if (p.y < area.p0.y && speed.y < 0) { speed.y = -speed.y; addSpeed?.let{it.y = -it.y} }
        if (p.y > area.p1.y && speed.y > 0) { speed.y = -speed.y; addSpeed?.let{it.y = -it.y} }
    }

    override fun act() {
        if (r > minR) r -= deGrowSpeed
        if (r < minR) r = minR

        if (addSpeed != null) {
            addSpeed = if (addSpeed!!.len > 0.001) addSpeed!! * 0.99 else null
        }
    }
}


private class MagnifyingGlass(p: Vector, r: Double, val pointer: Pointer) : Obj(p = p, r = r),
    Moveable, Actionable, SimpleEventsListener
{
    var t: Double = 0.0
    override fun move() {
        val position = pointer.externalPointerCoordinates ?: pointer.p
        position.let {
            t += mouseRotationSpeed
            if (t > PI2) t -= PI2
            val offset = v(sin(t), cos(t)) * mouseRotationRadius
            p.set(it + offset)
        }
    }

    override fun act() {
        dispatcher.index.objectsOnTheSamePlaceWith(this).forEach {
            if (it is Circle) {
                val len = (it.p - p).len
                if (len < r) {
                    if (it.r < maxR) it.r += growSpeed
                    if (it.r > maxR) it.r = maxR
                }
            }
        }
    }

    override fun onClick(me: MouseEvent) {
        dispatcher.index.objectsOnTheSamePlaceWith(this).forEach {
            if (it is Circle) {
                val len = (it.p - p).len
                if (len < r) {
                    it.addSpeed = it.speed.norm() * 9.0
                }
            }
        }
    }

}
