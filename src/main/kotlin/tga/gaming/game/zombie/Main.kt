package tga.gaming.game.zombie

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.Node
import tga.gaming.engine.model.v

fun main() {
    window.onload = {
        val canvas: HTMLCanvasElement = document.body!!.initCanvas()
        draw(canvas)
        val game = ZombieGame(canvas, v(1024, 768))
        game.startGame()
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

fun draw(canvas: HTMLCanvasElement) {
    val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
    ctx.strokeStyle = "green "
    ctx.strokeRect(10.0, 10.0, 100.0, 100.0)
}
