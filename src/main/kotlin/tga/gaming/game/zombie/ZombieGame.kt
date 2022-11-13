package tga.gaming.game.zombie

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.MouseEvent
import tga.gaming.engine.GameWord
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.drawers.withObjPositionDrawer
import tga.gaming.engine.image.getImage
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.index.sizeFactor
import tga.gaming.engine.model.*
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import kotlin.math.PI
import kotlin.math.sin

class ZombieGame(
    val canvas: HTMLCanvasElement,
    val wordSize: Vector,
    dsp: Dispatcher = ObjectsDispatcher(ObjectsSquareIndex(wordSize))
): GameWord(
    dispatcher = dsp,
    renderer = HtmlCanvas2dRenderer(canvas, dsp),
    turnDurationMillis = 25
) {

    val t = (1 shl sizeFactor) * 3.5
    val mainObj = KotlinSign(v(t,t))
        .apply {
//            withObjFrameDrawer()
            withObjPositionDrawer()
        }

    var rm = false

    fun startGame() {
        //dispatcher.addObj(IndexGrid())
        dispatcher.addObj(mainObj)

        this.run()

        canvas.onmouseup   = { stopMove() }
        canvas.onmousedown = { startMove() }
        canvas.onmousemove = { m -> doMove(m) }
    }

    private fun doMove(m: MouseEvent) {
        if (!rm) return
        mainObj.p.set(m.x, m.y)
        dispatcher.index.update(mainObj)
    }

    private fun stopMove()  { rm = false }
    private fun startMove() { rm = true  }

}

class KotlinSign(
    p: Vector
) : Obj(p = p, r = (1 shl sizeFactor)-5.0 ),
    CompositeDrawer,
    Actionable
{
    override val drawers = mutableListOf<Drawer>()

    val image = getImage("https://play.kotlinlang.org/assets/kotlin-logo.svg")

    override fun draw(ctx: CanvasRenderingContext2D) {
        ctx.drawImage(image, frame!!.p0.x, frame!!.p0.y, r*2, r*2)
        super.draw(ctx)
    }

    var t = 0.0
    override fun act() {
        t += 0.1
        angle = amplitude * sin(t)
    }

    companion object {
        const val amplitude = PI/4
    }
}

