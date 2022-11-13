package tga.gaming.engine

import kotlinx.browser.window
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.render.GameRenderer

open class GameWord(
    val dispatcher: Dispatcher,
    val renderer: GameRenderer,
    var turnDurationMillis: Int = 100
) {

    var active: Boolean = false
        private set

    fun run() {
        if (active) return
        console.log("run()")
        active = true
        window.setTimeout(this::gameLoop, turnDurationMillis)
        window.setTimeout(
            { this.paint(window.performance.now()) },
            turnDurationMillis + turnDurationMillis / 2
        )
    }

    fun pause() {
        console.log("pause()")
        active = false
    }

    private var turnsCounter: Long = 0
    open fun gameLoop() {
        if (!active) return

        turnsCounter++
        val startedAtMillis = window.performance.now()

        dispatcher.turn()

        val finishedAtMillis = window.performance.now()
        var nextRunIn = turnDurationMillis - (finishedAtMillis - startedAtMillis).toInt()
        if (nextRunIn < 0) nextRunIn = 0

        window.setTimeout(this::gameLoop, nextRunIn)
    }

    private var framesCounter: Long = 0
    open fun paint(t: Double) {
        if (!active) return

        framesCounter++
        renderer.paint()

        window.requestAnimationFrame( this::paint )
    }

}
