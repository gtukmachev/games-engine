package tga.gaming.engine.movers

import org.w3c.dom.events.KeyboardEvent
import tga.gaming.engine.model.*

fun CompositeMover.addKeyboardAwsdMover(maxSpeed: Double = 2.0, bounds: Frame): KeyboardArrowsMover {
    val mover = KeyboardArrowsMover(this as Obj, maxSpeed, bounds)
    this.movers.add(mover)
    return mover
}
inline fun <reified T: CompositeMover> T.withKeyboardAwsdMover(maxSpeed: Double = 2.0, bounds: Frame): T {
    val mover = KeyboardArrowsMover(this as Obj, maxSpeed, bounds)
    this.movers.add(mover)
    return this
}

fun CompositeMover.addKeyboardArrowsMover(maxSpeed: Double = 2.0, bounds: Frame): KeyboardArrowsMover {
    val mover = KeyboardArrowsMover(this as Obj, maxSpeed, bounds,
        keyUp    = "BracketLeft",
        keyRight = "Backslash",
        keyDown  = "Quote",
        keyLeft  = "Semicolon"
    )
    this.movers.add(mover)
    return mover
}

inline fun <reified T: CompositeMover> T.withKeyboardArrowsMoverAWSD(maxSpeed: Double = 2.0, bounds: Frame): T {
    val mover = KeyboardArrowsMover(this as Obj, maxSpeed, bounds,
        keyUp    = "BracketLeft",
        keyRight = "Backslash",
        keyDown  = "Quote",
        keyLeft  = "Semicolon"
    )
    this.movers.add(mover)
    return this
}

open class KeyboardArrowsMover(
    override val obj: Obj,
    private val maxSpeed: Double = 1.0,
    private val bounds: Frame,
    private val keyUp   : String = "KeyW",
    private val keyRight: String = "KeyD",
    private val keyDown : String = "KeyS",
    private val keyLeft : String = "KeyA"
) : Mover {


    private var speed: Vector? = null

    private var isUpKeyPressed    = false
    private var isRightKeyPressed = false
    private var isDownKeyPressed  = false
    private var isLeftKeyPressed  = false

    fun onKeyDown(ke: KeyboardEvent) {
        when (ke.code) {
            keyUp    -> if (!isUpKeyPressed )   { isUpKeyPressed    = true; updateSpeed() }
            keyRight -> if (!isRightKeyPressed) { isRightKeyPressed = true; updateSpeed() }
            keyDown  -> if (!isDownKeyPressed ) { isDownKeyPressed  = true; updateSpeed() }
            keyLeft  -> if (!isLeftKeyPressed ) { isLeftKeyPressed  = true; updateSpeed() }
        }
    }

    fun onKeyUp(ke: KeyboardEvent) {
        when (ke.code) {
            keyUp    -> if (isUpKeyPressed)    { isUpKeyPressed    = false; updateSpeed() }
            keyRight -> if (isRightKeyPressed) { isRightKeyPressed = false; updateSpeed() }
            keyDown  -> if (isDownKeyPressed)  { isDownKeyPressed  = false; updateSpeed() }
            keyLeft  -> if (isLeftKeyPressed)  { isLeftKeyPressed  = false; updateSpeed() }
        }
    }

    private fun updateSpeed() {
        val dx: Int = when {
            isRightKeyPressed == isLeftKeyPressed -> 0
            isRightKeyPressed                     -> 1
            isLeftKeyPressed                      -> -1
            else                                  -> 0
        }

        val dy: Int = when {
            isUpKeyPressed == isDownKeyPressed -> 0
            isDownKeyPressed                   -> 1
            isUpKeyPressed                     -> -1
            else                               -> 0
        }

        when {
            dx ==  0 && dy ==  0 -> speed = null
            dx ==  0 && dy == -1 -> speed = vUp        * maxSpeed
            dx ==  1 && dy == -1 -> speed = vUpRight   * maxSpeed
            dx ==  1 && dy ==  0 -> speed = vRight     * maxSpeed
            dx ==  1 && dy ==  1 -> speed = vDownRight * maxSpeed
            dx ==  0 && dy ==  1 -> speed = vDown      * maxSpeed
            dx == -1 && dy ==  1 -> speed = vDownLeft  * maxSpeed
            dx == -1 && dy ==  0 -> speed = vLeft      * maxSpeed
            dx == -1 && dy == -1 -> speed = vUpLeft    * maxSpeed
        }
    }


    override fun move() {
        if (speed != null) {
            val p = obj.p
            p += speed!!
            if      (p.x > bounds.p1.x) p.x = bounds.p1.x
            else if (p.x < bounds.p0.x) p.x = bounds.p0.x
            if      (p.y > bounds.p1.y) p.y = bounds.p1.y
            else if (p.y < bounds.p0.y) p.y = bounds.p0.y
        }
    }

}