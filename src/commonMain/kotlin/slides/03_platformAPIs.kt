package slides

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.kodein.cup.PreparedSlide
import net.kodein.cup.Slide
import net.kodein.cup.sa.rememberSourceCode
import net.kodein.cup.widgets.material.BulletPoints
import net.kodein.theme.KodeinColors
import net.kodein.theme.compose.Color
import net.kodein.theme.compose.m2.Link
import net.kodein.theme.cup.KodeinSourceCode
import net.kodein.theme.cup.ui.KodeinAnimatedVisibility
import net.kodein.theme.cup.ui.KodeinFadeAnimatedVisibility
import net.kodein.theme.cup.ui.defaultKodeinAnimationDuration
import org.kodein.emoji.Emoji
import org.kodein.emoji.compose.NotoAnimatedEmoji
import org.kodein.emoji.compose.m2.TextWithNotoAnimatedEmoji
import org.kodein.emoji.smileys_emotion.face_concerned.Cry
import org.kodein.emoji.smileys_emotion.face_negative.ImpSmile
import org.kodein.emoji.smileys_emotion.face_smiling.Laughing
import org.kodein.emoji.smileys_emotion.face_unwell.DizzyFace
import org.kodein.emoji.smileys_emotion.face_unwell.Vomit
import utils.VerticalSlides


val sha256commonAPI by PreparedSlide(
    stepCount = 7
) {
    val sourceCode = rememberSourceCode("kotlin") {
        val declaration by marker(highlighted(1))
        val content by marker(highlighted(2))
        val expect by marker(hidden(0..2))
        val factory by marker(highlighted(4))
        val utils by marker(hidden(0..4))
        val usage by marker(hidden(0..5))

        """
            public ${declaration}interface Digest : AutoCloseable${X} {
            ${content}    public val digestSize: Int
                public fun update(input: ByteArray, inputOffset: Int, len: Int)
                public fun finalize(output: ByteArray, outOffset: Int)
                public fun reset()${X}
            
            ${factory}    public interface Factory {
                    public fun create(): Digest
                }${X}
            }
            ${expect}
            public expect val Sha256: Digest.Factory${X}
            ${utils}
            public fun Digest.Factory.hash(input: ByteArray): ByteArray =
                create().use { d ->
                    d.update(input, 0, input.size)
                    ByteArray(d.digestSize).apply { d.finalize(this, 0) }
                }${X}
            ${usage}
            private val nameHash = Sha256.hash("Salomon BRYS")${X}
        """
    }

    slideContent { step ->
        Text("Sha256: Common Façade", style = MaterialTheme.typography.h2)
        Spacer(Modifier.height(8.dp))
        KodeinSourceCode(sourceCode, step, fontSize = 10.sp)
    }
}

val sha256jvm by PreparedSlide {
    val sourceCode = rememberSourceCode("kotlin") {
        """
            private class Sha256Jvm : Digest() {
                private val digest = MessageDigest.getInstance("SHA-256")
                override val digestSize get(): Int = digest.digestLength
                override fun update(input: ByteArray, inputOffset: Int, len: Int) {
                    digest.update(input, inputOffset, len)
                }
                override fun finalize(output: ByteArray, outputOffset: Int) {
                    digest.digest(output, outputOffset, output.size - outputOffset)
                }
                override fun reset() { digest.reset() }
                override fun close() {}

                object Factory : Digest.Factory {
                    override fun create(): Digest = Sha256Jvm()
                }
            }
        
            public actual val Sha256: Digest.Factory get() = Sha256Jvm.Factory
        """
    }

    slideContent {
        Text("Sha256: JVM API", style = MaterialTheme.typography.h2)
        Spacer(Modifier.height(8.dp))
        KodeinSourceCode(sourceCode, fontSize = 10.sp)
    }
}

val noJvmCommonization by Slide(
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
                text = "Commonization is not supported for JVM & Android.",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color(KodeinColors.purple_dark), RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
    }
    KodeinAnimatedVisibility(visible = step >= 1, durationMillis = 1_000) {
        Spacer(Modifier.height(8.dp))
        Link("https://youtrack.jetbrains.com/issue/KT-42466") {
            Text("KT-42466", fontSize = 24.sp)
        }
        KodeinAnimatedVisibility(visible = step >= 2) {
            Spacer(Modifier.height(4.dp))
            Text("(5 oct 2020)")
        }
    }
}

val sha256android by PreparedSlide(
    stepCount = 2
) {
    val sourceCode = rememberSourceCode("kotlin") {
        val jvm by marker(onlyShown(0))
        val android by marker(hidden(0))

        """
            private class Sha256${jvm}Jvm${X}${android}Android${X} : Digest() {
                private val digest = MessageDigest.getInstance("SHA-256")
                override val digestSize get(): Int = digest.digestLength
                override fun update(input: ByteArray, inputOffset: Int, len: Int) {
                    digest.update(input, inputOffset, len)
                }
                override fun finalize(output: ByteArray, outputOffset: Int) {
                    digest.digest(output, outputOffset, output.size - outputOffset)
                }
                override fun reset() { digest.reset() }
                override fun close() {}

                object Factory : Digest.Factory {
                    override fun create(): Digest = Sha256${jvm}Jvm${X}${android}Android${X}()
                }
            }
        
            public actual val Sha256: Digest.Factory get() = Sha256Jvm.Factory
        """
    }

    slideContent { step ->
        Text("Sha256: Android", style = MaterialTheme.typography.h2)
        Spacer(Modifier.height(8.dp))
        KodeinSourceCode(sourceCode, step, fontSize = 10.sp)
    }
}

val jvmArtifacts by Slide(
    stepCount = 4
) { step ->
    BulletPoints(
        spacedBy = 16.dp,
        animationDurationMillis = defaultKodeinAnimationDuration,
    ) {
        BulletPoint {
            TextWithNotoAnimatedEmoji("Duplicate all JVM & android code ${Emoji.Vomit}", fontSize = 20.sp, fixedEmojiSize = true)
        }
        BulletPoint(step >= 1) {
            Column {
                TextWithNotoAnimatedEmoji("Extract shared code in a JVM-only dependency ${Emoji.DizzyFace}", fontSize = 20.sp, fixedEmojiSize = true)
                KodeinAnimatedVisibility(step < 2) {
                    Text("...and require specific logic as function parameters:", Modifier.align(Alignment.Start))
                    KodeinSourceCode(rememberSourceCode("kotlin") {
                        """
                            // Needs Context on Android
                            suspend fun download(url: String, cacheDirPath: Path)

                            // Different dynamic libs
                            fun createCrypto(loadNativeLib: () -> Unit): Crypto

                            // Needs Context on Android
                            fun loadEmbeddedData(readResource: (String) -> InputStream)
                        """
                    })
                }
            }
        }
        BulletPoint(step >= 2) {
            Column(horizontalAlignment = Alignment.Start) {
                TextWithNotoAnimatedEmoji("Detect Android in the JVM code ${Emoji.Cry}", fontSize = 20.sp, fixedEmojiSize = true)
                KodeinAnimatedVisibility(step < 3) {
                    Text("...Only when you never need the context!", Modifier.align(Alignment.Start))
                    KodeinSourceCode(rememberSourceCode("kotlin") {
                        """
                            Class.forName("android.content.Context") != null
                        """
                    })
                }
            }
        }
    }
}

val sha256Apple by PreparedSlide(
    stepCount = 5
) {
    val sourceCode = rememberSourceCode("kotlin") {
        val constant by marker(highlighted(1))
        val memory by marker(highlighted(2))
        val function by marker(highlighted(3))
        val pinned by marker(highlighted(4))

        """
            private class Sha256Apple : Digest() {
                ${memory}val ctx: CC_SHA256_CTX = nativeHeap.alloc<CC_SHA256_CTX>()${X}
                    .also { ${function}CC_SHA256_Init(it.ptr)${X} }
                ${constant}override val digestSize get(): Int = CC_SHA256_DIGEST_LENGTH${X}
                override fun update(input: ByteArray, offset: Int, len: Int) {
            ${pinned}        input.usePinned {
                        ${function}CC_SHA256_Update(ctx.ptr, it.addressOf(offset), len.toUInt())${X}
                    }${X}
                }
                override fun finalize(output: ByteArray, offset: Int) {
            ${pinned}        output.asUByteArray().usePinned {
                        ${function}CC_SHA256_Final(it.addressOf(offset), ctx.ptr)${X}
                    }${X}
                }
                override fun reset() { CC_SHA256_Init(ctx.ptr) }
                ${memory}override fun close() { nativeHeap.free(ctx) }${X}
            }
        """
    }

    slideContent { step ->
        Text("Sha256: Kotlin/Native C API (Apple)", style = MaterialTheme.typography.h2)
        Spacer(Modifier.height(8.dp))
        KodeinSourceCode(sourceCode, step, fontSize = 10.sp)
    }
}

val sha256Mingw by PreparedSlide(
    stepCount = 6
) {
    val sourceCode = rememberSourceCode("kotlin") {
        val memory by marker(onlyShown(0))
        val init by marker(onlyShown(1))
        val size by marker(onlyShown(2..3))
        val scope by marker(highlighted(3))
        val inout by marker(onlyShown(4))
        val reset by marker(onlyShown(5))
        val above by marker(hidden(0))
        val below by marker(hidden(5))

        """
            private class Sha256Mingw : Digest() {
            ${memory}    val algorithm = nativeHeap.alloc<BCRYPT_ALG_HANDLEVar>()
                val hash = nativeHeap.alloc<BCRYPT_HASH_HANDLEVar>()
                override fun close() {
                    BCryptDestroyHash(hHash = hash.ptr)
                    nativeHeap.free(hash)
                    nativeHeap.free(algorithm)
                }
            ${X}${above}    //...
            ${X}${init}    private fun createHash() {
                    BCryptCreateHash(
                        hAlgorithm = algorithm.ptr,
                        phHash = hash.ptr,
                        pbHashObject = null, cbHashObject = 0u,
                        pbSecret = null, cbSecret = 0u, dwFlags = 0u
                    )
                }
                init {
                    BCryptOpenAlgorithmProvider(
                        phAlgorithm = algorithm.ptr,
                        pszAlgId = "SHA256",
                        pszImplementation = null, dwFlags = 0u
                    )
                    createHash()
                }
            ${X}${size}    override val digestSize: Int by lazy {
                    ${scope}memScoped {${X}
                        val length = ${scope}alloc<UCHARVar>()${X}
                        val resultLength = ${scope}alloc<ULONGVar>()${X}
                        BCryptGetProperty(
                            hObject = algorithm.ptr,
                            pszProperty = "HashDigestLength",
                            pbOutput = length.ptr,
                            cbOutput = sizeOf<UCHARVar>().toUInt(),
                            pcbResult = resultLength.ptr,
                            dwFlags = 0u
                        )
                        length.value.toInt()
                    ${scope}}${X}
                }
            ${X}${inout}    override fun update(input: ByteArray, inputOffset: Int, len: Int) {
                    input.usePinned { pinned ->
                        BCryptHashData(hHash = hash.ptr,
                            pbInput = pinned.addressOf(inputOffset).reinterpret(),
                            cbInput = len.toUInt(), dwFlags = 0u
                        )
                    }
                }
                override fun finalize(output: ByteArray, outputOffset: Int) {
                    output.usePinned { pinned ->
                        BCryptFinishHash(hHash = hash.ptr,
                            pbOutput = pinned.addressOf(outputOffset).reinterpret(),
                            cbOutput = (output.size - outputOffset).toUInt(),
                            dwFlags = 0u
                        )
                    }
                }
            ${X}${below}    //...
            ${X}${reset}    override fun doReset() {
                    BCryptDestroyHash(
                        hHash = hash.ptr
                    )
                    createHash()
                }
            ${X}}
        """
    }

    slideContent { step ->
        Text("Sha256: Kotlin/Native C API (Mingw)", style = MaterialTheme.typography.h2)
        Spacer(Modifier.height(8.dp))
        KodeinSourceCode(sourceCode, step, fontSize = 10.sp)
    }
}


val sha256platformAPIs = VerticalSlides(
    sha256commonAPI,
    sha256jvm,
    noJvmCommonization,
    sha256android,
    jvmArtifacts,
    sha256Apple,
    sha256Mingw
)
