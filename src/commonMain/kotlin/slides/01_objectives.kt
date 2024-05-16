package slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.kodein.cup.Slide
import net.kodein.cup.Slides
import net.kodein.cup.widgets.material.BulletPoints
import net.kodein.theme.cup.ui.KodeinAnimatedVisibility
import net.kodein.theme.cup.ui.KodeinFadeAnimatedVisibility
import org.kodein.emoji.Emoji
import org.kodein.emoji.compose.NotoAnimatedEmoji
import org.kodein.emoji.smileys_emotion.face_negative.ImpSmile
import org.kodein.emoji.smileys_emotion.face_smiling.Laughing
import utils.VerticalSlides


private val whatYouWant by Slide(
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

val expectations by Slide(
    stepCount = 3
) { step ->
    Column(
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Native libs in KMP are:",
            style = MaterialTheme.typography.h3
        )

        BulletPoints(spacedBy = 2.dp) {
            BulletPoint { Text("Clunky.") }
            BulletPoint { Text("Under-supported.") }
            BulletPoint { Text("Under-documented.") }
            BulletPoint { Text("Very hard!") }
        }

        KodeinAnimatedVisibility(step >= 1) {
            Text(
                text = "...but also:",
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(top = 8.dp)
            )
            BulletPoints(spacedBy = 2.dp) {
                BulletPoint { Text("Fun!") }
                BulletPoint { Text("Very satisfying!") }
                BulletPoint(step >= 2) { Text("Sometimes needed.") }
            }
        }
    }
}

val objectives = VerticalSlides(
    whatYouWant,
    expectations,
)