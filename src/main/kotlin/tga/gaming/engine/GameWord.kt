package tga.gaming.engine

import kotlinx.browser.window
import tga.gaming.engine.dispatcher.GameDispatcher
import tga.gaming.engine.index.SquareIndex
import tga.gaming.engine.internal.ObjectsByAspectCollection
import tga.gaming.engine.model.Obj
import tga.gaming.engine.model.TurnContext
import tga.gaming.engine.render.GameRenderer
import kotlin.js.Date

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

    var turnsCounter: Long = 0
    open fun gameLoop() {
        if (!active) return

        turnsCounter++
        val ctx = GlobalTurnContext(
            startedAtMillis = Date().getUTCMilliseconds(),
            index = this.index,
            objects = this.dispatcher
        )

        //dispatcher.turn(ctx)
        wereChanges = true
        console.log("turn $turnsCounter")

        ctx.finishedAtMillis = Date().getUTCMilliseconds()
        var nextRunIn = turnDurationMillis - (ctx.finishedAtMillis - ctx.startedAtMillis)
        if (nextRunIn < 0) nextRunIn = 0

        window.setTimeout(this::gameLoop, nextRunIn)
    }

    var framesCounter: Long = 0
    open fun renderGameFrame(t: Double) {
        if (!active) return

        if (wereChanges) {
            wereChanges = false
            framesCounter++
            renderer.paint()
        }

        window.requestAnimationFrame( this::renderGameFrame )
    }

    class GlobalTurnContext(
        val startedAtMillis: Int,
        override val index: SquareIndex,
        override val objects: ObjectsByAspectCollection,
        var finishedAtMillis: Int= 0,
    ) : TurnContext {
        override fun addObj(obj: Obj) {
            TODO("Not yet implemented")
        }

        override fun delObj(obj: Obj) {
            TODO("Not yet implemented")
        }
    }
}
