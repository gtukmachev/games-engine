package tga.gaming.games.wiggly_worm

import tga.gaming.engine.drawers.withCircleDrawer
import tga.gaming.engine.model.CompositeDrawer
import tga.gaming.engine.model.Drawer
import tga.gaming.engine.model.Obj
import tga.gaming.engine.model.Vector

class Food(
    p: Vector,
    r: Double = 10.0
) : Obj(p = p, r = r), CompositeDrawer {

    override val drawers = ArrayList<Drawer>(1)

    init {
        withCircleDrawer(radius = r, strokeStyle = "#EABE91FF", lineWidth = 5.0, fillStyle = "#FF6C30FF")
    }

}

