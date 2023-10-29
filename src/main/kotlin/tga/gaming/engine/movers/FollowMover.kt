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
    maxSpeed: Double = 2.0,
    var target: VectorFunction? = null
) : Mover {

    var maxSpeed2: Double = maxSpeed * maxSpeed
        private set

    var maxSpeed: Double = maxSpeed
        set(value) {
            field = value
            maxSpeed2 = value*value
        }

    override fun move() {
        target?.invoke()?.let { targetPosition: Vector ->
            if (targetPosition == obj.p) return
            obj.setDirectionToTarget(targetPosition)
            if (maxSpeed != 0.0) {
                var speed = targetPosition - obj.p
                if (speed.len2 > maxSpeed2) {
                    speed = obj.direction * maxSpeed
                }
                obj.p += speed
            }
        }
    }

}