package tga.gaming.engine.dispatcher

import tga.gaming.engine.index.SquareIndex
import tga.gaming.engine.model.Obj

interface GameObjects {

    val index: SquareIndex
    val objects: Set<Obj>

    fun finishGame()
    fun <T: Obj> addObj(obj: T): T
    fun <T: Obj> delObj(obj: T): T

    fun <T: Obj> addObjs(vararg objs: T)

}
