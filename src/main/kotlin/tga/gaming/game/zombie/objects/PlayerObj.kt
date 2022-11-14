package tga.gaming.game.zombie.objects

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.events.MouseEvent
import tga.gaming.engine.dispatcher.SimpleEventsListener
import tga.gaming.engine.image.getImage
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.*

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
    private var moveIsOn: Boolean = false
    private var speed = 10.0

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
        this.angle = direction.angle()
    }

    override fun onMouseDown(mouseEvent: MouseEvent) = startMove()

    override fun onMouseUp(mouseEvent: MouseEvent)  = stopMove()

    private fun startMove() {
        moveIsOn = true
    }

    private fun stopMove() {
        moveIsOn = false
    }

    override fun act() {
        if (moveIsOn) {
            p.set(p.x + direction.x * speed, p.y + direction.y * speed)
        }
    }
}

