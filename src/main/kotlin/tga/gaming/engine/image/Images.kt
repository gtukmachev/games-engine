package tga.gaming.engine.image

import kotlinx.browser.window
import org.w3c.dom.HTMLImageElement

val imagesCache = HashMap<String, HTMLImageElement>()

fun getImage(path: String): HTMLImageElement {
    var image: HTMLImageElement? = imagesCache[path]
    if (image != null) return image

    image = window.document.createElement("img") as HTMLImageElement
    image.src = path

    imagesCache[path] = image
    return image
}
