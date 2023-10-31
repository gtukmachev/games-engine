package tga.gaming.games.ghosts.objects

import tga.gaming.engine.drawers.withCircleDrawer
import tga.gaming.engine.model.*

class Ball(
    p: Vector,
    var speed: Vector,
    acceleration: Double = 3.5
): Obj(p = p, r = 15.0), CompositeDrawer, Moveable, Actionable {

    override val drawers = ArrayList<Drawer>(1)
    private var turnsToLive = 100
    private val accelerationVector = speed.norm() * acceleration

    init {
        withCircleDrawer(radius = r)
    }

    override fun move() {
        p += speed
        speed.plusAssign(accelerationVector)
    }

    override fun act() {
        turnsToLive--
        if (turnsToLive <= 0) { dispatcher.delObj(this); return }

        dispatcher.index.objectsOnTheSamePlaceWith(this)
            .forEach { ghost ->
                if (ghost is Ghost) {
                    ghost.disappear()
                }
            }

    }
}