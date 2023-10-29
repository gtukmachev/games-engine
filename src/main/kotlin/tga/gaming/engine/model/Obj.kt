package tga.gaming.engine.model

import tga.gaming.engine.dispatcher.GameObjects
import tga.gaming.engine.internal.IdSequence
import kotlin.math.cos
import kotlin.math.sin

open class Obj(
    open val p: Vector = Vector(),
    open var scale: Double = 1.0,
    r: Double = 10.0,
    open val frame: Frame? = Frame( v(-r,-r), v(r,r)),
) {
    val id: Long = IdSequence.next()
    open val isAlwaysVisible = false

    open var r: Double = r
        set(value) {
            field = value
            r2Cache = null
        }


    private var _angle: Double? = null
    private var _direction: Vector = v(1, 0)

    var angle: Double
        get() {
            if (_angle == null) _angle = _direction.angle()
            return _angle!!
        }
        set(a) {
            _angle = a
            _direction.set(cos(a), sin(a))
        }


    val direction: Vector get() = _direction
    fun setDirectionToTarget(target: Vector) {
        if (target == p) {
            _direction.set(1.0, 0.0)
            _angle = 0.0
        } else {
            _direction = (target-p).normalizeThis()
            _angle = null
        }
    }


    protected var r2Cache: Double? = null
    open val r2: Double
        get() {
            return r2Cache ?: ( (r * r).also{r2Cache = it} )
        }


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

