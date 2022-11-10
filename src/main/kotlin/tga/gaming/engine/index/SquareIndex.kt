package tga.gaming.engine.index

import tga.gaming.engine.model.Obj

interface SquareIndex {

    fun update(objects: Collection<Obj>) {
        objects.forEach { update(it) }
    }

    fun update(obj: Obj)

    fun remove(objects: Collection<Obj>) {
        objects.forEach { remove(it) }
    }

    fun remove(obj: Obj)

    fun objectsOnTheSamePlaceWith(obj: Obj): Sequence<Obj>

}
