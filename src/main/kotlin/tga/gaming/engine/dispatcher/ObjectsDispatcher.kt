package tga.gaming.engine.dispatcher

import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.pointerevents.PointerEvent
import tga.gaming.engine.index.SquareIndex
import tga.gaming.engine.model.Actionable
import tga.gaming.engine.model.Moveable
import tga.gaming.engine.model.Obj

interface Dispatcher : GameTurner, GameObjects, EventsListener

open class ObjectsDispatcher(
    override val index: SquareIndex
) : Dispatcher {
    override val objects: MutableSet<Obj> = HashSet()

    override fun turn() {
        addNewObjects()
        doMove()
        update2dIndex()
        doAct()
        removeDeletedObjects()

        movedObjects.clear()
        objectToAdd.clear()
        objectToDel.clear()
    }

    private val movedObjects = ArrayList<Obj>()
    private fun doMove() {
        objects.forEach {
            if (it is Moveable) {
                val oldPosition = it.p.copy()
                it.move()
                if (it.p != oldPosition) movedObjects += it
            }
        }

    }

    private fun update2dIndex() {
        if (movedObjects.isNotEmpty()) index.update(movedObjects)
    }

    private fun doAct() {
        objects.forEach {
            if (it is Actionable) it.act()
        }
    }

    private fun removeDeletedObjects() {
        if (objectToDel.isNotEmpty()) {
            objects.removeAll(objectToDel)
            index.remove(objectToDel)
        }
    }

    private fun addNewObjects() {
        if (objectToAdd.isNotEmpty()) {
            objects.addAll(objectToAdd)
            objectToAdd.forEach { it.dispatcher = this }
            index.update(objectToAdd)
        }
    }

    private val objectToAdd = ArrayList<Obj>()
    override fun addObj(obj: Obj) {
        objectToAdd.add(obj)
    }

    private val objectToDel = HashSet<Obj>()
    override fun delObj(obj: Obj) {
        objectToDel.add(obj)
    }

    override fun finishGame() {
        objects.clear()
        index.reset()
    }

    override fun onMouseMove (me: MouseEvent){ objects.forEach { if (it is EventsListener) it.onMouseMove (me) } }
    override fun onMouseDown (me: MouseEvent){ objects.forEach { if (it is EventsListener) it.onMouseDown (me) } }
    override fun onMouseUp   (me: MouseEvent){ objects.forEach { if (it is EventsListener) it.onMouseUp   (me) } }
    override fun onMouseEnter(me: MouseEvent){ objects.forEach { if (it is EventsListener) it.onMouseEnter(me) } }
    override fun onMouseLeave(me: MouseEvent){ objects.forEach { if (it is EventsListener) it.onMouseLeave(me) } }

    override fun onGotPointerCapture (pe: PointerEvent){ objects.forEach { if (it is EventsListener) it.onGotPointerCapture(pe) } }
    override fun onLostPointerCapture(pe: PointerEvent){ objects.forEach { if (it is EventsListener) it.onLostPointerCapture(pe) } }
    override fun onPointerDown       (pe: PointerEvent){ objects.forEach { if (it is EventsListener) it.onPointerDown(pe) } }
    override fun onPointerMove       (pe: PointerEvent){ objects.forEach { if (it is EventsListener) it.onPointerMove(pe) } }
    override fun onPointerUp         (pe: PointerEvent){ objects.forEach { if (it is EventsListener) it.onPointerUp(pe) } }
    override fun onPointerCancel     (pe: PointerEvent){ objects.forEach { if (it is EventsListener) it.onPointerCancel(pe) } }
    override fun onPointerOver       (pe: PointerEvent){ objects.forEach { if (it is EventsListener) it.onPointerOver(pe) } }
    override fun onPointerOut        (pe: PointerEvent){ objects.forEach { if (it is EventsListener) it.onPointerOut(pe) } }
    override fun onPointerEnter      (pe: PointerEvent){ objects.forEach { if (it is EventsListener) it.onPointerEnter(pe) } }
    override fun onPointerLeave      (pe: PointerEvent){ objects.forEach { if (it is EventsListener) it.onPointerLeave(pe) } }

    override fun onClick   (me: MouseEvent){ objects.forEach { if (it is EventsListener) it.onClick   (me) } }
    override fun onDblClick(me: MouseEvent){ objects.forEach { if (it is EventsListener) it.onDblClick(me) } }

    override fun onKeyPress(ke: KeyboardEvent) { objects.forEach { if (it is EventsListener) it.onKeyPress(ke) } }
    override fun onKeyDown (ke: KeyboardEvent) { objects.forEach { if (it is EventsListener) it.onKeyDown (ke) } }
    override fun onKeyUp   (ke: KeyboardEvent) { objects.forEach { if (it is EventsListener) it.onKeyUp   (ke) } }



}


