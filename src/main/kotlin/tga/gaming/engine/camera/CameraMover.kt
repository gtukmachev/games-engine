package tga.gaming.engine.camera

import tga.gaming.engine.model.CompositeMover
import tga.gaming.engine.model.Mover
import tga.gaming.engine.model.Obj

fun CompositeMover.addCameraMover(camera: Camera): CameraMover {
    val mover = CameraMover(this as Obj, camera)
    this.movers.add(mover)
    return mover
}

inline fun <reified T: CompositeMover> T.withCameraMover(camera: Camera): T {
    val mover = CameraMover(this as Obj, camera)
    this.movers.add(mover)
    return this
}


class CameraMover(
    override val obj: Obj,
    private val camera: Camera
) : Mover {

    override fun move() {
        camera.arrangePositionTo(obj)
    }

}