package slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import anims.AbsoluteTransitionBuilder
import anims.AbsoluteTransitionsBox
import net.kodein.cup.SLIDE_SIZE_16_9
import net.kodein.cup.Slide
import net.kodein.theme.compose.JetBrainsMono
import net.kodein.theme.cup.ui.KodeinAnimatedVisibility
import org.kodein.emoji.compose.m2.TextWithPlatformEmoji


@Composable
private fun TableRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) = Row(
    verticalAlignment = Alignment.Bottom,
    modifier = modifier.fillMaxWidth(.9f),
    content = content
)

@Composable
private fun RowScope.LeftCell(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        textAlign = TextAlign.End,
        modifier = modifier.weight(1.5f).padding(end = 16.dp)
    )
}

@Composable
private fun RowScope.Cell(
    text: String,
    modifier: Modifier = Modifier,
) {
    TextWithPlatformEmoji(
        text = text,
        textAlign = TextAlign.Center,
        modifier = modifier.weight(1f)
    )
}

val stepHighlights = mapOf(
    4 to listOf(
        listOf("sha256", "chachapoly", "jvm*"),
    ),
    5 to listOf(
        listOf("sha256", "apple", "mingw"),
    ),
    6 to listOf(
        listOf("sha256", "js"),
    ),
    7 to listOf(
        listOf("secp256k1", "jvm*"),
    ),
    8 to listOf(
        listOf("sha256", "linux"),
        listOf("secp256k1", "apple", "mingw", "linux"),
        listOf("chachapoly", "linux"),
    ),
    9 to listOf(
        listOf("secp256k1", "js"),
    ),
    10 to listOf(
        listOf("chachapoly", "apple", "mingw"),
    ),
    11 to listOf(
        listOf("chachapoly", "js"),
    ),
)

@Composable
private fun Modifier.platformAlpha(id: String, highlights: List<List<String>>?): Modifier {
    val alpha by animateFloatAsState(
        when {
            highlights == null -> 1f
            highlights.any { id in it } -> 1f
            else -> 0.3f
        },
        tween(600)
    )
    return alpha(alpha)
}

@Composable
private fun Modifier.targetAlpha(platforms: Pair<String, String>, highlights: List<List<String>>?): Modifier {
    val alpha by animateFloatAsState(
        when {
            highlights == null -> 1f
            highlights.any { platforms.first in it && platforms.second in it } -> 1f
            else -> 0.3f
        },
        tween(600)
    )
    val scale by animateFloatAsState(
        when {
            highlights == null -> 1f
            highlights.any { platforms.first in it && platforms.second in it } -> 1.4f
            else -> 1f
        },
        tween(600)
    )
    return alpha(alpha).scale(scale)
}

@Composable
fun AbsoluteTransitionBuilder.ContentTypeOfAPIs(step: Int) {
    val isCurrent = step == 0 || step >= 3
    val alpha by animateFloatAsState(if (isCurrent) 1f else 0f, tween(1_000))
    Content(
        isCurrent = isCurrent,
        modifier = Modifier.alpha(alpha)
    ) { blocks ->
        val highlights = stepHighlights[step]

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TableRow {
                LeftCell("")
                blocks.Block("jvm*", Modifier.weight(1f))
                blocks.Block("apple", Modifier.weight(1f))
                blocks.Block("mingw", Modifier.weight(1f))
                blocks.Block("linux", Modifier.weight(1f))
                blocks.Block("js", Modifier.weight(1f))
            }
            TableRow {
                LeftCell("SHA256", Modifier.platformAlpha("sha256", highlights))
                Cell("P(Java)", Modifier.targetAlpha("sha256" to "jvm*", highlights))
                Cell("P(C)", Modifier.targetAlpha("sha256" to "apple", highlights))
                Cell("P(C)", Modifier.targetAlpha("sha256" to "mingw", highlights))
                Cell("L(C)", Modifier.targetAlpha("sha256" to "linux", highlights))
                Cell("L(JS)", Modifier.targetAlpha("sha256" to "js", highlights))
            }
            TableRow {
                LeftCell("Secp256k1", Modifier.platformAlpha("secp256k1", highlights))
                Cell("L(C)", Modifier.targetAlpha("secp256k1" to "jvm*", highlights))
                Cell("L(C)", Modifier.targetAlpha("secp256k1" to "apple", highlights))
                Cell("L(C)", Modifier.targetAlpha("secp256k1" to "mingw", highlights))
                Cell("L(C)", Modifier.targetAlpha("secp256k1" to "linux", highlights))
                Cell("L(C)", Modifier.targetAlpha("secp256k1" to "js", highlights))
            }
            TableRow {
                LeftCell("Chacha-Poly", Modifier.platformAlpha("chachapoly", highlights))
                Cell("P(Java)", Modifier.targetAlpha("chachapoly" to "jvm*", highlights))
                Cell("P(Swift)", Modifier.targetAlpha("chachapoly" to "apple", highlights))
                Cell("P(C#)", Modifier.targetAlpha("chachapoly" to "mingw", highlights))
                Cell("L(C)", Modifier.targetAlpha("chachapoly" to "linux", highlights))
                Cell("L(??)", Modifier.targetAlpha("chachapoly" to "js", highlights))
            }

            KodeinAnimatedVisibility(step in 4..11) {
                Spacer(Modifier.height(32.dp))

                KodeinAnimatedVisibility(step == 4) {
                    Text("Java Platform API", style = MaterialTheme.typography.h2)
                }
                KodeinAnimatedVisibility(step == 5) {
                    Text("C Platform API", style = MaterialTheme.typography.h2)
                }
                KodeinAnimatedVisibility(step == 6) {
                    Text("JS External Library", style = MaterialTheme.typography.h2)
                }
                KodeinAnimatedVisibility(step in 7..9) {
                    Text("C External Library", style = MaterialTheme.typography.h2)
                    KodeinAnimatedVisibility(step == 7) {
                        Text("With a JNI manual façade", style = MaterialTheme.typography.h4)
                    }
                    KodeinAnimatedVisibility(step == 8) {
                        Text("With a Kotlin Native generated façade", style = MaterialTheme.typography.h4)
                    }
                    KodeinAnimatedVisibility(step == 9) {
                        Text("With a JS manual façade", style = MaterialTheme.typography.h4)
                    }
                }
                KodeinAnimatedVisibility(step == 10) {
                    Text("Modern Platform API", style = MaterialTheme.typography.h2)
                    Text("With a C manual façade", style = MaterialTheme.typography.h4)
                }
                KodeinAnimatedVisibility(step == 11) {
                    Text("???", style = MaterialTheme.typography.h2)
                }
            }
        }
    }
}

@Composable
fun AbsoluteTransitionBuilder.ContentListOfTarget(step: Int) {
    val isCurrent = step in 1..2
    val alpha = remember { Animatable(if (isCurrent) 1f else 0f) }
    var contentVisible by remember { mutableStateOf(isCurrent) }

    LaunchedEffect(isCurrent) {
        if (isCurrent) {
            alpha.animateTo(if (isCurrent) 1f else 0f, tween(1_000))
            contentVisible = true
        } else {
            alpha.animateTo(if (isCurrent) 1f else 0f, tween(1_000))
            contentVisible = false
        }
    }

    Content(
        isCurrent = isCurrent,
        modifier = Modifier.alpha(alpha.value)
    ) { blocks ->
        Row {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.width(136.dp)
            ) {
                blocks.Block("jvm*")
                blocks.Block("apple")
                blocks.Block("mingw")
                blocks.Block("linux")
                blocks.Block("js")
                KodeinAnimatedVisibility(step >= 2) {
                    Text("AndroidNative", fontSize = 20.sp, lineHeight = 40.sp)
                }
            }
            Column(
                modifier = Modifier.padding(start = 2.dp)
            ) {
                Text(": ", lineHeight = 40.sp)
                Text(": ", lineHeight = 40.sp)
                Text(": ", lineHeight = 40.sp)
                Text(": ", lineHeight = 40.sp)
                Text(": ", lineHeight = 40.sp)
                KodeinAnimatedVisibility(step >= 2) {
                    Text(": ", lineHeight = 40.sp)
                }
            }
            KodeinAnimatedVisibility(
                visible = contentVisible,
            ) {
                Column(
                    modifier = Modifier.padding(start = 2.dp)
                ) {
                    Text("jvm + androidTarget", fontFamily = JetBrainsMono, lineHeight = 40.sp)
                    Text("ios* + watchos* + tvos* + macos*", fontFamily = JetBrainsMono, lineHeight = 40.sp)
                    Text("mingwX64", fontFamily = JetBrainsMono, lineHeight = 40.sp)
                    Row {
                        Text("linuxX64", fontFamily = JetBrainsMono, lineHeight = 40.sp)
                        KodeinAnimatedVisibility(step >= 2) {
                            Text(" + ", fontFamily = JetBrainsMono, lineHeight = 40.sp)
                            Text("linuxArm64", fontFamily = JetBrainsMono, lineHeight = 40.sp, textDecoration = TextDecoration.LineThrough)
                        }
                    }
                    Text("js(IR) + wasmJs + wasmWasi", fontFamily = JetBrainsMono, lineHeight = 40.sp)
                    KodeinAnimatedVisibility(step >= 2) {
                        Text("androidNative*", lineHeight = 40.sp, fontFamily = JetBrainsMono, textDecoration = TextDecoration.LineThrough)
                    }
                }
            }
        }
    }
}

val platforms by Slide(
    specs = { copy(size = SLIDE_SIZE_16_9) },
    stepCount = 13
) { step ->
//    val density = LocalDensity.current
    AbsoluteTransitionsBox(Modifier.fillMaxSize()) {
        val highlights = stepHighlights[step]
        block("jvm*") { Text("JVM*", fontSize = 20.sp, lineHeight = 40.sp, modifier = Modifier.align(Alignment.Center).platformAlpha("jvm*", highlights)) }
        block("apple") { Text("Apple", fontSize = 20.sp, lineHeight = 40.sp, modifier = Modifier.align(Alignment.Center).platformAlpha("apple", highlights)) }
        block("mingw") { Text("MinGW", fontSize = 20.sp, lineHeight = 40.sp, modifier = Modifier.align(Alignment.Center).platformAlpha("mingw", highlights)) }
        block("linux") { Text("Linux", fontSize = 20.sp, lineHeight = 40.sp, modifier = Modifier.align(Alignment.Center).platformAlpha("linux", highlights)) }
        block("js") { Text("Js, Wasm", fontSize = 20.sp, lineHeight = 40.sp, modifier = Modifier.align(Alignment.Center).platformAlpha("js", highlights)) }

        ContentTypeOfAPIs(step)
        ContentListOfTarget(step)

    }
}
