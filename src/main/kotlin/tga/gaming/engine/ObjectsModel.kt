package tga.gaming.engine

import tga.gaming.engine.internal.IdSequence

open class Obj(
    val p: Vector = Vector()
) {
    val id: Long = IdSequence.next()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Obj) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

interface Actionable {
    fun onPrepareAct() { }
    fun onDoAct() { }
    fun onFinishAct() { }
}
