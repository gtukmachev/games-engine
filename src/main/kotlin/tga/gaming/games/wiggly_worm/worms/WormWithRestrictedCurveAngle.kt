package tga.gaming.games.wiggly_worm.worms

import tga.gaming.engine.PI2
import tga.gaming.engine.model.Vector
import tga.gaming.engine.model.normVectorOfAngle
import tga.gaming.engine.model.v
import tga.gaming.games.wiggly_worm.WigglyWormGame
import kotlin.random.Random

class WormWithRestrictedCurveAngle(
    p: Vector,
    initialRadius: Double,
    fillStyles: Array<String>,
    strokeStyles: Array<String>,
    private val maxCurveAngle: Double,
    electricCharge: Boolean = Random.nextBoolean(),
    game: WigglyWormGame
): Worm(p, initialRadius, fillStyles, strokeStyles, electricCharge, game=game) {

    init {
        setBodyInHorizontalLine()
    }

    private fun setBodyInHorizontalLine() {
        var center = p.copy()
        val offset = v(-r, 0.0)

        for (i in 0 until body.size) {
            body[i].p.set(center)
            center = center + offset
        }

    }

    override fun moveWormBody() {
        moveWormBodyWithRestrictedCurveAngle()
    }

    private fun moveWormBodyWithRestrictedCurveAngle() {

        // head
        body[0].p.set(p)

        // second circle
        val d = body[1].p - body[0].p
        if (d.len > r) body[1].p.set(body[0].p + d.assignLength(r))

        // other circles
        for (i in 2 until body.size) {
            val toPrev = body[i - 2].p - body[i - 1].p
            var toNext = body[i].p - body[i - 1].p
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

            body[i].p.set( body[i - 1].p + toNext)
        }


    }

}