package tga.gaming.games

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.Node
import tga.gaming.engine.GameWord
import tga.gaming.engine.model.v
import tga.gaming.games.balloons.BalloonsGame
import tga.gaming.games.wiggly_worm.WigglyWorm
import tga.gaming.games.zombie.ZombieGame

var game: GameWord? = null
lateinit var canvas: HTMLCanvasElement

fun main() {
    window.onload = {
        canvas  = document.body!!.initCanvas()

        document.getElementById("link-game-ghost")?.   addEventListener("click", {switchGame("Ghosts")    })
        document.getElementById("link-game-balloons")?.addEventListener("click", {switchGame("Balloons")  })
        document.getElementById("link-game-wiggly")?.  addEventListener("click", {switchGame("WigglyWorm")})

        switchGame("Balloons")

    }

    window.onresize = { canvas.resizeToWindow() }

}

fun switchGame(gameName: String) {
    game?.finishGame()
    val size = v(canvas.width, canvas.height)
    game = when(gameName) {
        "Ghosts"   -> ZombieGame(canvas, size)
        "Balloons" -> BalloonsGame(canvas, size)
        "WigglyWorm" -> WigglyWorm(canvas, size)
        else -> throw RuntimeException("unsupported game name!")
    }
    game!!.startGame()
}

fun HTMLCanvasElement.resizeToWindow(){
    width  = window.innerWidth-20
    height = window.innerHeight-20
}

fun Node.initCanvas(): HTMLCanvasElement {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    canvas.width  = window.innerWidth-20
    canvas.height = window.innerHeight-20
    appendChild(canvas)

    return canvas
}

