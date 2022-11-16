package tga.gaming.game.zombie.objects

import tga.gaming.engine.drawers.withImageDrawer
import tga.gaming.engine.image.loadImage
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.*
import kotlin.math.PI
import kotlin.math.sin

class KotlinSign(
    p: Vector = v(),
) : Obj(p = p, r = gridStepD-5.0),
    CompositeDrawer,
    Actionable
{
    override val drawers = mutableListOf<Drawer>()
    private var t = 0.0

    init {
        withImageDrawer(loadImage("/game/zombie/img/kotlin-logo.svg"))
    }

    override fun act() {
        t += 0.1
        angle = amplitude * sin(t)
    }

    companion object {
        const val amplitude = PI /4
    }
}
