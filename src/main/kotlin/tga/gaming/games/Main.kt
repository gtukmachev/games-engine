package tga.gaming.games

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.Node
import tga.gaming.engine.GameWord
import tga.gaming.engine.model.v
import tga.gaming.games.zombie.ZombieGame

lateinit var game: GameWord
lateinit var canvas: HTMLCanvasElement

fun main() {
    window.onload = {
        canvas  = document.body!!.initCanvas()
        game = ZombieGame(
            canvas,
            wordSize = v(canvas.width, canvas.height)
        )
/*
        game = BalloonsGame(
            canvas,
            wordSize = v(canvas.width, canvas.height)
        )
*/
        game.startGame()
    }

    window.onresize = { canvas.resizeToWindow() }

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

