package tga.gaming.engine.dispatcher

import tga.gaming.engine.GameWord
import tga.gaming.engine.internal.ObjectsByAspectCollection

interface GameDispatcher : ObjectsByAspectCollection {

    fun turn(ctx: GameWord)

}
