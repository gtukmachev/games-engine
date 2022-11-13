package tga.gaming.engine.model

import org.w3c.dom.CanvasRenderingContext2D

interface CompositeDrawer : Drawable, ObjCompanion {

    val drawers: List<Drawable>

    override fun draw(ctx: CanvasRenderingContext2D) {
        drawers.forEach {
            it.draw(ctx)
        }
    }

}

