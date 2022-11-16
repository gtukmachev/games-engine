package tga.gaming.game.zombie

import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.MouseEvent
import tga.gaming.engine.GameWord
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.index.gridStep
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.Vector
import tga.gaming.engine.model.v
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import tga.gaming.game.zombie.objects.Ghost
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

    private val player = playerObj(wordSize / 2, wordSize)

    fun startGame() {
        //dispatcher.addObj(IndexGrid("#443c38", "#886134"))

        val d = gridStepD/2
        dispatcher.addObj(
            KotlinSign(
                p = wordSize - v(d,d),
                speed = 0.08,
                r = (gridStepD / 2.0) / 2
            )
        )
        dispatcher.addObj(player)

        dispatcher.addObj( Ghost( v(0,0), player) )
        dispatcher.addObj( Ghost( wordSize.copy(), player) )
        dispatcher.addObj( Ghost( wordSize.copy(x = 0.0), player) )
        dispatcher.addObj( Ghost( wordSize.copy(y = 0.0), player) )

        this.run()

    }

    override fun propagateOnClick(mouseEvent: MouseEvent) {
        println("mouseEvent.button = ${mouseEvent.button}")
        dispatcher.addObj( Ghost(v(mouseEvent.x, mouseEvent.y), player) )
        super.propagateOnClick(mouseEvent)
    }
}

