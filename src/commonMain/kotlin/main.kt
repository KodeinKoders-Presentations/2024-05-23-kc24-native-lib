import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kc24_native_lib.generated.resources.Res
import kc24_native_lib.generated.resources.outro
import net.kodein.cup.LocalPresentationState
import net.kodein.cup.Slides
import net.kodein.cup.cupApplication
import net.kodein.cup.currentSlide
import net.kodein.cup.utils.DataMap
import net.kodein.cup.utils.DataMapElement
import net.kodein.theme.cup.KodeinPresentation
import net.kodein.theme.cup.slides.kodeinActivities
import net.kodein.theme.cup.ui.defaultKodeinAnimationDuration
import org.jetbrains.compose.resources.painterResource
import slides.*


data object IsOutro : DataMapElement<IsOutro>(Key) {
    object Key: DataMap.Key<IsOutro>
}

fun main() = cupApplication(
    title = "KC24: Native lib"
) {
    KodeinPresentation(
        slides = presentationSlides,
    ) { content ->
        content()
        AnimatedVisibility(
            visible = IsOutro.Key in LocalPresentationState.current.currentSlide.user,
            enter = slideInVertically(tween(2_500)) { -it },
            exit = fadeOut(tween(defaultKodeinAnimationDuration)),
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            Image(
                painter = painterResource(Res.drawable.outro),
                contentDescription = "Vote!",
                modifier = Modifier
                    .fillMaxHeight(0.92f)
                    .padding(end = 32.dp)
            )
        }
    }
}

val presentationSlides = Slides(
    intro,
    platforms,
    sha256platformAPIs,
    cLibs,
    dynamicCLib,
    crossCompilation,
    staticCLib,
    modernPlatformAPI,
    js,
    conclusion,
    kodeinActivities,
    outro
)
