package tga.gaming.game.zombie.objects

import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import tga.gaming.engine.dispatcher.SimpleEventsListener
import tga.gaming.engine.drawers.withImagesDrawer
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.*
import tga.gaming.engine.model.Vector.Companion.angle_90
import kotlin.math.PI

fun playerObj(
    p: Vector,
    bounds: Vector
): PlayerObj {
    val player = PlayerObj(p, bounds = bounds).apply {
        //withObjFrameDrawer()
        //withObjPositionDrawer()
    }
    return player
}

class PlayerObj(
    p: Vector,
    override val r: Double = gridStepD-1,
    override var frame: Frame = Frame.square(r),
    val bounds: Vector
) : Obj(p = p),
    CompositeDrawer,
//    Actionable,
    SimpleEventsListener,
    Moveable
{
    override val drawers = ArrayList<Drawer>()

    private var direction: Vector = v(1,0)
    private var speed: Vector? = null

    private var isUpKeyPressed    = false
    private var isRightKeyPressed = false
    private var isDownKeyPressed  = false
    private var isLeftKeyPressed  = false

    private val imagesDrawer = withImagesDrawer("/game/zombie/img/actor<n>.png", 11)

    override fun onMouseMove(mouseEvent: MouseEvent) {
        val toMouse = v(mouseEvent.x - p.x, mouseEvent.y - p.y)
        direction.set(toMouse.norm())
        angle = direction.angle()
        when {
            (angle >  angle_90) -> angle += PI
            (angle < -angle_90) -> angle += PI
        }
    }

    override fun onKeyDown(keyboardEvent: KeyboardEvent) {
        when (keyboardEvent.code) {
            "KeyW" -> if (!isUpKeyPressed )   { isUpKeyPressed    = true; updateSpeed() }
            "KeyD" -> if (!isRightKeyPressed) { isRightKeyPressed = true; updateSpeed() }
            "KeyS" -> if (!isDownKeyPressed ) { isDownKeyPressed  = true; updateSpeed() }
            "KeyA" -> if (!isLeftKeyPressed ) { isLeftKeyPressed  = true; updateSpeed() }
        }
    }

    override fun onKeyUp(keyboardEvent: KeyboardEvent) {
        when (keyboardEvent.code) {
            "KeyW" -> if (isUpKeyPressed)    { isUpKeyPressed    = false; updateSpeed() }
            "KeyD" -> if (isRightKeyPressed) { isRightKeyPressed = false; updateSpeed() }
            "KeyS" -> if (isDownKeyPressed)  { isDownKeyPressed  = false; updateSpeed() }
            "KeyA" -> if (isLeftKeyPressed)  { isLeftKeyPressed  = false; updateSpeed() }
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
            dx ==  0 && dy == -1 -> speed = vUp * 5
            dx ==  1 && dy == -1 -> speed = vUpRight * 5
            dx ==  1 && dy ==  0 -> speed = vRight * 5
            dx ==  1 && dy ==  1 -> speed = vDownRight * 5
            dx ==  0 && dy ==  1 -> speed = vDown * 5
            dx == -1 && dy ==  1 -> speed = vDownLeft * 5
            dx == -1 && dy ==  0 -> speed = vLeft * 5
            dx == -1 && dy == -1 -> speed = vUpLeft * 5
        }

    }

    override fun onClick(mouseEvent: MouseEvent) {
        imagesDrawer.nextImage()
    }

    override fun move() {
        if (speed != null) {
            p += speed!!
            if (p.x > bounds.x) p.x = bounds.x else if (p.x < 0.0) p.x = 0.0
            if (p.y > bounds.y) p.y = bounds.y else if (p.y < 0.0) p.y = 0.0
        }
    }

}

