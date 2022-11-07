package tga.gaming.engine


data class Vector(
    val x: Double = 0.0,
    val y: Double = 0.0
)

fun v(x: Double, y: Double) = Vector(x, y)
fun v(x: Float,  y: Float)  = Vector(x.toDouble(), y.toDouble())
fun v(x: Long,   y: Long)   = Vector(x.toDouble(), y.toDouble())
fun v(x: Int,    y: Int)    = Vector(x.toDouble(), y.toDouble())
