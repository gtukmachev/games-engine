package tga.gaming.game.zombie.objects

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import tga.gaming.engine.dispatcher.SimpleEventsListener
import tga.gaming.engine.image.getImage
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.*
import tga.gaming.engine.model.Vector.Companion.angle_90
import kotlin.math.PI

fun playerObj(
    p: Vector,
    imageIndex: Int
): PlayerObj {
    val player = PlayerObj(p, imageIndex).apply {
        //withObjFrameDrawer()
        //withObjPositionDrawer()
    }
    return player
}

class PlayerObj(
    p: Vector,
    var imageIndex: Int,
    override val r: Double = gridStepD-1,
    override var frame: Frame = Frame.square(r)
) : Obj(p = p),
    CompositeDrawer,
    Actionable,
    SimpleEventsListener
{
    override val drawers = ArrayList<Drawer>()
    private val images = Array(11) { getImage("/game/zombie/img/actor${it+1}.png") }

    private var direction: Vector = v(1,0)
    private var speed: Vector? = null

    private var isUpKeyPressed    = false
    private var isRightKeyPressed = false
    private var isDownKeyPressed  = false
    private var isLeftKeyPressed  = false

    override fun draw(ctx: CanvasRenderingContext2D) {
        val image = when {
            imageIndex < 0            -> images.first()
            imageIndex >= images.size -> images.last()
            else -> images[imageIndex]
        }
        ctx.drawImage(image, frame.p0.x, frame.p0.y, frame.width, frame.height)


        super.draw(ctx)
    }

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
            dx ==  0 && dy == -1 -> speed = vUp * 3
            dx ==  1 && dy == -1 -> speed = vUpRight * 3
            dx ==  1 && dy ==  0 -> speed = vRight * 3
            dx ==  1 && dy ==  1 -> speed = vDownRight * 3
            dx ==  0 && dy ==  1 -> speed = vDown * 3
            dx == -1 && dy ==  1 -> speed = vDownLeft * 3
            dx == -1 && dy ==  0 -> speed = vLeft * 3
            dx == -1 && dy == -1 -> speed = vUpLeft * 3
        }

    }

    override fun onClick(mouseEvent: MouseEvent) {
        imageIndex += 1
        if (imageIndex == images.size) imageIndex = 0
    }

    override fun act() {
        if (speed != null) {
            p += speed!!
        }
    }

}

