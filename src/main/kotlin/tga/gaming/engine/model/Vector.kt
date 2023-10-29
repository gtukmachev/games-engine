package tga.gaming.engine.model

import tga.gaming.engine.PI2
import kotlin.math.*
import kotlin.random.Random.Default.nextDouble


data class Vector(
    var x: Double = 0.0,
    var y: Double = 0.0
) {
    private var cachedLen  :Double? = null // length
    private var cachedLen2 :Double? = null // length * length
    var isNorm: Boolean = false // == true after calling the norm() or normThis() methods

    var len: Double
        get() {
            if (cachedLen == null) calculateLength()
            return cachedLen!!
        }
        set(value) {
            assignLength(value)
        }

    val len2: Double
        get() {
            if (cachedLen2 == null) calculateLength2()
            return cachedLen2!!
        }

    operator fun plusAssign(n: Int   ) { x+=n;   y+=n  ; cachedLen=null; cachedLen2=null; isNorm=false }
    operator fun plusAssign(n: Double) { x+=n;   y+=n  ; cachedLen=null; cachedLen2=null; isNorm=false }
    operator fun plusAssign(v: Vector) { x+=v.x; y+=v.y; cachedLen=null; cachedLen2=null; isNorm=false }

    operator fun minusAssign(n: Int   ) { x-=n;   y-=n  ; cachedLen=null; cachedLen2 = null; isNorm=false }
    operator fun minusAssign(n: Double) { x-=n;   y-=n  ; cachedLen=null; cachedLen2 = null; isNorm=false }
    operator fun minusAssign(v: Vector) { x-=v.x; y-=v.y; cachedLen=null; cachedLen2 = null; isNorm=false }

    operator fun timesAssign(n: Int)    { x*=n;   y*=n  ; cachedLen=null; cachedLen2 = null; isNorm=false }
    operator fun timesAssign(n: Double) { x*=n;   y*=n  ; cachedLen=null; cachedLen2 = null; isNorm=false }

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

    fun set(x: Double, y: Double) { this.x=  x; this.y =   y; cachedLen = null;        cachedLen2 = null;         isNorm=false    }
    fun set(v: Vector           ) { this.x=v.x; this.y = v.y; cachedLen = v.cachedLen; cachedLen2 = v.cachedLen2; isNorm=v.isNorm }

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
        when {
            (x == 0.0) -> y = (if (y >= 0) desiredLength else -desiredLength)
            (y == 0.0) -> x = (if (x >= 0) desiredLength else -desiredLength)
            else -> {
                val k = desiredLength / len
                x *= k
                y *= k
            }
        }
        cachedLen = desiredLength
        cachedLen2 = null
        return this
    }


    private fun calculateLength() {
        cachedLen = when {
            (x == 0.0) -> y
            (y == 0.0) -> x
            else -> sqrt( len2 )
        }
    }

    private fun calculateLength2() {
        cachedLen2 = y*y + x*x
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
        isNorm = true
        cachedLen = 1.0
        cachedLen2 = 1.0
        return this
    }

    fun setToAngle(angle: Double) {
        x = cos(angle)
        y = sin(angle)
        isNorm = true
    }

    companion object {
        const val angle_0  : Double =  0.0
        const val angle_90 : Double =  PI / 2
        const val angle_180: Double =  PI
        const val angle_270: Double =  PI/2 * 3

        fun random1() = randomNormVector()
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
fun normVectorOfAngle(angle: Double):Vector = v(cos(angle), sin(angle)).apply { isNorm = true }
fun randomNormVector() = normVectorOfAngle(nextDouble(0.0, PI2))

val vUp        = v(0,-1).apply { isNorm = true }
val vUpRight   = v(1,-1).normalizeThis()
val vRight     = v(1,0).apply { isNorm = true }
val vDownRight = v(1,1).normalizeThis()
val vDown      = v(0,1).apply { isNorm = true }
val vDownLeft  = v(-1,1).normalizeThis()
val vLeft      = v(-1,0).apply { isNorm = true }
val vUpLeft    = v(-1,-1).normalizeThis()

data class Frame(
    val p0: Vector,
    val p1: Vector
) {
    val width: Double get() = p1.x - p0.x
    val height: Double get() = p1.y - p0.y
    //val center = (p1 - p0) / 2

    fun hasIntersection(another: Frame): Boolean {
        if (another.p0.x > p1.x || another.p0.y > p1.y) return false
        if (another.p1.x < p0.x || another.p1.y < p0.y) return false
        return true
    }

    fun hasIntersection(p: Vector, radius: Double): Boolean {
        if ( (p.x-radius) > p1.x || ((p.y-radius) > p1.y) ) return false
        if ( (p.x+radius) < p0.x || ((p.y+radius) < p0.y) ) return false
        return true
    }

    operator fun plus(vector: Vector): Frame {
        return Frame( p0+vector, p1+ vector )
    }

    operator fun times(n: Double): Frame {
        return Frame( p0*n, p1*n )
    }

    operator fun times(n: Int): Frame {
        return Frame( p0*n, p1*n )
    }

    fun set(anotherFrame: Frame) {
        p0.set(anotherFrame.p0)
        p1.set(anotherFrame.p1)
    }

    fun set(anotherP0: Vector, anotherP1: Vector) {
        p0.set(anotherP0)
        p1.set(anotherP1)
    }

    fun shrink(percentage: Double): Frame {
        val xOffset = (width  - (width  * percentage)) / 2
        val yOffset = (height - (height * percentage)) / 2
        val vOffset = v(xOffset, yOffset)
        return Frame(p0 + vOffset, p1 - vOffset)
    }

    fun copy(): Frame {
        return Frame(p0.copy(), p1.copy())
    }

    fun center(): Vector = p0 + (p1-p0)/2

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

val ZERO_VECTOR = v(0,0)
