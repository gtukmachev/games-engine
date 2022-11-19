package tga.gaming.engine.dispatcher

import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.pointerevents.PointerEvent

interface EventsListener {
    fun onMouseMove (me: MouseEvent)
    fun onMouseDown (me: MouseEvent)
    fun onMouseUp   (me: MouseEvent)
    fun onMouseEnter(me: MouseEvent)
    fun onMouseLeave(me: MouseEvent)

    fun onGotPointerCapture (pe: PointerEvent)
    fun onLostPointerCapture(pe: PointerEvent)
    fun onPointerDown       (pe: PointerEvent)
    fun onPointerMove       (pe: PointerEvent)
    fun onPointerUp         (pe: PointerEvent)
    fun onPointerCancel     (pe: PointerEvent)
    fun onPointerOver       (pe: PointerEvent)
    fun onPointerOut        (pe: PointerEvent)
    fun onPointerEnter      (pe: PointerEvent)
    fun onPointerLeave      (pe: PointerEvent)

    fun onClick   (me: MouseEvent)
    fun onDblClick(me: MouseEvent)

    fun onKeyPress (ke: KeyboardEvent)
    fun onKeyDown  (ke: KeyboardEvent)
    fun onKeyUp    (ke: KeyboardEvent)
}

interface SimpleEventsListener : EventsListener {
    override fun onMouseMove (me: MouseEvent) { }
    override fun onMouseDown (me: MouseEvent) { }
    override fun onMouseUp   (me: MouseEvent) { }
    override fun onMouseEnter(me: MouseEvent) { }
    override fun onMouseLeave(me: MouseEvent) { }

    override fun onGotPointerCapture (pe: PointerEvent) { }
    override fun onLostPointerCapture(pe: PointerEvent) { }
    override fun onPointerDown       (pe: PointerEvent) { }
    override fun onPointerMove       (pe: PointerEvent) { }
    override fun onPointerUp         (pe: PointerEvent) { }
    override fun onPointerCancel     (pe: PointerEvent) { }
    override fun onPointerOver       (pe: PointerEvent) { }
    override fun onPointerOut        (pe: PointerEvent) { }
    override fun onPointerEnter      (pe: PointerEvent) { }
    override fun onPointerLeave      (pe: PointerEvent) { }

    override fun onClick   (me: MouseEvent) { }
    override fun onDblClick(me: MouseEvent) { }

    override fun onKeyPress(ke: KeyboardEvent) { }
    override fun onKeyDown (ke: KeyboardEvent) { }
    override fun onKeyUp   (ke: KeyboardEvent) { }
}
