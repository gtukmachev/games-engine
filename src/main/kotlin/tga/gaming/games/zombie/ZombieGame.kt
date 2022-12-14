package tga.gaming.games.zombie

import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.MouseEvent
import tga.gaming.engine.GameWord
import tga.gaming.engine.camera.Camera
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.index.gridStep
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.Frame
import tga.gaming.engine.model.Vector
import tga.gaming.engine.model.v
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import tga.gaming.games.zombie.objects.Ghost
import tga.gaming.games.zombie.objects.KotlinSign
import tga.gaming.games.zombie.objects.playerObj
import kotlin.random.Random.Default.nextDouble

class ZombieGame(
    canvas: HTMLCanvasElement,
    wordSize: Vector,
    camera: Camera =  Frame(v(0,0), v(canvas.width, canvas.height)).let{ Camera(it, it, wordSize) },
    dsp: Dispatcher = ObjectsDispatcher(ObjectsSquareIndex(wordSize))
): GameWord(
    canvas = canvas,
    wordSize = wordSize,
    dispatcher = dsp,
    camera = camera,
    renderer = HtmlCanvas2dRenderer(
        canvas,
        dsp,
        camera
    ),
    turnDurationMillis = 20
) {

    val t = gridStep * 3.5

    private val player = playerObj(wordSize / 2, wordSize)

    override fun startGame() {
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

        super.startGame()

        window.setInterval(timeout = 500, handler = this::ifPlay)
    }

    private fun ifPlay() {
        if (player.visibility <= 0) {

            player.imagesDrawer.nextImage()
            if (player.imagesDrawer.imageIndex == 0) {
                this.pause()
            } else {
                player.visibility = player.maxVisibility
            }

        } else {

            if (nextDouble() < 0.1) {
                dispatcher.addObj(
                    KotlinSign(
                        v(
                            x = nextDouble(wordSize.x),
                            y = nextDouble(wordSize.y)
                        )
                    )
                )

            }

            if (this.dispatcher.objects.size < 1000) {
                dispatcher.addObj(
                    Ghost(
                        v(
                            x = nextDouble(wordSize.x),
                            y = nextDouble(wordSize.y)
                        ),
                        player
                    )
                )
            }
        }

    }

    override fun propagateOnClick(me: MouseEvent) {
        println("mouseEvent.button = ${me.button}")
        dispatcher.addObj( Ghost(v(me.x, me.y), player) )
        super.propagateOnClick(me)
    }
}

