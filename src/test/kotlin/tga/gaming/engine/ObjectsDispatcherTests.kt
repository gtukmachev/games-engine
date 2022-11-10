package tga.gaming.engine

import tga.gaming.engine.dispatcher.ObjectsDispatcher
import tga.gaming.engine.model.Actionable
import tga.gaming.engine.model.Obj
import kotlin.test.Test
import kotlin.test.assertTrue

class ObjectsDispatcherTests {

    @Test
    fun separationByClassTest() {
        val od = ObjectsDispatcher()

        od.add(AnObj())
        od.add(AnObj())
        od.addAll(listOf(AnActionable1(), AnActionable2()))
        od.add(AnObj())
        od.add(AnActionable2())
        od += AnObj()
        od += listOf(AnObj(), AnActionable1())

        val allObjects = od.objects
        val actionable = od.actionable

        assertTrue(allObjects.size == 9, "allObjects.size == 9")
        assertTrue(allObjects.any { it is AnObj } && allObjects.any{ it is Actionable }, "allObjects.any { it is AnObj } && allObjects.any{ it is Actionable }")

        assertTrue(actionable.size == 4, "actionable.size == 4")
        assertTrue(actionable.all{ it is AnActionable2 || it is AnActionable1 }, "actionable.all{ it is AnActionable2 || it is AnActionable1 }")
    }

}

class AnObj : Obj()

class AnActionable1 : Obj(), Actionable {

}
class AnActionable2 : Obj(), Actionable {

}
