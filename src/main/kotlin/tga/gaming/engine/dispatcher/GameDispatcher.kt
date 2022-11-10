package tga.gaming.engine.dispatcher

import tga.gaming.engine.internal.ObjectsByAspectCollection
import tga.gaming.engine.model.TurnContext

interface GameDispatcher : ObjectsByAspectCollection {

    fun turn(ctx: TurnContext)

}
