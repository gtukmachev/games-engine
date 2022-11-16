package tga.gaming.engine.image

import kotlinx.browser.window
import org.w3c.dom.Image

val imagesCache = HashMap<String, Image>()

fun loadImage(path: String): Image {
    var image: Image? = imagesCache[path]
    if (image != null) return image

    image = window.document.createElement("img") as Image
    image.src = path

    imagesCache[path] = image
    return image
}

fun loadImages(imgSrcTemplate: String, numberOfImages: Int): List<Image> {
    return List(numberOfImages) {
        loadImage(imgSrcTemplate.replaceFirst(Regex("<n>"), "$it"))
    }
}

