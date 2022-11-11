package tga.gaming.engine.shapes

import org.w3c.dom.CanvasRenderingContext2D
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
                ctx.strokeStyle = if (index.matrix[l][c].size > 0) {
                                        "orange"
                                  } else {
                                        "gray"
                                  }
                ctx.rect(x+2,y+2, size-4,size-4)
                ctx.stroke()
                x += size
            }
            y += size
        }


    }
}
