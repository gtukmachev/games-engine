package tga.gaming.engine.model

import tga.gaming.engine.index.SquareIndex
import tga.gaming.engine.internal.ObjectsByAspectCollection

interface TurnContext {

    val index: SquareIndex
    val objects: ObjectsByAspectCollection

    fun addObj(obj: Obj)
    fun delObj(obj: Obj)

}
