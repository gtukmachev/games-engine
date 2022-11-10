package tga.gaming.engine.dispatcher

import tga.gaming.engine.internal.ObjByAspectCollection
import tga.gaming.engine.model.TurnContext

open class ObjectsDispatcher : GameDispatcher, ObjByAspectCollection() {

    override fun turn(ctx: TurnContext) {
        prepareMove(ctx)
        doMove(ctx)
        update2dIndex(ctx)
        prepareAct(ctx)
        doAct(ctx)
        finishAct(ctx)
        removeDeadObjects(ctx)
    }

    private fun removeDeadObjects(ctx: TurnContext) {
        TODO("Not yet implemented")
    }

    private fun finishAct(ctx: TurnContext) {
        TODO("Not yet implemented")
    }

    private fun doAct(ctx: TurnContext) {
        TODO("Not yet implemented")
    }

    private fun prepareAct(ctx: TurnContext) {
        TODO("Not yet implemented")
    }

    private fun update2dIndex(ctx: TurnContext) {
        TODO("Not yet implemented")
    }

    private fun doMove(ctx: TurnContext) {
        TODO("Not yet implemented")
    }

    private fun prepareMove(ctx: TurnContext) {
        TODO("Not yet implemented")
    }


}


