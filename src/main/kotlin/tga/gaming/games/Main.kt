package tga.gaming.games

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.Node
import tga.gaming.engine.GameWord
import tga.gaming.engine.camera.Camera
import tga.gaming.engine.index.gridStep
import tga.gaming.engine.model.Frame
import tga.gaming.engine.model.Vector
import tga.gaming.engine.model.v
import tga.gaming.games.balloons.BalloonsGame
import tga.gaming.games.ghosts.GhostsGame
import tga.gaming.games.wiggly_worm.WigglyWormGame

var game: GameWord? = null
lateinit var canvas: HTMLCanvasElement

enum class GameCase { ghosts, ballons, worms }

fun main() {
    window.onload = {
        canvas  = document.body!!.initCanvas()

        document.getElementById("link-game-ghost")?.   addEventListener("click", { switchGame(GameCase.ghosts ) } )
        document.getElementById("link-game-balloons")?.addEventListener("click", { switchGame(GameCase.ballons) } )
        document.getElementById("link-game-wiggly")?.  addEventListener("click", { switchGame(GameCase.worms  ) } )

        switchGame(GameCase.ghosts)

    }

    window.onresize = { canvas.resizeToWindow() }

}

fun switchGame(gameToRun: GameCase) {
    game?.finishGame()

    val zoomOut = 1

    val screenFrame: Frame = Frame(v(0,0), v(canvas.width, canvas.height))
    println(screenFrame)

    val wordSize: Vector = when (gameToRun){
        GameCase.ghosts  -> screenFrame.p1 * 2
        GameCase.ballons -> screenFrame.p1.copy()
        GameCase.worms   -> v(10_000, 10_000)
    }

    val center = wordSize / 2
    val halfOfScreenSize = v(canvas.width.toDouble() / 2.0, canvas.height.toDouble() / 2.0)
    val visibleWordFrame = (screenFrame * zoomOut) + (center - (halfOfScreenSize * zoomOut) )

    val camera = Camera(
        visibleWordFrame = visibleWordFrame,        
        screenFrame = screenFrame,
        wordSize = wordSize,
        percentageOfActiveArea = 0.6
    )

    val size = v(canvas.width, canvas.height)
    game = when(gameToRun) {
        GameCase.ghosts  -> GhostsGame(canvas, size)
        GameCase.ballons -> BalloonsGame(canvas, size)
        GameCase.worms   -> WigglyWormGame(canvas, wordSize, camera)
    }
    game!!.startGame()
}

fun HTMLCanvasElement.resizeToWindow(){
    width  = ((window.innerWidth -10) / gridStep) * gridStep
    height = ((window.innerHeight-10) / gridStep) * gridStep
}

fun Node.initCanvas(): HTMLCanvasElement {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    canvas.width  = window.innerWidth-20
    canvas.height = window.innerHeight-20
    appendChild(canvas)

    return canvas
}

