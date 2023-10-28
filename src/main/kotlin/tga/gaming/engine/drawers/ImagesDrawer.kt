package tga.gaming.engine.drawers

import kotlinx.browser.document
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.Image
import tga.gaming.engine.model.CompositeDrawer
import tga.gaming.engine.model.Drawer
import tga.gaming.engine.model.Obj
import tga.gaming.engine.model.v
import kotlin.math.PI
import kotlin.math.sqrt

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
        val p = obj.p

        val image = when {
            imageIndex < 0            -> images.first()
            imageIndex >= images.size -> images.last()
            else -> images[imageIndex]
        }


        val w = f.width; val h = f.height
        val diagonalLength: Double = sqrt(w*w + h*h) + 1
        val diagonalLengthInt = diagonalLength.toInt()

        val newCanvas = (document.createElement("canvas") as HTMLCanvasElement).apply {
            width = diagonalLengthInt
            height = diagonalLengthInt
        }
        val newCtx = newCanvas.getContext("2d")!! as CanvasRenderingContext2D

        val canvasCenter = diagonalLength / 2.0

        newCtx.translate(canvasCenter, canvasCenter)
        newCtx.rotate(obj.angle)
        newCtx.drawImage(image, -f.width/2, -f.height/2, f.width, f.height)

        ctx.drawImage(newCanvas, p.x-diagonalLength/2, p.y-diagonalLength/2, diagonalLength, diagonalLength)
    }



}

fun CompositeDrawer.withImageDrawer(img: Image): ImagesDrawer {
    return withImagesDrawer(listOf(img))
}

fun CompositeDrawer.withImagesDrawer(images: List<Image>, imageIndex: Int = 0): ImagesDrawer {
    val imagesDrawer = ImagesDrawer(
        obj = this as Obj,
        imageIndex = imageIndex,
        images = images
    )

    this.drawers.add(imagesDrawer)

    return imagesDrawer
}
