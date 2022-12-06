package tga.gaming.engine.colors

data class clr(val r: Int, val g: Int, val b: Int, val o: Int = 255){
    fun toHexColorString(): String {
        return "#" +
                r.toString(16).padStart(2,'0') +
                g.toString(16).padStart(2,'0') +
                b.toString(16).padStart(2,'0') +
                o.toString(16).padStart(2,'0')
    }
}

fun linearGradient(c0: clr, c1: clr, n: Int): Array<String> {

    val r0 = c0.r.toDouble()
    val g0 = c0.g.toDouble()
    val b0 = c0.b.toDouble()
    val o0 = c0.o.toDouble()

    val r1 = c1.r.toDouble()
    val g1 = c1.g.toDouble()
    val b1 = c1.b.toDouble()
    val o1 = c1.o.toDouble()

    val rDelta = r1 - r0
    val gDelta = g1 - g0
    val bDelta = b1 - b0
    val oDelta = o1 - o0

    val rStep = rDelta / n
    val gStep = gDelta / n
    val bStep = bDelta / n
    val oStep = oDelta / n

    val colors = Array(n+1) {i ->
        when (i) {
            0 -> c0.toHexColorString()
            n -> c1.toHexColorString()
            else -> clr(
                (r0 + rStep * i).toInt(),
                (g0 + gStep * i).toInt(),
                (b0 + bStep * i).toInt(),
                (o0 + oStep * i).toInt(),
            ).toHexColorString()
        }

    }
    println("linearGradient = [${colors.joinToString()}]")
    return colors
}

fun multiLinearGradient(c0: clr, vararg points: Pair<Int, clr>): Array<String> {
    val colors = ArrayList<String>()
//    var first = true
    var prevEndColor = c0
    for( (n, endColor) in points ) {
        val startColor = prevEndColor
        prevEndColor = endColor

        val part = linearGradient(startColor, endColor, n)
//        val startIndex = if (first) 0 else 1
        for (i in 0 until (part.size-1)) colors.add(part[i])
//        first = false
    }

    val result = colors.toTypedArray()
    println("multiLinearGradient = [${result.joinToString()}]")

    return result
}
