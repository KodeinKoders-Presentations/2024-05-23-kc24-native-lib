package slides

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.kodein.cup.PreparedSlide
import net.kodein.cup.Slide
import net.kodein.cup.sa.rememberSourceCode
import net.kodein.cup.widgets.material.BulletPoints
import net.kodein.theme.KodeinColors
import net.kodein.theme.compose.Color
import net.kodein.theme.cup.KodeinSourceCode
import net.kodein.theme.cup.ui.KodeinAnimatedVisibility
import net.kodein.theme.cup.ui.KodeinFadeAnimatedVisibility
import org.kodein.emoji.Emoji
import org.kodein.emoji.compose.NotoAnimatedEmoji
import org.kodein.emoji.smileys_emotion.face_hand.Salute
import org.kodein.emoji.smileys_emotion.face_negative.ImpSmile
import org.kodein.emoji.smileys_emotion.face_smiling.Grinning
import org.kodein.emoji.smileys_emotion.face_smiling.Laughing
import utils.VerticalSlides


val dynamicOpenSSL by Slide {
    Text("Sha256: Kotlin/Native C API (Linux)", style = MaterialTheme.typography.h2)
    Text("with external OpenSSL Dynamic library", style = MaterialTheme.typography.h4)
}

@OptIn(ExperimentalAnimationApi::class)
val consistentLinuxABI by Slide(
    stepCount = 4
) { step ->
    Box(contentAlignment = Alignment.Center) {
        androidx.compose.animation.AnimatedVisibility(
            visible = step in 0..1,
            enter = fadeIn(tween(1_000)),
            exit = fadeOut(tween(1_000)),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .graphicsLayer(
                        translationX = with (LocalDensity.current) {
                            transition.animateDp({ tween(1_000) }) {
                                if (it == EnterExitState.Visible) 0.dp else (-128).dp
                            }.value.toPx()
                        }
                    )
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
                        text = "The linux ecosystem is of course known for its library ABI consistency across distributions...\n...or not!",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color(KodeinColors.purple_dark), RoundedCornerShape(8.dp))
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }
            }
        }
        androidx.compose.animation.AnimatedVisibility(
            visible = step >= 2,
            enter = fadeIn(tween(1_000)),
            exit = fadeOut(tween(1_000)),
        ) {
            Column(
                modifier = Modifier
                    .graphicsLayer(
                        translationX = with (LocalDensity.current) {
                            transition.animateDp({ tween(1_000) }) {
                                if (it == EnterExitState.Visible) 0.dp else (-128).dp
                            }.value.toPx()
                        }
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BulletPoints(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .padding(end = 16.dp)
                            .background(Color(KodeinColors.orange_dark), RoundedCornerShape(8.dp))
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        BulletPoint {
                            Text("I trust Linux distribs provide OpenSSL.")
                        }
                        BulletPoint {
                            Text("I trust OpenSSL backward ABI compat.")
                        }
                        BulletPoint {
                            Text("I will only use OpenSSL 1.1.0 APIs.")
                        }
                        BulletPoint {
                            Text("I trust anti-corruption system policies.")
                        }
                    }
                    NotoAnimatedEmoji(Emoji.Salute, iterations = 2, stopAt = 0.76f, modifier = Modifier.size(96.dp))
                }

                KodeinAnimatedVisibility(step >= 3) {
                    Spacer(Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        NotoAnimatedEmoji(Emoji.Grinning, iterations = 1, stopAt = 0.7f, modifier = Modifier.size(96.dp))
                        Text(
                            text = "I'll use a Docker container!",
                            modifier = Modifier
                                .padding(8.dp)
                                .padding(start = 16.dp)
                                .background(Color(KodeinColors.orange_dark), RoundedCornerShape(8.dp))
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

val openSSLDef by PreparedSlide(
    stepCount = 5
) {

    val sourceCode = rememberSourceCode("properties") {
        val pkg by marker(highlighted(1))
        val hdr by marker(highlighted(2))
        val cmp by marker(highlighted(3))
        val lnk by marker(highlighted(4))

        """
            ${pkg}package = native.openssl.evp${X}
            
            ${hdr}headers = openssl/evp.h
            headerFilter = openssl/**${X}
            
            ${cmp}compilerOpts.linux_x64 = -I/usr/include \
                -I/usr/include/x86_64-linux-gnu${X}
            
            ${lnk}linkerOpts.linux_x64 = -lcrypto${X}
        """
    }

    slideContent { step ->
        Text("Kotlin/Native dynamic C lib", style = MaterialTheme.typography.h2)
        Text("Step 1: write a def file", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinSourceCode(sourceCode, step, file = "src/nativeInterop/cinterop/openssl.def")
    }
}

val openSSLGradle by PreparedSlide {

    val sourceCode = rememberSourceCode("kotlin") {
        """
            kotlin {
                linuxX64 {
                    compilations.named("main") {
                        cinterops.register("openssl")
                    }
                }
            }
        """
    }

    slideContent {
        Text("Kotlin/Native dynamic C lib", style = MaterialTheme.typography.h2)
        Text("Step 2: configure generation", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinSourceCode(sourceCode, file = "build.gradle.kts")
    }
}

val openSSLKotlin by PreparedSlide {

    val sourceCode = rememberSourceCode("kotlin") {
        """
            private class Sha256OpenSSL : Digest {
                val ctx = EVP_MD_CTX_new()
                init { EVP_DigestInit(ctx, EVP_sha256()) }
                override val digestSize: Int get() = EVP_MD_get_size(EVP_sha256())
                override fun update(input: ByteArray, inputOffset: Int, len: Int) {
                    input.usePinned { pinned ->
                        EVP_DigestUpdate(ctx, pinned.addressOf(inputOffset), len.convert())
                    }
                }
                override fun finalize(output: ByteArray, outputOffset: Int) {
                    output.usePinned { pinned ->
                        EVP_DigestFinal(
                            ctx, pinned.addressOf(outputOffset).reinterpret(), null
                        )
                    }
                }
                override fun reset() { EVP_MD_CTX_reset(ctx) }
                override fun close() { EVP_MD_CTX_free(ctx) }
            }
        """
    }

    slideContent { step ->
        Text("Kotlin/Native dynamic C lib", style = MaterialTheme.typography.h2)
        Text("Step 3: write Kotlin!", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinSourceCode(sourceCode, step, fontSize = 10.sp)
    }
}
val dynamicCLib = VerticalSlides(
    dynamicOpenSSL,
    consistentLinuxABI,
    openSSLDef,
    openSSLGradle,
    openSSLKotlin
)