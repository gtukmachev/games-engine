package tga.gaming.game.zombie

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.Node
import tga.gaming.engine.index.gridStep
import tga.gaming.engine.model.v

lateinit var game: ZombieGame

fun main() {
    window.onload = {
        val canvas: HTMLCanvasElement = document.body!!.initCanvas()
        game = ZombieGame(
            canvas,
            wordSize = v(gridStep*25, gridStep*13)
        )
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
    canvas.width  = window.innerWidth - 20
    canvas.height = window.innerHeight - 20
    canvas.style.cssText = """
        border: 0;
    """.trimIndent()
    appendChild(canvas)

    return canvas
}

