package tga.gaming.engine

import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.TouchEvent
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.pointerevents.PointerEvent
import tga.gaming.engine.dispatcher.Dispatcher
import tga.gaming.engine.render.GameRenderer

open class GameWord(
    val canvas: HTMLCanvasElement,
    val dispatcher: Dispatcher,
    val renderer: GameRenderer,
    var turnDurationMillis: Int = 10
) {

    private var gameLoopHandler: Int = -1

    var active: Boolean = false
        private set

    open fun startGame() {
        run()
    }

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
        window.addEventListener("touchmove",   {e -> propagateTouchMove  (e as TouchEvent)} , false)
        window.addEventListener("touchend",    {e -> propagateTouchEnd   (e as TouchEvent)} , false)
        window.addEventListener("touchstart",  {e -> propagateTouchStart (e as TouchEvent)} , false)
        window.addEventListener("touchcancel", {e -> propagateTouchCancel(e as TouchEvent)} , false)

        window.onmousemove = {me: MouseEvent -> propagateOnMouseMove(me)  }
        window.onmousedown = {me: MouseEvent -> propagateOnMouseDown(me)  }
        window.onmouseup   = {me: MouseEvent -> propagateOnMouseUp(me)    }
        window.onmouseenter= {me: MouseEvent -> propagateOnMouseEnter(me) }
        window.onmouseleave= {me: MouseEvent -> propagateOnMouseLeave(me) }

        window.ongotpointercapture  = { pe: PointerEvent -> propagateOnGotPointerCapture(pe) }
        window.onlostpointercapture = { pe: PointerEvent -> propagateOnLostPointerCapture(pe) }
        window.onpointerdown        = { pe: PointerEvent -> propagateOnPointerDown(pe) }
        window.onpointermove        = { pe: PointerEvent -> propagateOnPointerMove(pe) }
        window.onpointerup          = { pe: PointerEvent -> propagateOnPointerUp(pe) }
        window.onpointercancel      = { pe: PointerEvent -> propagateOnPointerCancel(pe) }
        window.onpointerover        = { pe: PointerEvent -> propagateOnPointerOver(pe) }
        window.onpointerout         = { pe: PointerEvent -> propagateOnPointerOut(pe) }
        window.onpointerenter       = { pe: PointerEvent -> propagateOnPointerEnter(pe) }
        window.onpointerleave       = { pe: PointerEvent -> propagateOnPointerLeave(pe) }

        window.onclick   = {me: MouseEvent -> propagateOnClick(me) }
        window.ondblclick= {me: MouseEvent -> propagateOnDblClick(me) }

        window.onkeypress  = {ke: KeyboardEvent -> propagateOnKeyPress(ke) }
        window.onkeydown   = {ke: KeyboardEvent -> propagateOnKeyDown(ke)  }
        window.onkeyup     = {ke: KeyboardEvent -> propagateOnKeyUp(ke)    }

    }

    open fun propagateTouchMove  (e: TouchEvent) { dispatcher.onTouchMove(e) }
    open fun propagateTouchEnd   (e: TouchEvent) { dispatcher.onTouchEnd(e) }
    open fun propagateTouchStart (e: TouchEvent) { dispatcher.onTouchStart(e) }
    open fun propagateTouchCancel(e: TouchEvent) { dispatcher.onTouchCancel(e) }

    open fun propagateOnMouseMove (me: MouseEvent) { dispatcher.onMouseMove (me) }
    open fun propagateOnMouseDown (me: MouseEvent) { dispatcher.onMouseDown (me) }
    open fun propagateOnMouseUp   (me: MouseEvent) { dispatcher.onMouseUp   (me) }
    open fun propagateOnMouseEnter(me: MouseEvent) { dispatcher.onMouseEnter(me) }
    open fun propagateOnMouseLeave(me: MouseEvent) { dispatcher.onMouseLeave(me) }

    open fun propagateOnGotPointerCapture (pe: PointerEvent){ dispatcher.onGotPointerCapture(pe) }
    open fun propagateOnLostPointerCapture(pe: PointerEvent){ dispatcher.onLostPointerCapture(pe) }
    open fun propagateOnPointerDown       (pe: PointerEvent){ dispatcher.onPointerDown(pe) }
    open fun propagateOnPointerMove       (pe: PointerEvent){ dispatcher.onPointerMove(pe) }
    open fun propagateOnPointerUp         (pe: PointerEvent){ dispatcher.onPointerUp(pe) }
    open fun propagateOnPointerCancel     (pe: PointerEvent){ dispatcher.onPointerCancel(pe) }
    open fun propagateOnPointerOver       (pe: PointerEvent){ dispatcher.onPointerOver(pe) }
    open fun propagateOnPointerOut        (pe: PointerEvent){ dispatcher.onPointerOut(pe) }
    open fun propagateOnPointerEnter      (pe: PointerEvent){ dispatcher.onPointerEnter(pe) }
    open fun propagateOnPointerLeave      (pe: PointerEvent){ dispatcher.onPointerLeave(pe) }

    open fun propagateOnClick   (me: MouseEvent) { dispatcher.onClick   (me) }
    open fun propagateOnDblClick(me: MouseEvent) { dispatcher.onDblClick(me) }

    open fun propagateOnKeyDown  (ke: KeyboardEvent) { dispatcher.onKeyDown  (ke) }
    open fun propagateOnKeyUp    (ke: KeyboardEvent) { dispatcher.onKeyUp    (ke) }
    open fun propagateOnKeyPress (ke: KeyboardEvent) {
        handleCommonKeys(ke)
        dispatcher.onKeyPress (ke)
    }

    private fun handleCommonKeys(keyboardEvent: KeyboardEvent) {
        when (keyboardEvent.code) {
            "KeyP" -> pause()
            "KeyR" -> run()
        }
    }

    private fun stopEventListeners() {
        //TODO("Not yet implemented")
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

    fun finishGame() {
        pause()
        dispatcher.finishGame()
    }

}
