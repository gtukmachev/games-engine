package tga.gaming.games.wiggly_worm

import tga.gaming.engine.PI2
import tga.gaming.engine.model.Vector
import tga.gaming.engine.model.normVectorOfAngle
import tga.gaming.engine.model.v
import kotlin.random.Random

class WormWithRestrictedCurveAngle(
    p: Vector,
    initialRadius: Double,
    fillStyles: Array<String>,
    strokeStyles: Array<String>,
    private val maxCurveAngle: Double,
    electricCharge: Boolean = Random.nextBoolean()
): Worm(p, initialRadius, fillStyles, strokeStyles, electricCharge) {

    init {
        setBodyInHorizontalLine()
    }

    private fun setBodyInHorizontalLine() {
        var center = p.copy()
        val offset = v(-r, 0.0)

        for (i in 0 until body.size) {
            body[i].set(center)
            center = center + offset
        }

    }

    override fun moveWormBody() {
        moveWormBodyWithRestrictedCurveAngle()
    }

    private fun moveWormBodyWithRestrictedCurveAngle() {

        // head
        body[0] = p

        // second circle
        val d = body[1] - body[0]
        if (d.len > r) body[1] = body[0] + d.assignLength(r)

        // other circles
        for (i in 2 until body.size) {
            val toPrev = body[i - 2] - body[i - 1]
            var toNext = body[i] - body[i - 1]
            val toNextNorm = toNext.norm()

            val aToPrev = toPrev.norm().angle()
            var aToNext = toNextNorm.angle()

            if (aToNext < aToPrev) aToNext += PI2

            val correctedAngle: Double? = when {
                aToNext < (aToPrev + maxCurveAngle) -> aToPrev + maxCurveAngle
                aToNext > (aToPrev + PI2 - maxCurveAngle) -> aToPrev + PI2 - maxCurveAngle
                else -> null
            }

            if (correctedAngle == null) {
                if (toNext.len > r) toNext = toNextNorm * r
            } else {
                toNext = normVectorOfAngle(correctedAngle).apply {
                    x *= r
                    y *= r
                }
            }

            body[i] = body[i - 1] + toNext
        }


    }

}