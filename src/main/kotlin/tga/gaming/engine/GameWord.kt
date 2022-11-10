package tga.gaming.engine

import kotlinx.browser.window
import tga.gaming.engine.dispatcher.GameDispatcher
import tga.gaming.engine.index.SquareIndex
import tga.gaming.engine.render.GameRenderer

open class GameWord(
    val dispatcher: GameDispatcher,
    val index: SquareIndex,
    val renderer: GameRenderer
) {
    var turnDurationMillis: Int = 1000

    var active: Boolean = false
        private set

    var wereChanges: Boolean = false

    fun run() {
        if (active) return
        active = true
        window.setTimeout(this::gameLoop, turnDurationMillis)
        window.setTimeout(
            { this.renderGameFrame(window.performance.now()) },
            turnDurationMillis + turnDurationMillis / 2
        )

    }

    private var turnsCounter: Long = 0
    open fun gameLoop() {
        if (!active) return

        turnsCounter++
        val startedAtMillis = window.performance.now()

        dispatcher.turn(this)
        wereChanges = true

        val finishedAtMillis = window.performance.now()
        var nextRunIn = turnDurationMillis - (finishedAtMillis - startedAtMillis).toInt()
        if (nextRunIn < 0) nextRunIn = 0

        window.setTimeout(this::gameLoop, nextRunIn)
    }

    private var framesCounter: Long = 0
    open fun renderGameFrame(t: Double) {
        if (!active) return

        if (wereChanges) {
            wereChanges = false
            framesCounter++
            renderer.paint()
        }

        window.requestAnimationFrame( this::renderGameFrame )
    }

 }
