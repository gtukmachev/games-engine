package tga.gaming.game.zombie.objects

import tga.gaming.engine.drawers.withImageDrawer
import tga.gaming.engine.image.getImage
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.*

class Gost (
    p: Vector,
    private val player: PlayerObj,
    override val r: Double = gridStepD -1,
    override var frame: Frame = Frame.square(r),
) : Obj(p = p),
    CompositeDrawer,
    Moveable
{
    override val drawers = ArrayList<Drawer>()
    private var speed: Vector? = (player.p - p).assignLength(0.001)

    private var maxSpeedLen = 5.0
    private var speedLen = 0.0

    init {
        angle = (player.p - p).angle()
        withImageDrawer(getImage("game/zombie/img/zombie-0.png"))
        //withObjPositionDrawer(strokeStyle = "#AD559AFF")
        //withObjFrameDrawer(strokeStyle = "#AD559AFF")
    }

    private var speedDirectionTurnsCounter = 0
    override fun move() {
        if (speedLen < maxSpeedLen) { speedLen += 0.0005; speed?.assignLength(speedLen) }

        speedDirectionTurnsCounter++
        if (speedDirectionTurnsCounter == 50) {
            speedDirectionTurnsCounter = 0
            speed = (player.p - p).assignLength(speedLen)
            angle = speed!!.angle()
        }

        speed?.let{ p += it}
    }
}
