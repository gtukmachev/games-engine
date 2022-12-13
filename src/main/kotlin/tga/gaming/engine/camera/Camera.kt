package tga.gaming.engine.camera

import tga.gaming.engine.model.Frame
import tga.gaming.engine.model.Obj
import tga.gaming.engine.model.Vector
import tga.gaming.engine.model.v


class Camera(
    val visibleWordFrame: Frame,
    val screenFrame: Frame,
    val wordSize: Vector,
    percentageOfActiveArea: Double = 0.6
) {

    var xScale = screenFrame.width / visibleWordFrame.width
    var yScale = screenFrame.height / visibleWordFrame.height

    val initialVisibleWordFrame = Frame(visibleWordFrame.p0.copy(), visibleWordFrame.p1.copy())
    val      initialScreenFrame = Frame(     screenFrame.p0.copy(),      screenFrame.p1.copy())

    val activeWordZone: Frame
    init {
        val xOffset = (visibleWordFrame.width  - (visibleWordFrame.width  * percentageOfActiveArea)) / 2
        val yOffset = (visibleWordFrame.height - (visibleWordFrame.height * percentageOfActiveArea)) / 2
        val vOffset = v(xOffset, yOffset)
        activeWordZone = Frame(visibleWordFrame.p0 + vOffset, visibleWordFrame.p1 - vOffset)
    }

    fun isInVisibleArea(obj: Obj): Boolean = visibleWordFrame.hasIntersection(obj.frame)

    fun arrangePositionTo(obj: Obj) {
        val p = obj.p

        if (p.x < activeWordZone.p0.x && visibleWordFrame.p0.x > 0) {
            var dx = p.x - activeWordZone.p0.x
            if ( (visibleWordFrame.p0.x+dx) < 0 ) dx = -visibleWordFrame.p0.x 
            moveHorizontally(dx)
        } else if (p.x > activeWordZone.p1.x && activeWordZone.p1.x < wordSize.x) {
            var dx = p.x - activeWordZone.p1.x
            if ( (visibleWordFrame.p1.x+dx) > wordSize.x ) dx = wordSize.x - visibleWordFrame.p1.x 
            moveHorizontally(dx)
        }

        if (p.y < activeWordZone.p0.y && visibleWordFrame.p0.y > 0) {
            var dy = p.y - activeWordZone.p0.y
            if ( (visibleWordFrame.p0.y+dy) < 0 ) dy = -visibleWordFrame.p0.y
            moveVertically(dy)
        } else if (p.y > activeWordZone.p1.y && activeWordZone.p1.y < wordSize.y) {
            var dy = p.y - activeWordZone.p1.y
            if ( (visibleWordFrame.p1.y+dy) > wordSize.y ) dy = wordSize.y - visibleWordFrame.p1.y
            moveVertically(dy)
        }


    }

    private fun moveHorizontally(dx: Double) {
        visibleWordFrame.p0.x += dx
        visibleWordFrame.p1.x += dx
        activeWordZone.p0.x += dx
        activeWordZone.p1.x += dx
    }

    private fun moveVertically(dy: Double) {
        visibleWordFrame.p0.y += dy
        visibleWordFrame.p1.y += dy
        activeWordZone.p0.y += dy
        activeWordZone.p1.y += dy
    }

    override fun toString(): String {
        return "Camera(visibleWordFrame=$visibleWordFrame, screenFrame=$screenFrame, wordSize=$wordSize, xScale=$xScale, yScale=$yScale, activeWordZone=$activeWordZone)"
    }

    fun changeScaleTo(newScale: Double) {

        val centerPoint = (visibleWordFrame.p1 - visibleWordFrame.p0) / 2

        val halfOfDesiredWidth  = (screenFrame.width  / newScale) / 2
        val halfOfDesiredHeight = (screenFrame.height / newScale) / 2
        val diagonalVector = v(halfOfDesiredWidth, halfOfDesiredHeight)

        visibleWordFrame.p0.set( centerPoint - diagonalVector )
        visibleWordFrame.p1.set( centerPoint + diagonalVector )

        xScale = newScale
        yScale = newScale

    }

    fun resetScale() {


    }


}