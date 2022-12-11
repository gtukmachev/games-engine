package tga.gaming.games.wiggly_worm

import tga.gaming.engine.model.Vector
import kotlin.random.Random

class WormWithMemoryBodyMover(
    p: Vector,
    initialRadius: Double,
    fillStyles: Array<String>,
    strokeStyles: Array<String>,
    electricCharge: Boolean = Random.nextBoolean()
): Worm(p, initialRadius, fillStyles, strokeStyles, electricCharge){

    private var headPoint: Tip<Vector> = Tip(p.copy())
    private val bodyTip: MutableList<Tip<Vector>> = ArrayList<Tip<Vector>>(desiredBodyLength).apply {
        repeat(desiredBodyLength){ add(headPoint) }
    }

    override fun increaseWormBodyLength() {
        super.increaseWormBodyLength()
        bodyTip.add(Tip(body[body.size-2], bodyTip[bodyTip.size-1] ))
    }

    override fun moveWormBody() {
        moveWithMemoryOfEachPoint()
    }

    private fun moveWithMemoryOfEachPoint() {
        // first circle
        body[0] = p
        headPoint.next = Tip(p.copy())
        headPoint = headPoint.next!!

        // next circles
        for (i in 1 until body.size) {

            while ( (body[i-1] - body[i]).len > r ) {
                bodyTip[i] = bodyTip[i].next!!
                body[i] = bodyTip[i].v
            }

        }
    }

}