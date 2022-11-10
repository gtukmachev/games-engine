package tga.gaming.game.zombie

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.Node
import tga.gaming.engine.model.v

lateinit var game: ZombieGame

fun main() {
    window.onload = {
        val canvas: HTMLCanvasElement = document.body!!.initCanvas()
        game = ZombieGame(canvas, v(1024, 768))
        game.startGame()
    }

    window.onkeypress = {
        console.log("onkeypress{${it.code}}")
        when (it.code) {
            "KeyP" -> game.pause()
            "KeyR" -> game.run()
        }
    }

}

fun Node.initCanvas(): HTMLCanvasElement {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.canvas.width  = window.innerWidth - 20
    context.canvas.height = window.innerHeight - 20
    appendChild(canvas)

    return canvas
}
