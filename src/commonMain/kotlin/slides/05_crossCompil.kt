package slides

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.kodein.cup.PreparedSlide
import net.kodein.cup.Slide
import net.kodein.cup.sa.rememberSourceCode
import net.kodein.cup.ui.styled
import net.kodein.theme.KodeinColors
import net.kodein.theme.compose.JetBrainsMonoNL
import net.kodein.theme.compose.m2.Link
import net.kodein.theme.cup.KodeinSourceCode
import net.kodein.theme.cup.KodeinStyleSheet
import net.kodein.theme.cup.kcStyled
import net.kodein.theme.cup.ui.KodeinAnimatedVisibility
import net.kodein.theme.cup.ui.KodeinFadeAnimatedVisibility
import org.kodein.emoji.Emoji
import org.kodein.emoji.compose.NotoAnimatedEmoji
import org.kodein.emoji.smileys_emotion.face_negative.ImpSmile
import org.kodein.emoji.smileys_emotion.face_smiling.Laughing
import utils.VerticalSlides


private object CStyleSheet : KodeinStyleSheet() {
    val green by registerMarker(SpanStyle(color = Color.Green))
}

val check by Slide(
    stepCount = 3
) { step ->
    Text(kcStyled { "Let's ${+sc}${+m}check${-m}${-sc} our code!" }, style = MaterialTheme.typography.h2)
    Text(kcStyled { "(on my mac)" }, style = MaterialTheme.typography.h4)

    Spacer(Modifier.height(16.dp))

    ProvideTextStyle(TextStyle(
        fontFamily = JetBrainsMonoNL,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        color = Color.White
    )) {
        Column(
            Modifier
                .width(384.dp)
                .background(Color.Black, RoundedCornerShape(4.dp))
                .border(1.dp, Color(0xFF_008800), RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            Text(styled(CStyleSheet) { "${+green}$>${-green} ./gradlew ${+b}check${-b}" })
            KodeinAnimatedVisibility(step == 1) {
                Text(styled(CStyleSheet) { "Exception [...]: ${+b}'openssl/evp.h' file not found${-b}" }, color = Color(0xFF_FF4444))
            }
            KodeinAnimatedVisibility(step == 2) {
                Text(styled(CStyleSheet) { "ld.lld: error: ${+b}unable to find library -lcrypto${-b}" }, color = Color(0xFF_FF4444))
            }
        }
    }
}

val noCrossCompilDisabling by Slide(
    stepCount = 3
) { step ->
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.padding(16.dp)) {
            KodeinFadeAnimatedVisibility(step == 0, durationMillis = 800) {
                NotoAnimatedEmoji(Emoji.Laughing, modifier = Modifier.size(96.dp))
            }
            KodeinFadeAnimatedVisibility(step >= 1, durationMillis = 800) {
                NotoAnimatedEmoji(Emoji.ImpSmile, iterations = 1, stopAt = 0.76f, modifier = Modifier.size(96.dp))
            }
        }
        KodeinAnimatedVisibility(visible = step >= 1, durationMillis = 1_000) {
            Text(
                text = "Cross-compilation is enabled by default. There is no API to configure or disable it!",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .background(net.kodein.theme.compose.Color(KodeinColors.purple_dark), RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
    }
    KodeinAnimatedVisibility(visible = step >= 1, durationMillis = 1_000) {
        Spacer(Modifier.height(8.dp))
        Link("https://youtrack.jetbrains.com/issue/KT-30498") {
            Text("KT-30498", fontSize = 24.sp)
        }
        KodeinAnimatedVisibility(visible = step >= 2) {
            Spacer(Modifier.height(4.dp))
            Text("Over 5 years ago (18 mar 2019)")
        }
    }
}

val disableCrossCompilation by PreparedSlide {
    val sourceCode = rememberSourceCode("kotlin") {
        """
            // Disable cross compilation
            val nativeTarget = when (val p = OperatingSystem.current().nativePrefix) {
                "linux-amd64" -> linuxX64()
                "macos-amd64" -> macosX64()
                "macos-aarch64" -> macosArm64()
                "win32-x86" -> mingwX64()
                else -> error("Unknown OS ${'$'}p")
            }
            configure(listOf(macosX64(), macosArm64(), linuxX64(), mingwX64()) - nativeTarget) {
                compilations.all {
                    cinterops.all { tasks[interopProcessingTaskName].enabled = false }
                    compileTaskProvider.configure { enabled = false }
                    tasks.named(processResourcesTaskName).configure { enabled = false }
                }
                binaries.all { linkTask.enabled = false }
            
                mavenPublication {
                    val publicationToDisable = this
                    tasks.withType<AbstractPublishToMaven>()
                        .all { onlyIf { publication != publicationToDisable } }
                    tasks.withType<GenerateModuleMetadata>()
                        .all { onlyIf { publication.get() != publicationToDisable } }
                }
            }
        """
    }
    slideContent {
        Text("Disable cross-compilation", style = MaterialTheme.typography.h2)
        Spacer(Modifier.height(8.dp))
        KodeinSourceCode(sourceCode, file = "build.gradle.kts", fontSize = 8.sp)
    }
}

val crossCompilation = VerticalSlides(
    check,
    noCrossCompilDisabling,
    disableCrossCompilation
)
