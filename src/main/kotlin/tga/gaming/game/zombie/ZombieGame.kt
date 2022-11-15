package tga.gaming.game.zombie

import org.w3c.dom.HTMLCanvasElement
import tga.gaming.engine.GameWord
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.index.gridStep
import tga.gaming.engine.model.Vector
import tga.gaming.engine.model.v
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import tga.gaming.engine.shapes.IndexGrid
import tga.gaming.game.zombie.objects.Zombie
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

    val player = playerObj(wordSize / 2, wordSize)

    fun startGame() {
        dispatcher.addObj(IndexGrid("#443c38", "#886134"))
        dispatcher.addObj(player)

        dispatcher.addObj( Zombie( v(0,0), player) )
        dispatcher.addObj( Zombie( wordSize, player) )
        dispatcher.addObj( Zombie( wordSize.copy(x = 0.0), player) )
        dispatcher.addObj( Zombie( wordSize.copy(y = 0.0), player) )

        this.run()

    }
}

