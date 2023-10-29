package tga.gaming.engine.drawers

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.Image
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

    // affine matrix (rotate, movement, scale)
    // [  a: cos(a)*scale    b: sin(a)*scale    0  ]
    // [  c:-sin(a)*scale    d: cos(a)*scale    0  ]
    // [  e: tx              f: ty              1  ]

    override fun draw(ctx: CanvasRenderingContext2D) {
        val f = obj.frame ?: return
        val p = obj.p

        val image = when {
            imageIndex < 0            -> images.first()
            imageIndex >= images.size -> images.last()
            else -> images[imageIndex]
        }

        ctx.save()
        ctx.translate(p.x, p.y)
        if (obj.angle != 0.0 ) ctx.rotate(obj.angle)
        ctx.drawImage(image, -f.width/2, -f.height/2, f.width, f.height)
        ctx.restore()
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
