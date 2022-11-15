package tga.gaming.game.zombie.objects

import tga.gaming.engine.drawers.withImageDrawer
import tga.gaming.engine.image.getImage
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.*

class Zombie (
    p: Vector,
    val player: PlayerObj,
    override val r: Double = gridStepD -1,
    override var frame: Frame = Frame.square(r),
) : Obj(p = p),
    CompositeDrawer,
    Moveable
{
    override val drawers = ArrayList<Drawer>()
    private var speed: Vector? = (player.p - p).assignLength(0.001)

    var maxSpeedLen = 5.0
    var speedLen = 0.0

    init {
        angle = (player.p - p).angle()
        withImageDrawer(getImage("game/zombie/img/zombie-0.png"))
    }

    private var speedDirectionTurnsCounter = 0
    override fun move() {
        if (speedLen < maxSpeedLen) { speedLen += 0.0005; speed?.assignLength(speedLen) }

        speedDirectionTurnsCounter++
        if (speedDirectionTurnsCounter == 100) {
            speedDirectionTurnsCounter = 0
            if (speedLen < maxSpeedLen) speedLen++
            speed = (player.p - p).assignLength(speedLen)
        }

        speed?.let{ p += it}
    }
}
