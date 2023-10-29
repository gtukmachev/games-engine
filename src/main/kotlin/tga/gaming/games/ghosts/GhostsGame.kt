package tga.gaming.games.ghosts

import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.MouseEvent
import tga.gaming.engine.GameWord
import tga.gaming.engine.camera.Camera
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.drawers.withCircleDrawer
import tga.gaming.engine.drawers.withObjFrameDrawer
import tga.gaming.engine.index.ObjectsSquareIndex
import tga.gaming.engine.index.gridStep
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.Frame
import tga.gaming.engine.model.Vector
import tga.gaming.engine.model.v
import tga.gaming.engine.render.HtmlCanvas2dRenderer
import tga.gaming.engine.shapes.Pointer
import tga.gaming.games.ghosts.objects.Ghost
import tga.gaming.games.ghosts.objects.KotlinSign
import tga.gaming.games.ghosts.objects.PlayerObj
import tga.gaming.games.ghosts.objects.playerObj
import kotlin.random.Random.Default.nextDouble

class GhostsGame(
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
    override val isDebugUiAllowed = true

    private lateinit var pointer: Pointer
    private lateinit var player: PlayerObj

    override fun startGame() {
        val d = gridStepD / 2
        val center = wordSize / 2

        pointer = dispatcher.addObj( Pointer(camera, center)
            .withCircleDrawer(radius = 5))

        player = playerObj(center, wordSize){ pointer.p }
            .withObjFrameDrawer()

        dispatcher.addObj(
            KotlinSign(p = wordSize / 2, speed = 0.08, r = d*3)
                .withObjFrameDrawer()
        )
        dispatcher.addObj(player)

        dispatcher.addObj( Ghost( v(0,0), player) )
        dispatcher.addObj( Ghost( wordSize.copy(), player) )
        dispatcher.addObj( Ghost( wordSize.copy(x = 0.0), player) )
        dispatcher.addObj( Ghost( wordSize.copy(y = 0.0), player) )

        window.setInterval(timeout = 500, handler = this::ifPlay)
        super.startGame()
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

