package tga.gaming.engine.model

interface CompositeMover : Moveable {

    val movers: MutableList<Mover>
    fun add(mover: Mover) {
        if (mover.obj != this) throw CompositeItemDoNotReferToTheObject()
        movers.add(mover)
    }

    override fun move() {
        movers.forEach { it.move() }
    }

}

interface Mover : ObjCompanion {
    fun move()
}