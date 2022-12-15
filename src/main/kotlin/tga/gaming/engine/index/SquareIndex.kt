package tga.gaming.engine.index

import tga.gaming.engine.model.Frame
import tga.gaming.engine.model.Obj
import tga.gaming.engine.model.Vector

interface SquareIndex {

    val lines  : Int
    val columns: Int
    val maxLinesIndex  : Int
    val maxColumnsIndex: Int
    val matrix: Array<Array<MutableSet<Obj>>>

    fun update(objects: Collection<Obj>) {
        objects.forEach { update(it) }
    }

    fun update(obj: Obj)

    fun remove(objects: Collection<Obj>) {
        objects.forEach { remove(it) }
    }

    fun remove(obj: Obj)

    fun objectsOnTheSamePlaceWith(obj: Obj): Sequence<Obj>

    fun reset()

    fun rangeOf(position: Vector, frame: Frame?): PositionsRange2D?
    fun rangeOf(x0: Double, y0: Double, x1: Double, y1: Double): PositionsRange2D?

}
