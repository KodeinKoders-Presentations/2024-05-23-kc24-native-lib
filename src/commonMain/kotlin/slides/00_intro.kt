package slides

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.qrose.options.*
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import kc24_native_lib.generated.resources.Res
import kc24_native_lib.generated.resources.kc24
import net.kodein.cup.SLIDE_SIZE_16_9
import net.kodein.cup.Slide
import net.kodein.theme.KodeinColors
import net.kodein.theme.compose.Color
import net.kodein.theme.compose.m2.Link
import net.kodein.theme.cup.ui.KodeinAnimatedVisibility
import net.kodein.theme.cup.ui.KodeinFadeAnimatedVisibility
import net.kodein.theme.cup.ui.KodeinLogo
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.kodein.emoji.Emoji
import org.kodein.emoji.compose.NotoAnimatedEmoji
import org.kodein.emoji.smileys_emotion.face_negative.ImpSmile
import org.kodein.emoji.smileys_emotion.face_smiling.Laughing
import utils.VerticalSlides


@Composable
fun LinksToThisPresentation() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Link(
            uri = "https://p.kodein.net/kc24"
        ) {
            Text(
                text = "p.kodein.net/kc24",
                style = MaterialTheme.typography.caption,
                color = Color(KodeinColors.coral)
            )
        }
        Image(
            painter = rememberQrCodePainter(
                "https://p.kodein.net/kc24",
                shapes = QrShapes(
                    ball = QrBallShape.roundCorners(.25f),
                    frame = QrFrameShape.roundCorners(.25f),
                    darkPixel = QrPixelShape.circle(),
                ),
                colors = QrColors(
                    dark = QrBrush.solid(Color(KodeinColors.coral))
                )
            ),
            contentDescription = "This presentation",
            modifier = Modifier.padding(2.dp).size(96.dp)
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
val title by Slide(
    specs = { copy(size = SLIDE_SIZE_16_9) }
) {
    Spacer(Modifier.height(16.dp))

    val scale by rememberInfiniteTransition().animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(2_500), RepeatMode.Reverse)
    )
    Link("https://kotlinconf.com/2024") {
        Image(
            painter = painterResource(Res.drawable.kc24),
            contentDescription = "KotlinConf 2024",
            modifier = Modifier
                .scale(scale)
                .fillMaxWidth(0.75f)
                .padding(end = 32.dp)
        )
    }

    Spacer(Modifier.weight(1f))

    Text(
        text = "Using C & native platforms in Kotlin",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h1
    )
    Text(
        text = "Building a multi-platform advanced library",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h4
    )

    Spacer(Modifier.weight(1f))

    Row {
        KodeinLogo(
            division = "Koders",
            modifier = Modifier.height(80.dp)
        ) {
            Text("Salomon BRYS", fontSize = 22.sp, modifier = Modifier.padding(top = 4.dp))
        }

        Spacer(Modifier.width(32.dp))

        LinksToThisPresentation()
    }

    Spacer(Modifier.height(16.dp))
}

private val lol by Slide(
    stepCount = 2
) { step ->
    Text(
        text = "So you want to use a native API in your Kotlin/Multiplatform project?",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h2
    )

    Box(Modifier.padding(16.dp)) {
        KodeinFadeAnimatedVisibility(step == 0, durationMillis = 800) {
            NotoAnimatedEmoji(Emoji.Laughing, modifier = Modifier.size(96.dp))
        }
        KodeinFadeAnimatedVisibility(step >= 1, durationMillis = 800) {
            NotoAnimatedEmoji(Emoji.ImpSmile, iterations = 1, stopAt = 0.76f, modifier = Modifier.size(96.dp))
        }
    }

    KodeinAnimatedVisibility(step >= 1) {
        Text(
            text = "...you fools!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h2
        )
    }
}

val intro = VerticalSlides(
    title,
    lol
)