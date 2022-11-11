package tga.gaming.engine.index

import tga.gaming.engine.model.Frame
import tga.gaming.engine.model.Obj
import tga.gaming.engine.model.Vector

const val sizeFactor: Int = 6 // square size == 64

class ObjectsSquareIndex(
    wordSize: Vector
) : SquareIndex {

    override val lines  : Int = wordSize.y.toInt() shr sizeFactor
    override val columns: Int = wordSize.x.toInt() shr sizeFactor
    override val matrix = Array<Array<MutableSet<Obj>>>(lines){ Array(columns) { HashSet() } }

    private val maxLinesIndex = lines - 1
    private val maxColumnsIndex = columns - 1

    private val positionsRangeByObj: MutableMap<Obj, PositionsRange2D> = HashMap()

    override fun update(obj: Obj) {
        val prev: PositionsRange2D? = positionsRangeByObj[obj]
        val curr: PositionsRange2D? = rangeOfObject(obj.p, obj.frame)

        if ( prev == curr ) return
        if ( curr == null) { remove(obj); return }

        prev?.forEachExcept(curr) {
            it.remove(obj)
        }

        curr.forEachExcept(prev) {
            it.add(obj)
        }

        positionsRangeByObj[obj] = curr

    }

    override fun remove(obj: Obj) {
        val prev: PositionsRange2D? = positionsRangeByObj[obj]
        prev?.forEach { it.remove(obj) }
    }

    override fun objectsOnTheSamePlaceWith(obj: Obj): Sequence<Obj> {
        val positionsRange = positionsRangeByObj[obj] ?: return emptySequence()

        val objects = HashSet<Obj>()
        positionsRange.forEach {
            objects.addAll(it)
        }

        return objects.asSequence().filter { it !== obj }
    }

    private fun PositionsRange2D.forEachExcept(except: PositionsRange2D?, body: (MutableSet<Obj>) -> Unit) {
        if (except == null) {
            this.forEach(body)
            return
        }

        for (l in lin0 .. lin1) {
            val lin = matrix[l]
            for (c in col0..col1) {
                if ( !(l in except.lin0..except.lin1 && c in except.col0..except.col1) ) {
                    body(lin[c])
                }
            }
        }

    }

    private fun PositionsRange2D.forEach(body: (MutableSet<Obj>) -> Unit) {
        for (l in lin0 .. lin1) {
            val line = matrix[l]
            for (c in col0..col1)
                body(line[c])
        }

    }

    private fun rangeOfObject(position: Vector, frame: Frame?): PositionsRange2D? {
        if (position == null || frame == null) return null

        var l0 = (position.y + frame.p0.y).toInt() shr sizeFactor
        var c0 = (position.x + frame.p0.x).toInt() shr sizeFactor
        var l1 = (position.y + frame.p1.y).toInt() shr sizeFactor
        var c1 = (position.x + frame.p1.x).toInt() shr sizeFactor

        if ((l0<0 && l1<0) || (l0>=lines   && l1>=lines  )) return null
        if ((c0<0 && c1<0) || (c0>=columns && c1>=columns)) return null

        if (l0<0) l0 = 0; if (l0>=lines) l0 = maxLinesIndex
        if (l1<0) l1 = 0; if (l1>=lines) l1 = maxLinesIndex
        if (c0<0) c0 = 0; if (c0>=lines) c0 = maxColumnsIndex
        if (c1<0) c1 = 0; if (c1>=lines) c1 = maxColumnsIndex

        return PositionsRange2D(lin0 = l0, col0 = c0, lin1 = l1, col1 = c1 )
    }

}

data class PositionsRange2D(
    val lin0: Int,
    val col0: Int,
    val lin1: Int,
    val col1: Int
)
