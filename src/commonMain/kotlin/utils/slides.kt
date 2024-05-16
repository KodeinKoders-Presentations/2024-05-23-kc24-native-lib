package utils

import androidx.compose.runtime.Composable
import net.kodein.cup.*
import net.kodein.cup.utils.DataMap
import net.kodein.cup.utils.emptyDataMap


fun VerticalSlides(
    vararg content: SlideGroup,
    user: DataMap = emptyDataMap(),
): SlideGroup = Slides(
    *content,
    user = user,
    specs = {
        copyWithInsideTransitions(
            config = it,
            startTransitions = TransitionSet.moveVertical,
            endTransitions = TransitionSet.moveVertical
        )
    }
)
