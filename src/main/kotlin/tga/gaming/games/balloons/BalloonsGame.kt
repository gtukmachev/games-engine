package tga.gaming.games.balloons

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.MouseEvent
import tga.gaming.engine.GameWord
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.dispatcher.SimpleEventsListener
import tga.gaming.engine.drawers.ObjPositionDrawer.Companion.PI2
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.*
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextInt

private val numberOfCircles = 1500
private val speedFixPart = 0.2
private val speedRandomPart = 0.6


private val growSpeed: Double = 2.5
private val deGrowSpeed: Double = growSpeed / 2.0
private val maxR: Double = 60.0


class BalloonsGame(
    canvas: HTMLCanvasElement,
    val wordSize: Vector,
    dsp: Dispatcher = ObjectsDispatcher(ObjectsSquareIndex(wordSize))
): GameWord(
    dispatcher = dsp,
    renderer = HtmlCanvas2dRenderer(canvas, dsp),
    turnDurationMillis = 20
) {

    override fun startGame() {
        initObjects()
        super.startGame()
    }

    private fun initObjects() {
        //dispatcher.addObj(IndexGrid("#443c38", "#886134"))

        val pointer = Mouse(wordSize / 2, r=gridStepD*1.5)
        dispatcher.addObj(pointer)


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
            arrayOf("#FF5F5D","#3F7C85","#00CCBF","#72F2EB","#747E7E",),
            arrayOf("#012030","#13678A","#45C4B0","#9AEBA3","#DAFDBA",),
            arrayOf("#151F30","#103778","#0593A2","#FF7A48","#E3371E",),
            arrayOf("#105057","#898C8B","#FF81D0","#400036","#919151",),
            arrayOf("#146152","#44803F","#B4CF66","#FFEC5C","#FF5A33",),
            arrayOf("#662400","#B33F00","#FF6B1A","#006663","#00B3AD",),
        )
    }
}



class Circle(
    p: Vector,
    r: Double,
    private val color: String,
    private val area: Frame,
    speedLength: Double,
) : Obj(
    p = p,
    r = r
), CompositeDrawer, Moveable, Actionable {

    val minR = r
    var speed = randomVector() * speedLength

    override val drawers = mutableListOf<Drawer>()

    override fun draw(ctx: CanvasRenderingContext2D) {
        ctx.beginPath()
        ctx.fillStyle = color
        ctx.arc(
            x = 0.0,
            y = 0.0,
            radius = r,
            startAngle = 0.0,
            endAngle =  PI2
        )
        ctx.fill()

        super.draw(ctx)
    }

    override fun move() {
        p += speed
        if (p.x < area.p0.x && speed.x < 0) speed.x = -speed.x;
        if (p.x > area.p1.x && speed.x > 0) speed.x = -speed.x;
        if (p.y < area.p0.y && speed.y < 0) speed.y = -speed.y;
        if (p.y > area.p1.y && speed.y > 0) speed.y = -speed.y;
    }

    override fun act() {
        if (r > minR) r -= deGrowSpeed
        if (r < minR) r = minR
    }
}


class Mouse(
    p: Vector,
    r: Double,
) : Obj(
    p = p,
    r = r
), Moveable, SimpleEventsListener, Actionable {

    private var mouseCoordinates : Vector? = null

    override fun move() {
        mouseCoordinates?.let {
            p.set(it)
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

    override fun onMouseMove(mouseEvent: MouseEvent) {
        if (mouseCoordinates == null) mouseCoordinates = v()
        mouseCoordinates!!.set(mouseEvent.x, mouseEvent.y)
    }

    override fun onMouseEnter(mouseEvent: MouseEvent) {
        mouseCoordinates =v(mouseEvent.x, mouseEvent.y)
    }

    override fun onMouseLeave(mouseEvent: MouseEvent) {
        mouseCoordinates = null
    }
}
