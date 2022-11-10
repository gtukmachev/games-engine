package tga.gaming.engine.dispatcher

import tga.gaming.engine.GameWord
import tga.gaming.engine.internal.ObjByAspectCollection
import tga.gaming.engine.model.Actionable
import tga.gaming.engine.model.Moveable
import tga.gaming.engine.model.Obj

open class ObjectsDispatcher : GameDispatcher, ObjByAspectCollection() {

    override fun turn(word: GameWord) {
        doMove(word)
        doAct(word)
    }

    private val movedObjects = ArrayList<Obj>()
    private fun doMove(word: GameWord) {
        movedObjects.clear()
        objects.forEach {
            if (it is Moveable) {
                val oldPosition = it.p.copy()
                it.move(word)
                if (it.p != oldPosition) movedObjects += it
            }
        }

        if (movedObjects.isNotEmpty()) word.index.update(movedObjects)
    }


    private fun doAct(word: GameWord) {
        objects.forEach {
            if (it is Actionable) {
                it.act(word)
            }
        }

    }


}


