package tga.gaming.games.wiggly_worm.objects

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.GameWord
import tga.gaming.engine.PI2
import tga.gaming.engine.model.Drawable
import tga.gaming.engine.model.Obj

class CirclesBackground(
    game: GameWord
): Obj(), Drawable {

    val size = game.wordSize
    val center = size / 2
    override val isAlwaysVisible = true


    override fun draw(ctx: CanvasRenderingContext2D) {

        ctx.lineWidth = 1.0
        ctx.strokeStyle = "#204041FF"

        var radiusInc = 30.0
        var radius = 10.0
        var n = 0

        while (radius < center.len) {
            //radiusInc += 5
            ctx.beginPath()
            ctx.arc(center.x, center.y, radius, 0.0, PI2)
            ctx.stroke()
            n++
            radius += radiusInc
            radiusInc += 10
        }

        ctx.lineWidth=12.0
        ctx.strokeRect(-5.0, -5.0, size.x+5, size.y+5)


    }
}