package tga.gaming.game.zombie

import org.w3c.dom.HTMLCanvasElement
import tga.gaming.engine.GameWord
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.model.Vector
import tga.gaming.engine.render.HtmlCanvas2dRenderer

class ZombieGame(
    val canvas: HTMLCanvasElement,
    val wordSize: Vector
): GameWord(
    dispatcher = ObjectsDispatcher(),
    index = ObjectsSquareIndex(wordSize),
    renderer = HtmlCanvas2dRenderer(canvas)
) {

    fun startGame() {
        this.run()
    }


}
