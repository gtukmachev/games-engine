package tga.gaming.engine.model

import tga.gaming.engine.dispatcher.GameObjects

interface Actionable {
    fun act(dispatcher: GameObjects) { }
}
