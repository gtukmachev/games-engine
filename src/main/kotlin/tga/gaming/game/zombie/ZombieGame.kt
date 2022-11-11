package tga.gaming.game.zombie

import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.MouseEvent
import tga.gaming.engine.GameWord
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.model.Vector
import tga.gaming.engine.model.v
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import tga.gaming.engine.shapes.IndexGrid
import tga.gaming.engine.shapes.Rect

class ZombieGame(
    val canvas: HTMLCanvasElement,
    val wordSize: Vector,
    val dsp: ObjectsDispatcher = ObjectsDispatcher(ObjectsSquareIndex(wordSize))
): GameWord(
    dispatcher = dsp,
    renderer = HtmlCanvas2dRenderer(canvas, dsp)
) {
    val rect = Rect(v(100, 100), 16.0)
    var rm = false

    fun startGame() {
        dsp.addObj(IndexGrid())

        dsp.addObj(rect)

        this.run()


        canvas.onmouseup   = { stopMove() }
        canvas.onmousedown = { startMove() }
        canvas.onmousemove = { m -> doMove(m) }
    }

    private fun doMove(m: MouseEvent) {
        if (!rm) return
        rect.p.set(m.x, m.y)
        dsp.index.update(rect)
    }

    private fun stopMove() {
        rm = false
    }

    private fun startMove() {
        rm = true
    }


}

