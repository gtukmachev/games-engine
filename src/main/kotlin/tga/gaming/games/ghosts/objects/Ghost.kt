package tga.gaming.games.ghosts.objects

import tga.gaming.engine.drawers.withImagesDrawer
import tga.gaming.engine.drawers.withObjFrameDrawer
import tga.gaming.engine.image.loadImages
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.*
import tga.gaming.engine.movers.withFollowMover
import kotlin.random.Random.Default.nextInt

class Ghost (
    p: Vector,
    private val player: PlayerObj,
    r: Double = gridStepD -1,
    override var frame: Frame = Frame.square(r),
) : Obj(p = p, r=r),
    CompositeDrawer,
    CompositeMover
{
    override val drawers = ArrayList<Drawer>(2)
    override val movers = ArrayList<Mover>(1)

    init {
        angle = (player.p - p).angle()
        withImagesDrawer(ghostImages, nextInt(6))
        // withObjPositionDrawer(strokeStyle = "#AD559AFF")
        withObjFrameDrawer(strokeStyle = "#AD559AFF")
        withFollowMover(1.0){ player.p }
    }


    companion object {
        val ghostImages = loadImages("game/zombie/img/gost-<n>.gif", 6)
    }

}
