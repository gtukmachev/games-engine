package tga.gaming.games.ghosts.objects

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import tga.gaming.engine.dispatcher.SimpleEventsListener
import tga.gaming.engine.drawers.withImagesDrawer
import tga.gaming.engine.image.loadImages
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.*
import tga.gaming.engine.movers.VectorFunction
import tga.gaming.engine.movers.withFollowMover

fun playerObj(
    p: Vector,
    bounds: Vector,
    targetPoint: VectorFunction
): PlayerObj {
    val player = PlayerObj(p, bounds = bounds).apply {
        withFollowMover(0.0, target = targetPoint)
    }
    return player
}

class PlayerObj(
    p: Vector,
    r: Double = gridStepD-2,
    override var frame: Frame = Frame.square(r),
    val bounds: Vector
) : Obj(p = p, r = r),
    CompositeDrawer,
    CompositeMover,
    SimpleEventsListener,
    Moveable,
    Actionable
{
    override val drawers = ArrayList<Drawer>(2)
    override val movers = ArrayList<Mover>(1)
    private var speed: Vector? = null

    private var isUpKeyPressed    = false
    private var isRightKeyPressed = false
    private var isDownKeyPressed  = false
    private var isLeftKeyPressed  = false

    val maxVisibility: Double = 100.0
    var visibility: Double = maxVisibility

    val imagesDrawer = withImagesDrawer(playerImages)

    override fun draw(ctx: CanvasRenderingContext2D) {
        ctx.globalAlpha = when {
                visibility <= 0 -> 0.0
                visibility > 0 && visibility <= maxVisibility -> visibility / maxVisibility
                else -> 1.0
            }
        super.draw(ctx)
        ctx.globalAlpha = 1.0
    }

    override fun onKeyDown(ke: KeyboardEvent) {
        when (ke.code) {
            "KeyW" -> if (!isUpKeyPressed )   { isUpKeyPressed    = true; updateSpeed() }
            "KeyD" -> if (!isRightKeyPressed) { isRightKeyPressed = true; updateSpeed() }
            "KeyS" -> if (!isDownKeyPressed ) { isDownKeyPressed  = true; updateSpeed() }
            "KeyA" -> if (!isLeftKeyPressed ) { isLeftKeyPressed  = true; updateSpeed() }
        }
    }

    override fun onKeyUp(ke: KeyboardEvent) {
        when (ke.code) {
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
            dx ==  0 && dy == -1 -> speed = speedVectorUp
            dx ==  1 && dy == -1 -> speed = speedVectorUpRight
            dx ==  1 && dy ==  0 -> speed = speedVectorRight
            dx ==  1 && dy ==  1 -> speed = speedVectorDownRight
            dx ==  0 && dy ==  1 -> speed = speedVectorDown
            dx == -1 && dy ==  1 -> speed = speedVectorDownLeft
            dx == -1 && dy ==  0 -> speed = speedVectorLeft
            dx == -1 && dy == -1 -> speed = speedVectorUpLeft
        }

    }

    override fun onClick(me: MouseEvent) {
        imagesDrawer.nextImage()
    }

    override fun move() {
        if (speed != null) {
            p += speed!!
            if (p.x > bounds.x) p.x = bounds.x else if (p.x < 0.0) p.x = 0.0
            if (p.y > bounds.y) p.y = bounds.y else if (p.y < 0.0) p.y = 0.0
        }
        super.move()
    }

    override fun act() {
        if (visibility > 0) {
            dispatcher.index.objectsOnTheSamePlaceWith(this).forEach {
                when (it) {
                    is Ghost -> {
                        visibility -= 10
                        dispatcher.delObj(it)
                    }
                    is KotlinSign -> {
                        dispatcher.delObj(it)
                        visibility = maxVisibility
                    }

                }
            }
        }
    }

    companion object {
        val playerImages = loadImages("/game/zombie/img/actor<n>.png", 11)

        private val playerSpeedLen = 5

        private val speedVectorUp        = vUp * playerSpeedLen
        private val speedVectorUpRight   = vUpRight * playerSpeedLen
        private val speedVectorRight     = vRight * playerSpeedLen
        private val speedVectorDownRight = vDownRight * playerSpeedLen
        private val speedVectorDown      = vDown * playerSpeedLen
        private val speedVectorDownLeft  = vDownLeft * playerSpeedLen
        private val speedVectorLeft      = vLeft * playerSpeedLen
        private val speedVectorUpLeft    = vUpLeft * playerSpeedLen

    }
}


