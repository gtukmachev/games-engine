package tga.gaming.engine.index

import tga.gaming.engine.Obj

interface SquareIndex {

    fun update(objects: Collection<Obj>) {
        objects.forEach { update(it) }
    }

    fun update(obj: Obj)

    fun remove(objects: Collection<Obj>) {
        objects.forEach { remove(it) }
    }

    fun remove(obj: Obj)

    fun nearest(obj: Obj): Sequence<Obj>

}
