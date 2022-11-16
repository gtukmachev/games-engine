package tga.gaming.engine.dispatcher

import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent

interface EventsListener {
    fun onMouseMove (  mouseEvent: MouseEvent   )
    fun onMouseDown (  mouseEvent: MouseEvent   )
    fun onMouseUp   (  mouseEvent: MouseEvent   )
    fun onMouseEnter(  mouseEvent: MouseEvent   )
    fun onMouseLeave(  mouseEvent: MouseEvent   )
    fun onClick     (  mouseEvent: MouseEvent   )
    fun onDblClick  (  mouseEvent: MouseEvent   )
    fun onKeyPress (keyboardEvent: KeyboardEvent)
    fun onKeyDown  (keyboardEvent: KeyboardEvent)
    fun onKeyUp    (keyboardEvent: KeyboardEvent)
}

interface SimpleEventsListener : EventsListener {
    override fun onMouseMove (  mouseEvent: MouseEvent   ) { }
    override fun onMouseDown (  mouseEvent: MouseEvent   ) { }
    override fun onMouseUp   (  mouseEvent: MouseEvent   ) { }
    override fun onMouseEnter(  mouseEvent: MouseEvent   ) { }
    override fun onMouseLeave(  mouseEvent: MouseEvent   ) { }
    override fun onClick     (  mouseEvent: MouseEvent   ) { }
    override fun onDblClick  (  mouseEvent: MouseEvent   ) { }
    override fun onKeyPress (keyboardEvent: KeyboardEvent) { }
    override fun onKeyDown  (keyboardEvent: KeyboardEvent) { }
    override fun onKeyUp    (keyboardEvent: KeyboardEvent) { }
}
