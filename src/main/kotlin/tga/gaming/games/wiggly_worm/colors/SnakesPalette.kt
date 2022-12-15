package tga.gaming.games.wiggly_worm.colors

import tga.gaming.engine.colors.Clr
import tga.gaming.engine.colors.multiLinearGradient

object SnakesPalette {
    val colors: Array<SnakeColors> = arrayOf(
        SnakeColors(
            fillStyles = multiLinearGradient(Clr(60, 129, 121), 10 to Clr(137, 194, 189), 10 to Clr(60, 129, 121)),
            strokeStyles = multiLinearGradient(Clr(4, 40, 35), 10 to Clr(37, 80, 74), 10 to Clr(4, 40, 35)),
        ),
        SnakeColors(
            fillStyles = multiLinearGradient(Clr(110, 21, 154), 10 to Clr(163, 107, 190), 10 to Clr(110, 21, 154)),
            strokeStyles = multiLinearGradient(Clr(30, 3, 44), 10 to Clr(64, 38, 77), 10 to Clr(30, 3, 44)),
        ),
        SnakeColors(
            fillStyles = multiLinearGradient(Clr(255, 255, 0), 10 to Clr(255, 255, 200), 10 to Clr(255, 255, 0)),
            strokeStyles = multiLinearGradient(Clr(56, 56, 0), 10 to Clr(87,87,36), 10 to Clr(56, 56, 0)),
        ),
            SnakeColors(
                fillStyles = multiLinearGradient(Clr(161,6,17), 10 to Clr(203,98,174), 10 to Clr(161,6,17)),
                strokeStyles = multiLinearGradient(Clr(49,1,35), 10 to Clr(79,33,66), 10 to Clr(49,1,35)),
            ),
    )

}

data class SnakeColors(
    val fillStyles: Array<String>,
    val strokeStyles: Array<String>,
)