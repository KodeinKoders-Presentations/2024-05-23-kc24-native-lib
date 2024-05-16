package utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas


internal class TintModifier(
    private val blendMode: BlendMode = BlendMode.Overlay,
    private val color: Color
) : DrawModifier {
    override fun ContentDrawScope.draw() {
        val saturationFilter = ColorFilter.tint(color, blendMode)
        val paint = Paint().apply {
            colorFilter = saturationFilter
        }
        drawIntoCanvas {
            it.saveLayer(Rect(0f, 0f, size.width, size.height), paint)
            drawContent()
            it.restore()
        }
    }
}

fun Modifier.tint(color: Color, blendMode: BlendMode = BlendMode.Modulate) =
    this.then(TintModifier(color = color, blendMode = blendMode))
