package tga.gaming.games.ghosts.objects

import tga.gaming.engine.drawers.withImagesDrawer
import tga.gaming.engine.image.loadImages
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.*
import kotlin.random.Random.Default.nextInt

class Ghost (
    p: Vector,
    private val player: PlayerObj,
    r: Double = gridStepD -1,
    override var frame: Frame = Frame.square(r),
) : Obj(p = p, r=r),
    CompositeDrawer,
    Moveable
{
    override val drawers = ArrayList<Drawer>()
    private var speed: Vector? = (player.p - p)

    private var maxSpeedLen = 5.0
    private var speedLen = 0.5

    init {
        angle = (player.p - p).angle()
        withImagesDrawer(ghostImages, nextInt(6))
        // withObjPositionDrawer(strokeStyle = "#AD559AFF")
        // withObjFrameDrawer(strokeStyle = "#AD559AFF")
    }

    private var speedDirectionTurnsCounter = 0
    override fun move() {
        if (speedLen < maxSpeedLen) { speedLen += 0.0005; speed?.assignLength(speedLen) }

        speedDirectionTurnsCounter++
        if (speedDirectionTurnsCounter == 3) {
            speedDirectionTurnsCounter = 0
            speed = (player.p - p).assignLength(speedLen)
            angle = speed!!.angle()
        }

        speed?.let{ p += it}
    }

    companion object {
        val ghostImages = loadImages("game/zombie/img/gost-<n>.gif", 6)
    }

}
