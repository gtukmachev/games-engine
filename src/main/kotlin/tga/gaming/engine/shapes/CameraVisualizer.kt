package tga.gaming.engine.shapes

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.camera.Camera
import tga.gaming.engine.model.Drawable
import tga.gaming.engine.model.Obj


class CameraVisualizer(
    val camera: Camera
): Obj(), Drawable {

    override val frame get() = camera.visibleWordFrame
    override fun draw(ctx: CanvasRenderingContext2D) {
        ctx.lineWidth = 1.0
        ctx.strokeStyle = "red"
        with(camera.visibleWordFrame) {
            ctx.strokeRect(p0.x + 1, p0.y + 1, width - 1, height - 1)
        }

        ctx.strokeStyle = "yellow"
        with(camera.activeWordZone) {
            ctx.strokeRect(p0.x, p0.y, width, height)
        }

    }

}
