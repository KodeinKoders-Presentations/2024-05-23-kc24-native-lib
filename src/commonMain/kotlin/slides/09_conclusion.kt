package slides

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.kodein.cup.SLIDE_SIZE_16_9
import net.kodein.cup.Slide
import net.kodein.cup.widgets.material.BulletPoints
import net.kodein.theme.KodeinColors
import net.kodein.theme.compose.Color
import net.kodein.theme.cup.ui.KodeinAnimatedVisibility
import net.kodein.theme.cup.ui.defaultKodeinAnimationDuration
import org.kodein.emoji.Emoji
import org.kodein.emoji.compose.NotoAnimatedEmoji
import org.kodein.emoji.smileys_emotion.face_affection.StarStruck
import utils.VerticalSlides


val conclusionSkills by Slide(
    specs = { copy(size = SLIDE_SIZE_16_9) },
    stepCount = 2
) { step ->
    Text("To use native APIs in Kotlin/Multiplatform", style = MaterialTheme.typography.h4)
    Text("You need to understand:", style = MaterialTheme.typography.h2)

    Spacer(Modifier.height(16.dp))

    BulletPoints(
        spacedBy = animateDpAsState(if (step == 0) 8.dp else 2.dp, tween(defaultKodeinAnimationDuration)).value
    ) {
        BulletPoint { Text("C and Obj-C") }
        BulletPoint { Text("Kotlin C-Interop") }
        BulletPoint { Text("JNI or JNA") }
        BulletPoint { Text("System compilation configuration") }
        BulletPoint { Text("Native toolchains (autotools, CMake, ...)") }
        BulletPoint { Text("Modern languages (Swift, C#, Rust, ...)") }
        BulletPoint { Text("Wasm JS memory management") }
    }
    KodeinAnimatedVisibility(visible = step >= 1) {
        Spacer(Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NotoAnimatedEmoji(Emoji.StarStruck, iterations = 1, stopAt = 1f, modifier = Modifier.size(64.dp))
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color(KodeinColors.orange_dark), RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "What an amazing journey!",
                    fontSize = 18.sp,
                )
                Text(
                    text = "(Let's hope it becomes less amazing in the future!)",
                    fontSize = 12.sp,
                )
            }
        }
    }
}

val conclusion = VerticalSlides(
    conclusionSkills
)