package tga.gaming.engine.model

import tga.gaming.engine.GameWord

interface Actionable {
    fun act(word: GameWord) { }
}
