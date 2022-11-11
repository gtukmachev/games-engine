package tga.gaming.engine.model

import kotlin.math.sqrt


data class Vector(
    var x: Double = 0.0,
    var y: Double = 0.0
) {
    private var len_ :Double? = null

    var len: Double
        get() {
            if (len_ == null) calculateLength()
            return len_!!
        }
        set(value) {
            assignLength(value)
        }

    operator fun plusAssign(n: Int   ) { x+=n;   y+=n  ; len_ = null; }
    operator fun plusAssign(n: Double) { x+=n;   y+=n  ; len_ = null; }
    operator fun plusAssign(v: Vector) { x+=v.x; y+=v.y; len_ = null; }

    operator fun minusAssign(n: Int   ) { x-=n;   y-=n  ; len_ = null; }
    operator fun minusAssign(n: Double) { x-=n;   y-=n  ; len_ = null; }
    operator fun minusAssign(v: Vector) { x-=v.x; y-=v.y; len_ = null; }

    operator fun timesAssign(n: Int)    { x*=n;   y*=n  ; if (len_ != null) len_ = len_!! * n; }
    operator fun timesAssign(n: Double) { x*=n;   y*=n  ; if (len_ != null) len_ = len_!! * n; }

    operator fun plus(n: Int   ) = Vector(x+n,   y+n)
    operator fun plus(n: Double) = Vector(x+n,   y+n)
    operator fun plus(v: Vector) = Vector(x+v.x, y+v.y)

    operator fun minus(n: Int   ) = Vector(x-n,   y-n)
    operator fun minus(n: Double) = Vector(x-n,   y-n)
    operator fun minus(v: Vector) = Vector(x-v.x, y-v.y)

    operator fun div(n: Int   ) = Vector(x/n, y/n)
    operator fun div(n: Double) = Vector(x/n, y/n)

    fun set(x: Double, y: Double) { this.x=  x; this.y =   y; len_ = null; }
    fun set(v: Vector           ) { this.x=v.x; this.y = v.y; len_ = null; }

    private fun assignLength(desiredLength: Double): Vector {
        when {
            (x == 0.0) -> y = if (y > 0) desiredLength else -desiredLength
            (y == 0.0) -> x = if (x > 0) desiredLength else -desiredLength
            else -> {
                val k = desiredLength / len
                x *= k
                y *= k
            }
        }
        len_ = desiredLength
        return this
    }

    private fun calculateLength() {
        len_ = when {
            (x == 0.0) -> y
            (y == 0.0) -> x
            else -> sqrt(x*x + y*y)
        }
    }

    fun norm(quite: Boolean = false): Vector {
        if (x == 0.0 && y == 0.0) {
            if (quite) return this else throw RuntimeException("A zero vector cannot be normalized")
        }
        normalize()
        return this
    }

    private fun normalize() {
        // here the |vector| should be > 0 !!!
        when {
            (x == 0.0) -> y = if (y > 0) 1.0 else -1.0
            (y == 0.0) -> x = if (x > 0) 1.0 else -1.0
            else -> {
                val l = len
                x /= l
                y /= l
            }
        }
        len_ = 1.0
    }

}

fun v()                     = Vector()
fun v(x: Double, y: Double) = Vector(x, y)
fun v(x: Float,  y: Float)  = Vector(x.toDouble(), y.toDouble())
fun v(x: Long,   y: Long)   = Vector(x.toDouble(), y.toDouble())
fun v(x: Int,    y: Int)    = Vector(x.toDouble(), y.toDouble())
fun v(x: Int,    y: Double) = Vector(x.toDouble(), y)
fun v(x: Int,    y: Float)  = Vector(x.toDouble(), y.toDouble())
fun v(x: Int,    y: Long)   = Vector(x.toDouble(), y.toDouble())
fun v(x: Double, y: Int)    = Vector(x,            y.toDouble())
fun v(x: Float,  y: Int)    = Vector(x.toDouble(), y.toDouble())
fun v(x: Long,   y: Int)    = Vector(x.toDouble(), y.toDouble())

data class Frame(
    val p0: Vector,
    val p1: Vector
) {
    val width: Double = p1.x - p0.x
    val height: Double = p1.y - p0.y
    val center = (p1 - p0) / 2

    companion object
}
