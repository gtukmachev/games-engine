package tga.gaming.engine.internal

object IdSequence {

    private var currentId: Long = 0L

    fun next(): Long {
        return ++currentId
    }
}
