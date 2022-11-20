package tga.gaming.engine.dispatcher

import tga.gaming.engine.index.SquareIndex
import tga.gaming.engine.model.Obj

interface GameObjects {

    val index: SquareIndex
    val objects: Set<Obj>

    fun finishGame()
    fun addObj(obj: Obj)
    fun delObj(obj: Obj)

}
