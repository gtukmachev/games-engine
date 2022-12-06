package tga.gaming.engine.model

import kotlin.math.*
import kotlin.random.Random.Default.nextDouble


data class Vector(
    var x: Double = 0.0,
    var y: Double = 0.0
) {
    private var cachedLen :Double? = null

    var len: Double
        get() {
            if (cachedLen == null) calculateLength()
            return cachedLen!!
        }
        set(value) {
            assignLength(value)
        }

    operator fun plusAssign(n: Int   ) { x+=n;   y+=n  ; cachedLen = null; }
    operator fun plusAssign(n: Double) { x+=n;   y+=n  ; cachedLen = null; }
    operator fun plusAssign(v: Vector) { x+=v.x; y+=v.y; cachedLen = null; }

    operator fun minusAssign(n: Int   ) { x-=n;   y-=n  ; cachedLen = null; }
    operator fun minusAssign(n: Double) { x-=n;   y-=n  ; cachedLen = null; }
    operator fun minusAssign(v: Vector) { x-=v.x; y-=v.y; cachedLen = null; }

    operator fun timesAssign(n: Int)    { x*=n;   y*=n  ; if (cachedLen != null) cachedLen = cachedLen!! * n; }
    operator fun timesAssign(n: Double) { x*=n;   y*=n  ; if (cachedLen != null) cachedLen = cachedLen!! * n; }

    operator fun plus(n: Int   ) = Vector(x+n,   y+n)
    operator fun plus(n: Double) = Vector(x+n,   y+n)
    operator fun plus(v: Vector) = Vector(x+v.x, y+v.y)

    operator fun minus(n: Int   ) = Vector(x-n,   y-n)
    operator fun minus(n: Double) = Vector(x-n,   y-n)
    operator fun minus(v: Vector) = Vector(x-v.x, y-v.y)

    operator fun times(n: Int   ) = Vector(x*n,   y*n)
    operator fun times(n: Double) = Vector(x*n,   y*n)
    operator fun times(v: Vector) = Vector(x*v.x, y*v.y)

    operator fun div(n: Int   ) = Vector(x/n, y/n)
    operator fun div(n: Double) = Vector(x/n, y/n)

    fun set(x: Double, y: Double) { this.x=  x; this.y =   y; cachedLen = null; }
    fun set(v: Vector           ) { this.x=v.x; this.y = v.y; cachedLen = null; }

    /**
     * restore angle by vector v, only for len(v) = 1
     */
    fun angle(): Double {

        return when {
            (x == 0.0) -> if (y > 0) angle_90 else angle_270
            (y == 0.0) -> if (x > 0) angle_0  else angle_180
            (y > 0) -> acos(x)
            else -> angle_180 + acos(-x)
        }
    }

    fun assignLength(desiredLength: Double): Vector {
        cachedLen = null
        when {
            (x == 0.0) -> y = if (y >= 0) desiredLength else -desiredLength
            (y == 0.0) -> x = if (x >= 0) desiredLength else -desiredLength
            else -> {
                val k = desiredLength / len
                x *= k
                y *= k
            }
        }
        cachedLen = desiredLength
        return this
    }

    private fun calculateLength() {
        cachedLen = when {
            (x == 0.0) -> y
            (y == 0.0) -> x
            else -> sqrt(x*x + y*y)
        }
    }

    fun norm(quite: Boolean = false): Vector {
        return this.copy().normalizeThis(quite)
    }

    fun normalizeThis(quite: Boolean = false): Vector {
        if (x == 0.0 && y == 0.0) {
            if (quite) return this else throw ZeroLengthVector("A zero vector cannot be normalized")
        }
        when {
            (x == 0.0) -> y = if (y > 0) 1.0 else -1.0
            (y == 0.0) -> x = if (x > 0) 1.0 else -1.0
            else -> {
                val l = len
                x /= l
                y /= l
            }
        }
        cachedLen = 1.0
        return this
    }

    fun setToAngle(angle: Double) {
        x = cos(angle)
        y = sin(angle)
    }

    companion object {
        const val angle_0  : Double =  0.0
        const val angle_90 : Double =  PI / 2
        const val angle_180: Double =  PI
        const val angle_270: Double =  PI/2 * 3
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
fun randomNormVector() = v(nextDouble(-1.0, 1.0), nextDouble(-1.0, 1.0)).normalizeThis()

val vUp        = v(0,-1)
val vUpRight   = v(1,-1).norm()
val vRight     = v(1,0)
val vDownRight = v(1,1).norm()
val vDown      = v(0,1)
val vDownLeft  = v(-1,1).norm()
val vLeft      = v(-1,0)
val vUpLeft    = v(-1,-1).norm()

data class Frame(
    val p0: Vector,
    val p1: Vector
) {
    val width: Double = p1.x - p0.x
    val height: Double = p1.y - p0.y
    //val center = (p1 - p0) / 2

    companion object {
        fun square(size: Double): Frame {
            val r = size/2
            return Frame( v(-r,-r), v(r,r) )
        }
    }
}

fun Vector.restrictWithFrame(bounds: Frame){
    if      (x > bounds.p1.x) x = bounds.p1.x
    else if (x < bounds.p0.x) x = bounds.p0.x
    if      (y > bounds.p1.y) y = bounds.p1.y
    else if (y < bounds.p0.y) y = bounds.p0.y
}


