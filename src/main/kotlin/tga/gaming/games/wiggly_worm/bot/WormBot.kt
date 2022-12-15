package tga.gaming.games.wiggly_worm.bot

import tga.gaming.engine.model.Actionable
import tga.gaming.engine.model.Obj
import tga.gaming.engine.model.Vector
import tga.gaming.engine.model.v
import tga.gaming.games.wiggly_worm.worms.Worm
import kotlin.math.PI
import kotlin.random.Random

class WormBot(
    val worm: Worm
): Obj(frame = null, r = 0.0), Actionable {

    var targetPoint: Vector = v(1,0)

    val rt = PI/Random.nextDouble(50.0, 150.0).let{
        if (Random.nextBoolean()) it else -it
    }

    override fun act() {
        val currentDirection: Vector = worm.body[0].p - worm.body[1].p
        val norm = currentDirection.norm(quite = true)
        val angle = norm.angle()
        norm.setToAngle(angle + rt)
        targetPoint.set(worm.body[0].p.x + norm.x, worm.body[0].p.y + norm.y)
    }
}