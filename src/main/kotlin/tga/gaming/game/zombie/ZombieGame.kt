package tga.gaming.game.zombie

import org.w3c.dom.HTMLCanvasElement
import tga.gaming.engine.GameWord
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.index.gridStep
import tga.gaming.engine.model.Vector
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import tga.gaming.game.zombie.objects.playerObj

class ZombieGame(
        canvas: HTMLCanvasElement,
    val wordSize: Vector,
    dsp: Dispatcher = ObjectsDispatcher(ObjectsSquareIndex(wordSize))
): GameWord(
    dispatcher = dsp,
    renderer = HtmlCanvas2dRenderer(canvas, dsp),
    turnDurationMillis = 20
) {

    val t = gridStep * 3.5

    val player = playerObj(wordSize / 2, 1)

    fun startGame() {
        // dispatcher.addObj(IndexGrid("#443c38", "#886134"))
        dispatcher.addObj(player)

        this.run()

    }
}

