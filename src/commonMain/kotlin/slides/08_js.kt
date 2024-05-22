package slides

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.kodein.cup.PreparedSlide
import net.kodein.cup.Slide
import net.kodein.cup.sa.rememberSourceCode
import net.kodein.theme.KodeinColors
import net.kodein.theme.compose.Color
import net.kodein.theme.compose.m2.Link
import net.kodein.theme.cup.KodeinSourceCode
import net.kodein.theme.cup.ui.KodeinAnimatedVisibility
import net.kodein.theme.cup.ui.KodeinFadeAnimatedVisibility
import net.kodein.theme.cup.ui.defaultKodeinAnimationDuration
import org.kodein.emoji.Emoji
import org.kodein.emoji.compose.NotoAnimatedEmoji
import org.kodein.emoji.smileys_emotion.face_hand.ShushingFace
import org.kodein.emoji.smileys_emotion.face_negative.ImpSmile
import org.kodein.emoji.smileys_emotion.face_smiling.Laughing
import utils.VerticalSlides


val jsTitle by Slide(

) {
    Text(
        text = "What about JS?!?",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h2
    )
}

val jsSha1 by PreparedSlide(
    stepCount = 6
) {
    val sourceCodeGradle = rememberSourceCode("kotlin") {
        """
            kotlin {
                sourceSets {
                    commonMain.dependencies {
                        implementation(npm("js-sha256", "~0.11"))
                    }
                }
            }
        """
    }

    val sourceCodeJsModule = rememberSourceCode("kotlin") {
        """
            external interface JsSha256Module : JsAny {
                val sha256: JsDigestFactory
            }
            
            @JsModule("js-sha256")
            external val jsSha256Module: JsSha256Module
            
            external interface JsDigestFactory: JsAny {
                fun create(): JsDigest
            }
            
            external interface JsDigest : JsAny {
                fun update(bytes: Uint8Array)
                fun arrayBuffer(): ArrayBuffer
            }
        """
    }

    val sourceCodeJsUtilities = rememberSourceCode("kotlin") {
        """
            private fun ByteArray.toUint8Array(offset: Int, len: Int): Uint8Array =
                Uint8Array(len).also { array ->
                    repeat(len) { index ->
                        array.set(index, get(offset + index))
                    }
                }
            
            fun Uint8Array.copyInto(output: ByteArray, outputOffset: Int) {
                repeat(length) { index ->
                    output[outputOffset + index] = get(index)
                }
            }
        """
    }

    val sourceCodeKotlin = rememberSourceCode("kotlin") {
        """
            private class Sha256Js : Digest {
                private var digest = jsSha256Module.sha256.create()
            
                override val digestSize: Int get() = 32

                override fun update(input: ByteArray, inputOffset: Int, len: Int) {
                    digest.update(input.toUint8Array(inputOffset, len))
                }
            
                override fun finalize(output: ByteArray, outputOffset: Int) {
                    val arrayBuffer = digest.arrayBuffer()
                    Uint8Array(arrayBuffer).copyInto(output, outputOffset)
                }
            
                override fun reset() {
                    digest = jsSha256Module.sha256.create()
                }
            
                override fun close() {}
            }
        """
    }

    slideContent { step ->
        Text("Sha1: NPM JS library", style = MaterialTheme.typography.h2)

        Spacer(Modifier.height(16.dp))

        Box(
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(animateFloatAsState(if (step == 3) 0.2f else 1f, tween(defaultKodeinAnimationDuration)).value)
            ) {
                KodeinAnimatedVisibility(step == 0) {
                    KodeinSourceCode(sourceCodeGradle, file = "build.gradle.kts", fontSize = 10.sp)
                }
                KodeinAnimatedVisibility(step == 1) {
                    KodeinSourceCode(sourceCodeJsModule, fontSize = 10.sp)
                }
                KodeinAnimatedVisibility(step in 2..4) {
                    KodeinSourceCode(sourceCodeJsUtilities, fontSize = 10.sp)
                }
                KodeinAnimatedVisibility(step == 5) {
                    KodeinSourceCode(sourceCodeKotlin, fontSize = 10.sp)
                }
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = step == 3,
                enter = fadeIn(tween(defaultKodeinAnimationDuration)) + slideInHorizontally(tween(defaultKodeinAnimationDuration)) { it / 2 },
                exit = fadeOut(tween(defaultKodeinAnimationDuration)) + slideOutHorizontally(tween(defaultKodeinAnimationDuration))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NotoAnimatedEmoji(Emoji.ImpSmile, iterations = 1, stopAt = 0.76f, modifier = Modifier.size(96.dp))
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .padding(start = 16.dp)
                            .background(Color(KodeinColors.purple_dark), RoundedCornerShape(8.dp))
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        Link("https://youtrack.jetbrains.com/issue/KT-30098") {
                            Text("KT-30098", fontSize = 24.sp)
                        }
                        Text(
                            text = """
                            
                            Created more than five years ago...
                            ...
                            Just sayin'!
                        """.trimIndent(),
                        )
                    }
                }
            }
        }
    }
}

val jsSecp256k1Title by Slide {
    Text("JS Secp256k1: external C library", style = MaterialTheme.typography.h2)

    Spacer(Modifier.height(16.dp))

    Text("Import a C API into Kotlin/Wasm", style = MaterialTheme.typography.h4)
}

val noWasmCInterop by Slide(
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
                text = "There is no C-interop for Kotlin/Wasm (and no way to link with an external library)!",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color(KodeinColors.purple_dark), RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
    }
    KodeinAnimatedVisibility(visible = step >= 2, durationMillis = 1_000) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Kotlin/Wasm is in ALPHA!",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color(KodeinColors.orange_dark), RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )
            NotoAnimatedEmoji(Emoji.ShushingFace, iterations = 1, stopAt = 0.74f, modifier = Modifier.size(96.dp))
        }
    }
    KodeinAnimatedVisibility(visible = step >= 1, durationMillis = 1_000) {
        Spacer(Modifier.height(8.dp))
        Link("https://youtrack.jetbrains.com/issue/KT-56223") {
            Text("KT-56223", fontSize = 24.sp)
        }
    }
}

val jsSecp256k1CFacade by PreparedSlide(
    stepCount = 2
) {
    val sourceCode = rememberSourceCode("c") {
        val em by marker(highlighted(1))

        """
            ${em}EMSCRIPTEN_KEEPALIVE${X}
            void wasm_secp256k1_createPubKey(
                const unsigned char *secKey,
                unsigned char *output
            ) {
                secp256k1_context *ctx = secp256k1_context_create(SECP256K1_CONTEXT_SIGN);
                
                secp256k1_pubkey pubKey;
                secp256k1_ec_pubkey_create(ctx, &pubKey, secKey);
                
                size_t len = 65;
                secp256k1_ec_pubkey_serialize(
                    ctx, output, &len, &pubKey, SECP256K1_EC_UNCOMPRESSED
                );
            
                secp256k1_context_destroy(ctx);
            }
        """
    }
    slideContent { step ->
        Text("Wasm external C lib", style = MaterialTheme.typography.h2)
        Text("Step 1: write a C façade", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinSourceCode(sourceCode, step, fontSize = 9.sp)
    }
}

val jsSecp256k1Compile by PreparedSlide(
    stepCount = 7
) {
    val sourceCodeShell = rememberSourceCode("bash") {
        val em by marker(highlighted(1))
        val src by marker(highlighted(2))
        val out by marker(highlighted(3))
        val cnf by marker(highlighted(4))
        """
            ${em}emcc \${X}
            ${src}    ../../src/secp256k1.c \
                ../../src/precomputed_ecmult.c \
                ../../src/precomputed_ecmult_gen.c \
                ../secp256k1_wasm.c \${X}
                -I../../include/ \
            ${out}    -o secp256k1.js \
                --emit-tsd secp256k1.d.ts \${X}
            ${cnf}    -sMODULARIZE -s ENVIRONMENT='web' \
                -sEXPORTED_FUNCTIONS=_malloc,_free${X}
        """
    }
    val sourceCodeTs = rememberSourceCode("typescript") {
        val exp by marker(highlighted(1))
        """
            declare namespace RuntimeExports {
                let HEAPF32: any;
                let HEAPF64: any;
                let HEAP_DATA_VIEW: any;
                let HEAP8: any;
                let HEAPU8: any;
                let HEAP16: any;
                let HEAPU16: any;
                let HEAP32: any;
                let HEAPU32: any;
                let HEAP64: any;
                let HEAPU64: any;
            }
            interface WasmModule {
            ${exp}  _free(_0: number): void;
              _malloc(_0: number): number;
              _wasm_secp256k1_createPubKey(_0: number, _1: number): void;${X}
            }
            export type MainModule = WasmModule & typeof RuntimeExports;
            export default function MainModuleFactory (options?: unknown): Promise<MainModule>;
        """
    }
    slideContent { step ->
        Text("Wasm external C lib", style = MaterialTheme.typography.h2)
        Text("Step 2: compile the C façade", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinAnimatedVisibility(step in 0..4) {
            KodeinSourceCode(sourceCodeShell, step)
        }
        KodeinAnimatedVisibility(step >= 5) {
            KodeinSourceCode(sourceCodeTs, step - 5, fontSize = 9.sp)
        }
    }
}

val jsSecp256k1Kotlin by PreparedSlide(
    stepCount = 6
) {
    val sourceCodeJsModule = rememberSourceCode("kotlin") {
        """
            private typealias JsPointer = JsNumber
            
            private external interface WasmModule : JsAny {
                val HEAPU8: Uint8Array
            
                fun _malloc(size: JsNumber): JsPointer
                fun _free(size: JsPointer)
            
                fun _wasm_secp256k1_createPubKey(secKey: JsPointer, output: JsPointer)
            }
            
            @JsModule("./secp256k1.js")
            private external val Secp256k1Loader: () -> Promise<WasmModule>
        """
    }

    val sourceCodeJsUtilities = rememberSourceCode("kotlin") {
        """
            fun Uint8Array.read(offset: Int, size: Int): ByteArray =
                ByteArray(size).also {
                    repeat(size) { index ->
                        it[index] = get(offset + index)
                    }
                }
            
            fun Uint8Array.write(offset: Int, bytes: ByteArray) {
                repeat(bytes.size) { index ->
                    set(offset + index, bytes[index])
                }
            }
        """
    }

    val sourceCodeKotlin = rememberSourceCode("kotlin") {
        val alloc1 by marker(highlighted(1))
        val call by marker(highlighted(2))
        val free by marker(highlighted(3))

        """
            override fun createPubKey(secKey: ByteArray): ByteArray {
            ${alloc1}    val secKeyPtr = module._malloc(32.toJsNumber())
                module.HEAPU8.write(secKeyPtr.toInt(), secKey)${X}
            
            ${call}    val outputPtr = module._malloc(65.toJsNumber())
                module._wasm_secp256k1_createPubKey(secKeyPtr, outputPtr)
                val pubKey = module.HEAPU8.read(outputPtr.toInt(), 65)${X}
            
            ${free}    module._free(secKeyPtr)
                module._free(outputPtr)${X}
            
                return pubKey
            }
        """
    }

    slideContent { step ->
        Text("Wasm external C lib", style = MaterialTheme.typography.h2)
        Text("Step 3: write Kotlin!", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        Box(
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                KodeinAnimatedVisibility(step == 0) {
                    KodeinSourceCode(sourceCodeJsModule, fontSize = 10.sp)
                }
                KodeinAnimatedVisibility(step == 1) {
                    KodeinSourceCode(sourceCodeJsUtilities, fontSize = 10.sp)
                }
                KodeinAnimatedVisibility(step >= 2) {
                    KodeinSourceCode(sourceCodeKotlin, step - 2, fontSize = 10.sp)
                }
            }

        }
    }
}

val js = VerticalSlides(
    jsTitle,
    jsSha1,
    jsSecp256k1Title,
    noWasmCInterop,
    jsSecp256k1CFacade,
    jsSecp256k1Compile,
    jsSecp256k1Kotlin
)