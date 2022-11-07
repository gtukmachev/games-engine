package tga.gaming.engine

import tga.gaming.engine.internal.ObjByAspectCollection

class ObjectsDispatcher: ObjByAspectCollection() {

    fun turn() {
        prepareAct()
        doAct()
        finishAct()
    }

    private fun prepareAct() {
        actionable.forEach(Actionable::onPrepareAct)
    }

    private fun doAct() {
        actionable.forEach(Actionable::onDoAct)
    }

    private fun finishAct() {
        actionable.forEach(Actionable::onFinishAct)
    }

}



