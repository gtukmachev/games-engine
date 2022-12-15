package tga.gaming.games.wiggly_worm

import tga.gaming.engine.model.Obj
import tga.gaming.engine.model.Vector

class Body(
    p: Vector,
    val worm: Worm
): Obj(p = p) {

    override var r: Double
        get() = worm.r
        set(value) { worm.r = value }


    override val r2: Double
        get() = worm.r2
}