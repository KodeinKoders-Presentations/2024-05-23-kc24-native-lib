package slides

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import net.kodein.cup.SLIDE_SIZE_16_9
import net.kodein.cup.Slide
import net.kodein.cup.widgets.material.BulletPoints
import net.kodein.theme.KodeinColors
import net.kodein.theme.compose.Color
import net.kodein.theme.cup.kStyled
import net.kodein.theme.cup.ui.KodeinAnimatedVisibility
import org.kodein.emoji.Emoji
import org.kodein.emoji.activities.arts_crafts.Knot
import org.kodein.emoji.compose.NotoImageEmoji
import org.kodein.emoji.objects.office.Clipboard
import org.kodein.emoji.objects.tool.Link
import org.kodein.emoji.symbols.av_symbol.PlayButton
import utils.VerticalSlides
import utils.tint

val cLibsTitle by Slide(
    stepCount = 3,
    specs = { copy(size = SLIDE_SIZE_16_9) }
) { step ->
    ProvideTextStyle(MaterialTheme.typography.h2) {
        Row {
            KodeinAnimatedVisibility(step in 0..1) {
                Text("Sha256: ")
            }
            Text("Kotlin/Native")
            KodeinAnimatedVisibility(step >= 1) {
                Text(" external")
            }
            Text(" C API")
            KodeinAnimatedVisibility(step in 0..1) {
                Text("  (Linux)")
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    ProvideTextStyle(MaterialTheme.typography.h4) {
        KodeinAnimatedVisibility(step == 0) {
            Text(" ")
        }
        KodeinAnimatedVisibility(step == 1) {
            Text("With OpenSSL")
        }
        KodeinAnimatedVisibility(step >= 2) {
            Text("In general")
        }
    }
}

val staticOrDynamic by Slide(
    stepCount = 11
) { step ->

    KodeinAnimatedVisibility(step == 0 || step >= 10) {
        Text("Static or Dynamic?", style = MaterialTheme.typography.h2)
    }

    KodeinAnimatedVisibility(step in 1..9) {
        val animDuration = 1_000
        Spacer(Modifier.height(16.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            val staticLibVisible by animateFloatAsState(if (step <= 1) 1f else 0f, tween(animDuration))
            Column(
                Modifier
                    .width(224.dp)
                    .scale(staticLibVisible * 0.8f + 0.2f)
                    .alpha(staticLibVisible)
            ) {
                Text("Static library", style = MaterialTheme.typography.h6, color = Color(KodeinColors.orange_light))
                Column(
                    Modifier
                        .fillMaxWidth()
                        .border(2.dp, Color(KodeinColors.orange_light), RoundedCornerShape(8.dp))
                        .padding(4.dp)
                ) {
                    Text("Symbols table:")
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .border(2.dp, Color(KodeinColors.purple_light), RoundedCornerShape(8.dp))
                            .padding(4.dp)
                    ) {
                        BulletPoints(spacedBy = 0.dp) {
                            BulletPoint {
                                Text("List of defined symbols")
                            }
                            BulletPoint {
                                Text("List of needed symbols")
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Binary code definition:")
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .border(2.dp, Color(KodeinColors.purple_light), RoundedCornerShape(8.dp))
                            .padding(4.dp)
                    ) {
                        BulletPoints(spacedBy = 0.dp) {
                            BulletPoint {
                                Text("Defined symbol 1")
                            }
                            BulletPoint {
                                Text("Defined symbol 2")
                            }
                            BulletPoint {
                                Text("...")
                            }
                        }
                    }
                }
            }

            val linkVisible by animateFloatAsState(if (step in 2..4) 1f else 0f, tween(animDuration))
            Box(
                Modifier
                    .scale(1f + 2 * (1f - linkVisible))
                    .alpha(linkVisible)
                    .size(animateDpAsState(if (step in 3..4) 192.dp else 64.dp, tween(animDuration)).value)
            ) {
                NotoImageEmoji(Emoji.Clipboard, Modifier.size(64.dp).align(Alignment.TopStart))
                NotoImageEmoji(Emoji.Clipboard, Modifier.size(64.dp).align(Alignment.TopEnd))
                NotoImageEmoji(Emoji.Clipboard, Modifier.size(64.dp).align(Alignment.BottomStart))
                NotoImageEmoji(Emoji.Clipboard, Modifier.size(64.dp).align(Alignment.BottomEnd))

                val centralLinkVisible by animateFloatAsState(if (step >= 4) 1f else 0f, tween(animDuration))
                NotoImageEmoji(Emoji.Link, Modifier.size(64.dp).align(Alignment.Center).graphicsLayer(alpha = centralLinkVisible, rotationZ = (1f - centralLinkVisible) * 360))
            }

            Column(
                Modifier
                    .width(224.dp)
                    .alpha(animateFloatAsState(if (step == 5 || step >= 8) 1f else 0f, tween(animDuration)).value)
                    .scale(animateFloatAsState(if (step <= 5 || step >= 8) 1f else 0.2f, tween(animDuration)).value)
            ) {
                Column(Modifier.fillMaxWidth()) {
                    KodeinAnimatedVisibility(step < 9, durationMillis = animDuration) {
                        Text("Executable", style = MaterialTheme.typography.h6, color = Color(KodeinColors.orange_light))
                    }
                    KodeinAnimatedVisibility(step >= 9, durationMillis = animDuration) {
                        Text("Dynamic Library", style = MaterialTheme.typography.h6, color = Color(KodeinColors.orange_light))
                    }
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .border(2.dp, Color(KodeinColors.orange_light), RoundedCornerShape(8.dp))
                        .padding(4.dp)
                ) {
                    Text("Symbols table:")
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .border(2.dp, Color(KodeinColors.purple_light), RoundedCornerShape(8.dp))
                            .padding(4.dp)
                    ) {
                        BulletPoints(spacedBy = 0.dp) {
                            BulletPoint {
                                Text("List of provided symbols")
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(kStyled { "${+b}Executable${-b} binary code:" })
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .border(2.dp, Color(KodeinColors.purple_light), RoundedCornerShape(8.dp))
                            .padding(4.dp)
                    ) {
                        BulletPoints(spacedBy = 0.dp, animationDurationMillis = animDuration) {
                            BulletPoint(step < 9) {
                                Text("main(argc, argv)")
                            }
                            BulletPoint {
                                Text("Linked symbol 1")
                            }
                            BulletPoint {
                                Text("Linked symbol 2")
                            }
                            BulletPoint {
                                Text("...")
                            }
                        }
                    }
                }
            }

            val loadVisible by animateFloatAsState(if (step in 6..7) 1f else 0f, tween(animDuration))
            Box(
                Modifier
                    .scale(1f + 2 * (1f - loadVisible))
                    .alpha(loadVisible)
                    .size(192.dp)
            ) {
                val dylinkVisible by animateFloatAsState(if (step >= 7) 1f else 0f, tween(animDuration))
                with (LocalDensity.current) {
                    NotoImageEmoji(Emoji.PlayButton, Modifier.size(64.dp).align(Alignment.Center))

                    NotoImageEmoji(Emoji.PlayButton, Modifier.size(32.dp).align(Alignment.TopStart).graphicsLayer(translationX = ((-32).dp * (1f - dylinkVisible)).toPx(), translationY = ((-32).dp * (1f - dylinkVisible)).toPx(), alpha = dylinkVisible).tint(Color.Cyan))
                    NotoImageEmoji(Emoji.Knot, Modifier.padding(32.dp).size(32.dp).align(Alignment.TopStart).graphicsLayer(translationX = ((-16).dp * (1f - dylinkVisible)).toPx(), translationY = ((-16).dp * (1f - dylinkVisible)).toPx(), alpha = dylinkVisible, rotationZ = -45f))
                    NotoImageEmoji(Emoji.PlayButton, Modifier.size(32.dp).align(Alignment.TopEnd).graphicsLayer(translationX = (32.dp * (1f - dylinkVisible)).toPx(), translationY = ((-32).dp * (1f - dylinkVisible)).toPx(), alpha = dylinkVisible).tint(Color.Cyan))
                    NotoImageEmoji(Emoji.Knot, Modifier.padding(32.dp).size(32.dp).align(Alignment.TopEnd).graphicsLayer(translationX = (16.dp * (1f - dylinkVisible)).toPx(), translationY = ((-16).dp * (1f - dylinkVisible)).toPx(), alpha = dylinkVisible, rotationZ = 45f))
                    NotoImageEmoji(Emoji.PlayButton, Modifier.size(32.dp).align(Alignment.BottomStart).graphicsLayer(translationX = ((-32).dp * (1f - dylinkVisible)).toPx(), translationY = (32.dp * (1f - dylinkVisible)).toPx(), alpha = dylinkVisible).tint(Color.Cyan))
                    NotoImageEmoji(Emoji.Knot, Modifier.padding(32.dp).size(32.dp).align(Alignment.BottomStart).graphicsLayer(translationX = ((-16).dp * (1f - dylinkVisible)).toPx(), translationY = (16.dp * (1f - dylinkVisible)).toPx(), alpha = dylinkVisible, rotationZ = 225f))
                    NotoImageEmoji(Emoji.PlayButton, Modifier.size(32.dp).align(Alignment.BottomEnd).graphicsLayer(translationX = (32.dp * (1f - dylinkVisible)).toPx(), translationY = (32.dp * (1f - dylinkVisible)).toPx(), alpha = dylinkVisible).tint(Color.Cyan))
                    NotoImageEmoji(Emoji.Knot, Modifier.padding(32.dp).size(32.dp).align(Alignment.BottomEnd).graphicsLayer(translationX = (16.dp * (1f - dylinkVisible)).toPx(), translationY = (16.dp * (1f - dylinkVisible)).toPx(), alpha = dylinkVisible, rotationZ = 135f))
                }
            }
        }
    }

    KodeinAnimatedVisibility(step >= 10) {
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column {
                Text("Static library", style = MaterialTheme.typography.h6, color = Color(KodeinColors.orange_light))
                Column(
                    Modifier
                        .width(176.dp)
                        .border(2.dp, Color(KodeinColors.orange_light), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    BulletPoints {
                        BulletPoint {
                            Text("Linked with app")
                        }
                        BulletPoint {
                            Text("Included in KLib")
                        }
                        BulletPoint {
                            Text("May conflict")
                        }
                    }
                }
            }
            Column {
                Text("Dynamic library", style = MaterialTheme.typography.h6, color = Color(KodeinColors.orange_light))
                Column(
                    Modifier
                        .width(176.dp)
                        .border(2.dp, Color(KodeinColors.orange_light), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    BulletPoints {
                        BulletPoint {
                            Text("Accessed at runtime")
                        }
                        BulletPoint {
                            Text("Provided by system")
                        }
                        BulletPoint {
                            Text("May be reused")
                        }
                    }
                }
            }
        }
    }
}

val cLibs = VerticalSlides(
    cLibsTitle,
    staticOrDynamic
)