package tga.gaming.engine.shapes

import org.w3c.dom.pointerevents.PointerEvent
import tga.gaming.engine.GameWord
import tga.gaming.engine.dispatcher.SimpleEventsListener
import tga.gaming.engine.model.*

fun GameWord.withPointer(): Pointer = dispatcher.addObj( Pointer() )

class Pointer : Obj(r = 0.0), Moveable, SimpleEventsListener, CompositeDrawer {

    override val drawers = mutableListOf<Drawer>()

    var externalPointerCoordinates : Vector? = null
        private set
    private var externalPointerWasMoved = false

    override fun move() {
        if (externalPointerWasMoved) {
            externalPointerCoordinates?.let{
                p.set(it)
            }
            externalPointerWasMoved = false
        }
    }

    override fun onPointerMove(pe: PointerEvent) {
        if (externalPointerCoordinates == null) externalPointerCoordinates = v()
        externalPointerCoordinates!!.set(pe.x, pe.y)
        externalPointerWasMoved = true
    }

    override fun onPointerOver(pe: PointerEvent) {
        externalPointerCoordinates = v(pe.x, pe.y)
        externalPointerWasMoved = true
    }

    override fun onPointerOut(pe: PointerEvent) {
        externalPointerCoordinates = null
        externalPointerWasMoved = true
    }

}