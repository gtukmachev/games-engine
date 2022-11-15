package tga.gaming.engine

import kotlinx.browser.window
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.render.GameRenderer

open class GameWord(
    val dispatcher: Dispatcher,
    val renderer: GameRenderer,
    var turnDurationMillis: Int = 10
) {

    private var gameLoopHandler: Int = -1

    var active: Boolean = false
        private set

    fun run() {
        if (active) return
        console.log("run()")
        active = true
        runEventListeners()
        gameLoopHandler = window.setInterval(this::gameLoop, turnDurationMillis)
        window.setTimeout(
            { this.paint(window.performance.now()) },
            turnDurationMillis + turnDurationMillis / 2
        )
    }

    fun pause() {
        console.log("pause()")
        active = false
        if (gameLoopHandler != -1) window.clearInterval(gameLoopHandler)
        gameLoopHandler = -1
        stopEventListeners()
    }

    private fun runEventListeners() {
        window.onmousemove = {    mouseEvent: MouseEvent    -> propagateOnMouseMove(mouseEvent)   }
        window.onmousedown = {    mouseEvent: MouseEvent    -> propagateOnMouseDown(mouseEvent)   }
        window.onmouseup   = {    mouseEvent: MouseEvent    -> propagateOnMouseUp(mouseEvent)     }
        window.onclick     = {    mouseEvent: MouseEvent    -> propagateOnClick(mouseEvent)       }
        window.ondblclick  = {    mouseEvent: MouseEvent    -> propagateOnDblClick(mouseEvent)    }
        window.onkeypress  = { keyboardEvent: KeyboardEvent -> propagateOnKeyPress(keyboardEvent) }
        window.onkeydown   = { keyboardEvent: KeyboardEvent -> propagateOnKeyDown(keyboardEvent)  }
        window.onkeyup     = { keyboardEvent: KeyboardEvent -> propagateOnKeyUp(keyboardEvent)    }
    }

    open fun propagateOnMouseMove(   mouseEvent: MouseEvent   ) { dispatcher.onMouseMove(   mouseEvent) }
    open fun propagateOnMouseDown(   mouseEvent: MouseEvent   ) { dispatcher.onMouseDown(   mouseEvent) }
    open fun propagateOnMouseUp  (   mouseEvent: MouseEvent   ) { dispatcher.onMouseUp  (   mouseEvent) }
    open fun propagateOnClick    (   mouseEvent: MouseEvent   ) { dispatcher.onClick    (   mouseEvent) }
    open fun propagateOnDblClick (   mouseEvent: MouseEvent   ) { dispatcher.onDblClick (   mouseEvent) }
    open fun propagateOnKeyPress (keyboardEvent: KeyboardEvent) { dispatcher.onKeyPress (keyboardEvent) }
    open fun propagateOnKeyDown  (keyboardEvent: KeyboardEvent) { dispatcher.onKeyDown  (keyboardEvent) }
    open fun propagateOnKeyUp    (keyboardEvent: KeyboardEvent) { dispatcher.onKeyUp    (keyboardEvent) }

    private fun stopEventListeners() {
        TODO("Not yet implemented")
    }

    private var turnsCounter: Long = 0
    open fun gameLoop() {
        if (!active) return

        turnsCounter++
        //val startedAtMillis = window.performance.now()

        dispatcher.turn()

        //val finishedAtMillis = window.performance.now()
        //var nextRunIn = turnDurationMillis - (finishedAtMillis - startedAtMillis).toInt()
        //if (nextRunIn < 0) nextRunIn = 0

        //window.setTimeout(this::gameLoop, nextRunIn)
    }

    private var framesCounter: Long = 0
    open fun paint(t: Double) {
        if (!active) return

        framesCounter++
        renderer.paint()

        window.requestAnimationFrame( this::paint )
    }

}
