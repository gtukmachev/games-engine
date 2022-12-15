package tga.gaming.engine.shapes

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.CanvasTextBaseline
import org.w3c.dom.TOP
import tga.gaming.engine.GameWord
import tga.gaming.engine.camera.Camera
import tga.gaming.engine.index.gridStepD
import tga.gaming.engine.model.Drawable
import tga.gaming.engine.model.Obj
import kotlin.math.max
import kotlin.math.min


fun GameWord.withIndexGrid(
    colorPassive: String = "#443c38",
    colorActive: String = "#886134",
    camera: Camera,
): IndexGrid {
    return dispatcher.addObj(IndexGrid(colorPassive, colorActive, camera))
}

class IndexGrid(
    val colorPassive: String,
    val colorActive: String,
    val camera: Camera,
) : Obj(frame = null), Drawable {

    override val isAlwaysVisible = true

    override fun draw(ctx: CanvasRenderingContext2D) {

        val index = this.dispatcher.index
        val range =
            with(camera.visibleWordFrame) {
                index.rangeOf(p0.x, p0.y, p1.x, p1.y)
            } ?: return

        val l0 = max(0, range.lin0)
        val c0 = max(0, range.col0)
        val l1 = min(index.maxLinesIndex, range.lin1)
        val c1 = min(index.maxColumnsIndex, range.col1)
        val size = gridStepD
        var y = l0*size
        for (l in l0 .. l1) {
            var x = c0*size
            for (c in c0 .. c1) {
                ctx.beginPath()
                val count = index.matrix[l][c].size
                ctx.strokeStyle = if ( count > 0) colorActive else colorPassive
                val x0 = x+2
                val y0 = y+2

                ctx.lineWidth = 0.5
                ctx.strokeText("$count",x0+3,y0+3)
                ctx.textBaseline = CanvasTextBaseline.TOP
                ctx.lineWidth = 2.0
                ctx.rect(x0,y0, size-4,size-4)
                ctx.stroke()
                x += size
            }
            y += size
        }


    }
}
