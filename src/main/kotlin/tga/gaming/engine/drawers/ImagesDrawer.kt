package tga.gaming.engine.drawers

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.Image
import tga.gaming.engine.image.getImage
import tga.gaming.engine.model.CompositeDrawer
import tga.gaming.engine.model.Drawer
import tga.gaming.engine.model.Obj

class ImagesDrawer(
    override val obj: Obj,
    var imageIndex: Int,
    private val images: List<Image>
) : Drawer {

    fun nextImage() {
        imageIndex += 1
        if (imageIndex == images.size) imageIndex = 0
    }

    override fun draw(ctx: CanvasRenderingContext2D) {
        val f = obj.frame ?: return

        val image = when {
            imageIndex < 0            -> images.first()
            imageIndex >= images.size -> images.last()
            else -> images[imageIndex]
        }

        ctx.drawImage(image, f.p0.x, f.p0.y, f.width, f.height)
    }

}

fun CompositeDrawer.withImagesDrawer(imgSrcTemplate: String, numberOfImages: Int): ImagesDrawer {
    val images = List(numberOfImages) {
        getImage(imgSrcTemplate.replaceFirst(Regex("<n>"), "$it")) as Image
    }

    return withImagesDrawer(images)
}

fun CompositeDrawer.withImageDrawer(img: Image): ImagesDrawer {
    return withImagesDrawer(listOf(img))
}

fun CompositeDrawer.withImagesDrawer(images: List<Image>): ImagesDrawer {
    val imagesDrawer = ImagesDrawer(
        obj = this as Obj,
        imageIndex = 0,
        images = images
    )

    this.drawers.add(imagesDrawer)

    return imagesDrawer
}
