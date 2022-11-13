package tga.gaming.engine.model

import tga.gaming.engine.dispatcher.GameObjects
import tga.gaming.engine.internal.IdSequence

open class Obj(
    open val p: Vector = Vector(),
    open var angle: Double = 0.0,
    open var scale: Double = 1.0,
    open val r: Double = 10.0,
    open val frame: Frame? = Frame( v(-r,-r), v(r,r)),
) {
    val id: Long = IdSequence.next()
    lateinit var dispatcher: GameObjects

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

