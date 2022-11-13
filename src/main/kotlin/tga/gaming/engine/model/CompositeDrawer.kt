package tga.gaming.engine.model

import org.w3c.dom.CanvasRenderingContext2D

class CompositeItemDoNotReferToTheObject : RuntimeException("This composite item is not for this object")


interface CompositeDrawer : Drawable {

    val drawers: MutableList<Drawer>
    fun add(drawer: Drawer) {
        if (drawer.obj != this) throw CompositeItemDoNotReferToTheObject()
        drawers.add(drawer)
    }

    override fun draw(ctx: CanvasRenderingContext2D) {
        drawers.forEach { it.draw(ctx) }
    }
}

interface Drawer : ObjCompanion {
    fun draw(ctx: CanvasRenderingContext2D)
}

