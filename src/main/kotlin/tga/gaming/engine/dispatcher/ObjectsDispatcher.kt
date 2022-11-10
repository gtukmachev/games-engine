package tga.gaming.engine.dispatcher

import tga.gaming.engine.index.SquareIndex
import tga.gaming.engine.model.Actionable
import tga.gaming.engine.model.Moveable
import tga.gaming.engine.model.Obj

open class ObjectsDispatcher(
    override val index: SquareIndex
) : GameTurner, GameObjects {
    override val objects: MutableSet<Obj> = HashSet()

    override fun turn() {
        movedObjects.clear()
        objectToAdd.clear()
        objectToDel.clear()

        doMove()
        update2dIndex()
        doAct()
        removeDeletedObjects()
        addNewObjects()
    }

    private val movedObjects = ArrayList<Obj>()
    private fun doMove() {
        objects.forEach {
            if (it is Moveable) {
                val oldPosition = it.p.copy()
                it.move(this)
                if (it.p != oldPosition) movedObjects += it
            }
        }

    }

    private fun update2dIndex() {
        if (movedObjects.isNotEmpty()) index.update(movedObjects)
    }

    private fun doAct() {
        objects.forEach {
            if (it is Actionable) it.act(this)
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


}


