package tga.gaming.engine

import tga.gaming.engine.internal.ObjByAspectCollection

class ObjectsDispatcher: ObjByAspectCollection() {

    fun turn(ctx: TurnContext) {
        prepareMove()
        doMove()
        update2dIndex()
        prepareAct()
        doAct()
        finishAct()
    }

    private fun update2dIndex() {
        TODO("Not yet implemented")
    }

    private fun doMove() {
        TODO("Not yet implemented")
    }

    private fun prepareMove() {
        TODO("Not yet implemented")
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

interface TurnContext {

}


