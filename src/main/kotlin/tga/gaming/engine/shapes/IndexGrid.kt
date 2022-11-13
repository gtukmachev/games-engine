package tga.gaming.engine.shapes

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.CanvasTextBaseline
import org.w3c.dom.TOP
import tga.gaming.engine.index.sizeFactor
import tga.gaming.engine.model.Drawable
import tga.gaming.engine.model.Obj

class IndexGrid : Obj(frame = null), Drawable {

    override fun draw(ctx: CanvasRenderingContext2D) {

        val index = this.dispatcher.index
        val size = (1 shl sizeFactor).toDouble()
        var y = 0.0
        for (l in 0 until index.lines) {
            var x = 0.0
            for (c in 0 until index.columns) {
                ctx.beginPath()
                val count = index.matrix[l][c].size
                ctx.strokeStyle = if ( count > 0) "gray" else "black"

                val x0 = x+2
                val y0 = y+2

                ctx.strokeText("$count",x0+3,y0+3)
                ctx.textBaseline = CanvasTextBaseline.TOP
                ctx.rect(x0,y0, size-4,size-4)
                ctx.stroke()
                x += size
            }
            y += size
        }


    }
}