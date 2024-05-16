package anims

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize


internal data class Coordinates(
    val offset: Offset,
    val size: Size,
)

class AbsoluteTransitionBlocks internal constructor(
    private val animating: Boolean,
    private val blocks: Map<String, @Composable BoxScope.() -> Unit>,
    private val coordinates: MutableMap<String, Coordinates>
) {
    @Composable
    fun Block(
        id: String,
        modifier: Modifier = Modifier,
    ) {
        blocks[id]?.let { block ->
            Box(
                content = block,
                modifier = modifier
                    .onGloballyPositioned {
                        coordinates[id] = Coordinates(it.positionInRoot(), it.size.toSize())
                    }
                    .drawWithContent {
                        if (!animating) drawContent()
                    }
            )
        }
    }
}

class AbsoluteTransitionBuilder internal constructor(
    private val animating: Boolean,
    private val setCoordinates: suspend (Map<String, Coordinates>) -> Unit,
) {
    internal val blocks = HashMap<String, @Composable BoxScope.() -> Unit>()

    fun block(
        id: String,
        content: @Composable BoxScope.() -> Unit
    ) {
        blocks[id] = content
    }

    @Composable
    fun Content(
        isCurrent: Boolean,
        modifier: Modifier = Modifier,
        contentAlignment: Alignment = Alignment.Center,
        content: @Composable BoxScope.(AbsoluteTransitionBlocks) -> Unit
    ) {
        val coordinates: MutableMap<String, Coordinates> = remember { mutableStateMapOf() }

        LaunchedEffect(isCurrent) {
            if (isCurrent) {
                setCoordinates(coordinates)
            }
        }

        Box(
            contentAlignment = contentAlignment,
            modifier = modifier
        ) {
            content(AbsoluteTransitionBlocks(animating, blocks, coordinates))
        }
    }
}

@Composable
fun AbsoluteTransitionsBox(
    modifier: Modifier = Modifier,
    transitionDurationMillis: Int = 1_000,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable AbsoluteTransitionBuilder.() -> Unit
) {
    val density = LocalDensity.current
    var rootOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        contentAlignment = contentAlignment,
        modifier = modifier
            .onGloballyPositioned { rootOffset = it.positionInRoot() }
    ) {
        var currentCoordinates: Map<String, Coordinates> by remember { mutableStateOf(mutableStateMapOf()) }
        var previousCoordinates: Map<String, Coordinates> by remember { mutableStateOf(mutableStateMapOf()) }

        val transition = remember { Animatable(1f) }

        val animating = transition.value != 1f
        val builder = AbsoluteTransitionBuilder(
            animating = animating,
            setCoordinates = {
                if (it === currentCoordinates) return@AbsoluteTransitionBuilder
                previousCoordinates = currentCoordinates
                currentCoordinates = it
                transition.snapTo(0f)
                transition.animateTo(1f, tween(transitionDurationMillis))
            }
        )
        builder.content()

        currentCoordinates.forEach { (id, coordinates) ->
            val lerpedCoordinates = previousCoordinates[id]?.let {
                Coordinates(
                    lerp(it.offset, coordinates.offset, transition.value),
                    lerp(it.size, coordinates.size, transition.value)
                )
            } ?: coordinates
            builder.blocks[id]?.let { block ->
                with(density) {
                    val offset = lerpedCoordinates.offset - rootOffset
                    val size = lerpedCoordinates.size
                    Box(
                        content = block,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(offset.x.toDp(), offset.y.toDp())
                            .size(size.toDpSize())
                            .drawWithContent {
                                if (animating) drawContent()
                            }
                    )
                }
            }
        }
    }
}
