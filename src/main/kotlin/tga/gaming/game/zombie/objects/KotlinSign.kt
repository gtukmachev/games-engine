package tga.gaming.game.zombie.objects

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.image.getImage
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

    val image = getImage("/game/zombie/img/kotlin-logo.svg")

    override fun draw(ctx: CanvasRenderingContext2D) {
        ctx.drawImage(image, frame!!.p0.x, frame.p0.y, r*2, r*2)
        super.draw(ctx)
    }

    var t = 0.0
    override fun act() {
        t += 0.1
        angle = amplitude * sin(t)
    }

    companion object {
        const val amplitude = PI /4
    }
}
