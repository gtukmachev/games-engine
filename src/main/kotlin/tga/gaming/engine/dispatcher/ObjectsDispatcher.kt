package tga.gaming.engine.dispatcher

import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
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

    override fun onMouseMove (mouseEvent: MouseEvent){ objects.forEach { if (it is EventsListener) it.onMouseMove (mouseEvent) } }
    override fun onMouseDown (mouseEvent: MouseEvent){ objects.forEach { if (it is EventsListener) it.onMouseDown (mouseEvent) } }
    override fun onMouseUp   (mouseEvent: MouseEvent){ objects.forEach { if (it is EventsListener) it.onMouseUp   (mouseEvent) } }
    override fun onMouseEnter(mouseEvent: MouseEvent){ objects.forEach { if (it is EventsListener) it.onMouseEnter(mouseEvent) } }
    override fun onMouseLeave(mouseEvent: MouseEvent){ objects.forEach { if (it is EventsListener) it.onMouseLeave(mouseEvent) } }

    override fun onClick    (mouseEvent: MouseEvent){ objects.forEach { if (it is EventsListener) it.onClick    (mouseEvent) } }
    override fun onDblClick (mouseEvent: MouseEvent){ objects.forEach { if (it is EventsListener) it.onDblClick (mouseEvent) } }

    override fun onKeyPress (keyboardEvent: KeyboardEvent) { objects.forEach { if (it is EventsListener) it.onKeyPress (keyboardEvent) } }
    override fun onKeyDown  (keyboardEvent: KeyboardEvent) { objects.forEach { if (it is EventsListener) it.onKeyDown  (keyboardEvent) } }
    override fun onKeyUp    (keyboardEvent: KeyboardEvent) { objects.forEach { if (it is EventsListener) it.onKeyUp    (keyboardEvent) } }



}


