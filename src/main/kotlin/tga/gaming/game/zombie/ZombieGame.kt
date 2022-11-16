package tga.gaming.game.zombie

import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.MouseEvent
import tga.gaming.engine.GameWord
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.index.gridStep
import tga.gaming.engine.model.Vector
import tga.gaming.engine.model.v
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import tga.gaming.game.zombie.objects.Gost
import tga.gaming.game.zombie.objects.KotlinSign
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
        //dispatcher.addObj(IndexGrid("#443c38", "#886134"))

        dispatcher.addObj(KotlinSign(wordSize.copy(y = 100.0) - v(-200,0)))
        dispatcher.addObj(player)

        dispatcher.addObj( Gost( v(0,0), player) )
        dispatcher.addObj( Gost( wordSize.copy(), player) )
        dispatcher.addObj( Gost( wordSize.copy(x = 0.0), player) )
        dispatcher.addObj( Gost( wordSize.copy(y = 0.0), player) )

        this.run()

    }

    override fun propagateOnClick(mouseEvent: MouseEvent) {
        println("mouseEvent.button = ${mouseEvent.button}")
        dispatcher.addObj( Gost(v(mouseEvent.x, mouseEvent.y), player) )
        super.propagateOnClick(mouseEvent)
    }
}

