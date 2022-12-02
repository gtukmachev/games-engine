package tga.gaming.engine.shapes

import org.w3c.dom.TouchEvent
import org.w3c.dom.events.MouseEvent
import tga.gaming.engine.GameWord
import tga.gaming.engine.dispatcher.SimpleEventsListener
import tga.gaming.engine.drawers.withCircleDrawer
import tga.gaming.engine.model.*

fun GameWord.withPointer(indicate: Boolean = false): Pointer = dispatcher.addObj( Pointer(indicate) )

class Pointer(indicate: Boolean = false) : Obj(r = 0.0), Moveable, SimpleEventsListener, CompositeDrawer {

    override val drawers = mutableListOf<Drawer>()

    var externalPointerCoordinates : Vector? = null
        private set
    private var externalPointerWasMoved = false

    init {
        if (indicate) withCircleDrawer(radius = 10)
    }

    override fun move() {
        if (externalPointerWasMoved) {
            externalPointerCoordinates?.let{
                p.set(it)
            }
            externalPointerWasMoved = false
        }
    }

    override fun onTouchMove  (te: TouchEvent) { te.touches.item(0)?.let{ movementProcess(it.clientX.toDouble(), it.clientY.toDouble()) }}
    override fun onTouchEnd   (te: TouchEvent) { movementFinish() }
    override fun onTouchStart (te: TouchEvent) { te.touches.item(0)?.let{ movementStart(it.clientX.toDouble(), it.clientY.toDouble()) }}
    override fun onMouseMove  (me: MouseEvent) { movementProcess(me.x, me.y) }
    override fun onMouseEnter (me: MouseEvent) { movementStart(me.x, me.y)  }
    override fun onMouseLeave (me: MouseEvent) { movementFinish()  }

    private fun movementProcess(x: Double, y: Double) {
        if (externalPointerCoordinates == null) externalPointerCoordinates = v()
        externalPointerCoordinates!!.set(x, y)
        externalPointerWasMoved = true
    }

    fun movementStart(x: Double, y: Double) {
        externalPointerCoordinates = v(x, y)
        externalPointerWasMoved = true
    }

    fun movementFinish() {
        if (externalPointerCoordinates != null) {
            externalPointerCoordinates = null
            externalPointerWasMoved = true
        }
    }

}