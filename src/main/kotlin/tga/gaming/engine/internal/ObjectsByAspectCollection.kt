package tga.gaming.engine.internal

import tga.gaming.engine.model.Actionable
import tga.gaming.engine.model.Obj

interface ObjectsByAspectCollection: MutableCollection<Obj> {
    val objects: Set<Obj>
    val actionable: Set<Actionable>
}
