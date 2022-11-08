package tga.gaming.engine.index

import tga.gaming.engine.Frame
import tga.gaming.engine.Obj
import tga.gaming.engine.Vector

const val sizeFactor: Int = 5 // square size == 32

class ObjectsSquareIndex(
    private val wordSize: Vector
) : SquareIndex {

    private val lines  : Int = wordSize.y.toInt() shr sizeFactor
    private val columns: Int = wordSize.x.toInt() shr sizeFactor
    private val maxL: Int = lines - 1
    private val maxC: Int = columns - 1

    private val matrix = Array<Array<MutableSet<Obj>>>(lines){ Array(columns) { HashSet() } }

    private val positionsRangeByObj: MutableMap<Obj, PositionsRange2D> = HashMap()

    override fun update(obj: Obj) {
        val prev: PositionsRange2D? = positionsRangeByObj[obj]
        val curr: PositionsRange2D? = PositionsRange2D.from(obj.frame)

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

    override fun nearest(obj: Obj): Sequence<Obj> {
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
            for (c in col0..col1) body(line[c])
        }

    }

}

data class PositionsRange2D(
    val lin0: Int,
    val col0: Int,
    val lin1: Int,
    val col1: Int
) {

    companion object {
        fun from(frame: Frame?): PositionsRange2D? {
            if (frame == null) return null
            return PositionsRange2D(
                lin0 = frame.p0.x.toInt() shr sizeFactor,
                col0 = frame.p0.y.toInt() shr sizeFactor,
                lin1 = frame.p1.x.toInt() shr sizeFactor,
                col1 = frame.p1.y.toInt() shr sizeFactor,
            )
        }
    }

}

