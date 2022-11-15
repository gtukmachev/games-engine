package tga.gaming.engine.image

import kotlinx.browser.window
import org.w3c.dom.Image

val imagesCache = HashMap<String, Image>()

fun getImage(path: String): Image {
    var image: Image? = imagesCache[path]
    if (image != null) return image

    image = window.document.createElement("img") as Image
    image.src = path

    imagesCache[path] = image
    return image
}
