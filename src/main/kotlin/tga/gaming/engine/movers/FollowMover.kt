package tga.gaming.engine.movers

import tga.gaming.engine.model.CompositeMover
import tga.gaming.engine.model.Mover
import tga.gaming.engine.model.Obj
import tga.gaming.engine.model.Vector


typealias VectorFunction = () -> Vector?

fun CompositeMover.addFollowMover(maxSpeed: Double = 2.0, setObjAngle: Boolean = true, target: VectorFunction? = null): FollowMover {
    val mover = FollowMover(this as Obj, setObjAngle, maxSpeed, target)
    this.movers.add(mover)
    return mover
}

inline fun <reified T: CompositeMover> T.withFollowMover(maxSpeed: Double = 2.0, setObjAngle: Boolean = true, noinline target: VectorFunction): T {
    val mover = FollowMover(this as Obj, setObjAngle, maxSpeed, target)
    this.movers.add(mover)
    return this
}

class FollowMover(
    override val obj: Obj,
    var setObjAngle: Boolean = true,
    var maxSpeed: Double = 2.0,
    var target: VectorFunction? = null
) : Mover {

    override fun move() {
        target?.invoke()?.let { targetPosition: Vector ->
            val speed = targetPosition - obj.p
            if (speed.len > maxSpeed) {
                speed.normalizeThis()
                speed *= maxSpeed
            }
            obj.p += speed

            if (setObjAngle) {
                obj.angle = speed.angle()
            }
        }


    }
}