package tga.gaming.games.wiggly_worm.worms

import tga.gaming.engine.model.Vector
import tga.gaming.engine.model.v
import tga.gaming.games.wiggly_worm.WigglyWormGame

class WormWithFollowBodyMover(
    p: Vector,
    initialRadius: Double,
    fillStyles: Array<String>,
    strokeStyles: Array<String>,
    game: WigglyWormGame
): Worm(p, initialRadius, fillStyles, strokeStyles, game = game) {

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
        moveWormWithFollowBodyMover()
    }

    private fun moveWormWithFollowBodyMover() {
        body[0].p.set(p)
        var prev = p
        for (i in 1 until body.size) {
            val curr = body[i].p

            val offset = prev + (curr-prev).assignLength(r)
            curr.set(offset)

            prev=curr
       }
    }

}