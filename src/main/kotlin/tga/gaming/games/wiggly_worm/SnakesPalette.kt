package tga.gaming.games.wiggly_worm

import tga.gaming.engine.colors.clr
import tga.gaming.engine.colors.multiLinearGradient

object SnakesPalette {
    val colors: Array<SnakeColors> = arrayOf(
        SnakeColors(
            fillStyles = multiLinearGradient(clr(60, 129, 121), 10 to clr(137, 194, 189), 10 to clr(60, 129, 121)),
            strokeStyles = multiLinearGradient(clr(4, 40, 35), 10 to clr(37, 80, 74), 10 to clr(4, 40, 35)),
        ),
        SnakeColors(
            fillStyles = multiLinearGradient(clr(110, 21, 154), 10 to clr(163, 107, 190), 10 to clr(110, 21, 154)),
            strokeStyles = multiLinearGradient(clr(30, 3, 44), 10 to clr(64, 38, 77), 10 to clr(30, 3, 44)),
        ),
        SnakeColors(
            fillStyles = multiLinearGradient(clr(255, 255, 0), 10 to clr(255, 255, 200), 10 to clr(255, 255, 0)),
            strokeStyles = multiLinearGradient(clr(56, 56, 0), 10 to clr(87,87,36), 10 to clr(56, 56, 0)),
        ),
            SnakeColors(
                fillStyles = multiLinearGradient(clr(161,6,17), 10 to clr(203,98,174), 10 to clr(161,6,17)),
                strokeStyles = multiLinearGradient(clr(49,1,35), 10 to clr(79,33,66), 10 to clr(49,1,35)),
            ),
    )

}

data class SnakeColors(
    val fillStyles: Array<String>,
    val strokeStyles: Array<String>,
)