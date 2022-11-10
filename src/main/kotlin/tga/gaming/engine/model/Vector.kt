package tga.gaming.engine.model


data class Vector(
    val x: Double = 0.0,
    val y: Double = 0.0
) {

    operator fun plus(n: Int): Vector {
        return Vector(x+n, y+n)
    }

    operator fun minus(n: Int): Vector {
        return Vector(x-n, y-n)
    }

}

fun v(x: Double, y: Double) = Vector(x, y)
fun v(x: Float,  y: Float)  = Vector(x.toDouble(), y.toDouble())
fun v(x: Long,   y: Long)   = Vector(x.toDouble(), y.toDouble())
fun v(x: Int,    y: Int)    = Vector(x.toDouble(), y.toDouble())

data class Frame(
    val p0: Vector,
    val p1: Vector
)
