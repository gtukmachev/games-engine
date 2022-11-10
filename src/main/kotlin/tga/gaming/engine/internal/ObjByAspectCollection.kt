package tga.gaming.engine.internal

import tga.gaming.engine.model.Actionable
import tga.gaming.engine.model.Obj

abstract class ObjByAspectCollection: ObjectsByAspectCollection {

    private val allObjects: MutableSet<Obj> = HashSet()
    private val actionableObjects: MutableSet<Actionable> = HashSet()

    override val objects: Set<Obj> get() = allObjects
    override val actionable: Set<Actionable> get() = actionableObjects

    override val size: Int get() = allObjects.size
    override fun contains(element: Obj) = allObjects.contains(element)
    override fun containsAll(elements: Collection<Obj>) = allObjects.containsAll(elements)
    override fun isEmpty() = allObjects.isEmpty()
    override fun iterator() = allObjects.iterator()

    override fun add(element: Obj): Boolean {
        if (element is Actionable) actionableObjects.add(element)
        return allObjects.add(element)
    }

    override fun addAll(elements: Collection<Obj>): Boolean {
        elements.forEach { if (it is Actionable) actionableObjects.add(it) }
        return allObjects.addAll(elements)
    }

    override fun clear() {
        actionableObjects.clear()
        allObjects.clear()
    }

    override fun remove(element: Obj): Boolean {
        if (element is Actionable) actionableObjects.remove(element)
        return allObjects.remove(element)
    }

    override fun removeAll(elements: Collection<Obj>): Boolean {
        elements.forEach { if (it is Actionable) actionableObjects.remove(it) }
        return allObjects.removeAll(elements.toSet())
    }

    override fun retainAll(elements: Collection<Obj>): Boolean {
        val stayOnly = elements.filter { it is Actionable }.toSet() as Set<Actionable>
        actionableObjects.retainAll(stayOnly)
        return allObjects.retainAll(elements.toSet())
    }

}
