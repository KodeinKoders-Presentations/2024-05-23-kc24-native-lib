package slides

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.qrose.options.*
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import net.kodein.cup.PreparedSlide
import net.kodein.cup.SLIDE_SIZE_16_9
import net.kodein.cup.Slide
import net.kodein.cup.sa.rememberSourceCode
import net.kodein.theme.KodeinColors
import net.kodein.theme.compose.Color
import net.kodein.theme.compose.m2.Link
import net.kodein.theme.cup.KodeinSourceCode
import net.kodein.theme.cup.ui.KodeinAnimatedVisibility
import org.kodein.emoji.Emoji
import org.kodein.emoji.compose.m2.TextWithNotoImageEmoji
import org.kodein.emoji.symbols.av_symbol.ArrowUp
import utils.VerticalSlides


val chachapolyTitle by Slide(
    specs = { copy(size = SLIDE_SIZE_16_9) },
    stepCount = 2
) { step ->
    Text("Chacha-Poly: Modern platform API (Apple)", style = MaterialTheme.typography.h2)

    Spacer(Modifier.height(16.dp))

    Text("Import a Swift API into Kotlin/Native", style = MaterialTheme.typography.h4)

    KodeinAnimatedVisibility(step >= 1) {
        Spacer(Modifier.height(16.dp))

        Text("Swift → Obj-C → Kotlin", style = MaterialTheme.typography.h4)
    }
}

val chachapolyAppleSwift by PreparedSlide(
    stepCount = 3
) {
    val sourceCode = rememberSourceCode("swift") {
        val objc by marker(highlighted(1..2))
        val res by marker(highlighted(2))

        """
            ${objc}@objc${X} public class func encrypt(
                key: Data, nonce: Data, aad: Data?, plaintext: Data
            ) ${res}-> DataResult${X} {
                DataResult {
                    let symKey = SymmetricKey(data: key)
                    let chaNonce: ChaChaPoly.Nonce = try ChaChaPoly.Nonce(data: nonce)
                    let sealedBox: ChaChaPoly.SealedBox
                    if let aad = aad {
                        sealedBox = try ChaChaPoly.seal(
                            plaintext, using: symKey, nonce: chaNonce, authenticating: aad
                        )
                    } else {
                        sealedBox = try ChaChaPoly.seal(plaintext, using: symKey, nonce: chaNonce)
                    }
                    return sealedBox.ciphertext + sealedBox.tag
                }
            }
        """
    }

    slideContent { step ->
        Text("Apple Swift API", style = MaterialTheme.typography.h2)
        Text("Step 1: write static swift façade...", style = MaterialTheme.typography.h4)
        Text("...with an Obj-C bridging header", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinSourceCode(sourceCode, step, fontSize = 8.sp)
    }
}

val chachapolyAppleDef by PreparedSlide(
    stepCount = 3
) {

    val sourceCode = rememberSourceCode("properties") {
        val lang by marker(highlighted(1))
        val opts by marker(highlighted(2))

        """
            package = native.chachapoly
            
            ${lang}language = Objective-C${X}

            headers = SwiftChachaPoly/SwiftChachaPoly-Swift.h
            headerFilter = SwiftChachaPoly/*
            
            staticLibraries = libSwiftChachaPoly.a
            libraryPaths.ios_x64 = SwiftChachaPoly/build/Release-iphonesimulator
            libraryPaths.ios_arm64 = SwiftChachaPoly/build/Release-iphoneos
            
            ${opts}linkerOpts = -L/usr/lib/swift
            linkerOpts.ios_x64 = -ios_simulator_version_min 13.0.0 \
                -L[xctoolchain]/usr/lib/swift/iphonesimulator/
            linkerOpts.ios_arm64 = -iphoneos_version_min 13.0.0 \
                -L[xctoolchain]/usr/lib/swift/iphoneos/${X}
        """
    }

    slideContent { step ->
        Text("Apple Swift API", style = MaterialTheme.typography.h2)
        Text("Step 2: write a def file", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinSourceCode(sourceCode, step, file = "src/nativeInterop/cinterop/chachapoly-swift.def", fontSize = 10.sp)
    }
}

val chachapolyAppleKotlin by PreparedSlide(
    stepCount = 4
) {

    val sourceCode = rememberSourceCode("kotlin") {
        val arp by marker(highlighted(1))
        val encrypt by marker(onlyShown(0..1))
        val data by marker(hidden(0..1))
        val create by marker(highlighted(3))

        """
            ${encrypt}override fun encrypt(
                key: ByteArray, nonce: ByteArray, aad: ByteArray, plainText: ByteArray
            ): ByteArray {
                require(key.size == 32) { "Key must be 32 bytes" }
                require(nonce.size == 12) { "Nonce must be 12 bytes" }
                ${arp}autoreleasepool {${X}
                    return SwiftChachaPoly.encryptWithKey(
                        key.toData(),
                        nonce.toData(),
                        aad.toData(),
                        plainText.toData()
                    ).unwrap().toByteArray()
                ${arp}}${X}
            }${X}
            ${data}private inline fun ByteArray.toData(): NSData {
                if (isEmpty()) return NSData()
            ${create}    val pinned = pin()
                return NSData.create(
                    bytesNoCopy = pinned.addressOf(0),
                    length = size.toULong(),
                    deallocator = { _, _ -> pinned.unpin() }
                )${X}
            }
            private fun NSData.toByteArray(): ByteArray {
                val size = length.toInt()
                val bytes = ByteArray(size)
                if (size > 0) {
                    bytes.usePinned { pinned ->
                        memcpy(pinned.addressOf(0), this.bytes, this.length)
                    }
                }
                return bytes
            }${X}
        """
    }

    slideContent { step ->
        Text("Apple Swift API", style = MaterialTheme.typography.h2)
        Text("Step 3: write Kotlin!", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinSourceCode(sourceCode, step, fontSize = 10.sp)
    }
}

val SwiftInKotlinLink by Slide(
    specs = { copy(size = SLIDE_SIZE_16_9) },
) { step ->
    Text("Create a Kotlin/Multiplatform library with Swift", style = MaterialTheme.typography.h2)

    Spacer(Modifier.height(16.dp))

    Link(uri = "https://blog.kodein.net/swift-in-kotlin") {
        Text(
            text = "blog.kodein.net/swift-in-kotlin",
            fontSize = 1.2.em
        )
    }

    Spacer(Modifier.height(16.dp))

    Image(
        painter = rememberQrCodePainter(
            "https://blog.kodein.net/swift-in-kotlin",
            shapes = QrShapes(
                ball = QrBallShape.roundCorners(.25f),
                frame = QrFrameShape.roundCorners(.25f),
                darkPixel = QrPixelShape.circle(),
            ),
            colors = QrColors(
                dark = QrBrush.solid(Color(KodeinColors.light_orange))
            )
        ),
        contentDescription = "This presentation",
        modifier = Modifier.padding(2.dp).size(128.dp)
    )
}

val modernLanguageBridge by Slide(
    stepCount = 3
) { step ->
    ProvideTextStyle(MaterialTheme.typography.h2) {
        Row(
            horizontalArrangement = Arrangement.Center,
        ) {
            Text("Modern ")
            Row {
                KodeinAnimatedVisibility(step == 0) {
                    Text("Swift")
                }
                KodeinAnimatedVisibility(step == 1) {
                    Text("Rust")
                }
                KodeinAnimatedVisibility(step == 2) {
                    Text("C# / CLR")
                }
            }
            Text(" platform API")
        }
    }

    Spacer(Modifier.height(16.dp))

    ProvideTextStyle(TextStyle(fontSize = 1.2.em)) {
        KodeinAnimatedVisibility(step >= 2) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .padding(4.dp)
                    .border(1.dp, Color(KodeinColors.orange), RoundedCornerShape(4.dp))
                    .padding(4.dp)
            ) {
                Text(
                    text = "CLR API",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(.8f)
                        .padding(4.dp)
                        .border(1.dp, Color(KodeinColors.orange), RoundedCornerShape(4.dp))
                        .background(Color(KodeinColors.dark_orange), RoundedCornerShape(4.dp))
                        .padding(2.dp)
                )
                Text("Dynamic C# façade")
            }

            TextWithNotoImageEmoji("${Emoji.ArrowUp} Loads ${Emoji.ArrowUp}", Modifier.padding(8.dp), fixedEmojiSize = true)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth(.8f)
                .padding(4.dp)
                .border(1.dp, Color(KodeinColors.orange), RoundedCornerShape(4.dp))
                .padding(4.dp)
        ) {
            KodeinAnimatedVisibility(step < 2) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth(.8f)
                        .padding(4.dp)
                        .border(1.dp, Color(KodeinColors.orange), RoundedCornerShape(4.dp))
                        .background(Color(KodeinColors.dark_orange), RoundedCornerShape(4.dp))
                        .padding(2.dp)
                ) {
                    KodeinAnimatedVisibility(step == 0) {
                        Text("Swift")
                    }
                    KodeinAnimatedVisibility(step == 1) {
                        Text("Rust")
                    }
                    Text(" API")
                }
            }
            Row {
                Text("Static ")
                KodeinAnimatedVisibility(step == 0) {
                    Text("Obj-")
                }
                Text("C façade")
            }
        }

        TextWithNotoImageEmoji("${Emoji.ArrowUp} Links with ${Emoji.ArrowUp}", Modifier.padding(8.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth(.8f)
                .padding(4.dp)
                .border(1.dp, Color(KodeinColors.purple), RoundedCornerShape(4.dp))
                .padding(4.dp)
        ) {
            Text(
                text = "Kotlin/Native C-interop",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .padding(4.dp)
                    .border(1.dp, Color(KodeinColors.purple), RoundedCornerShape(4.dp))
                    .background(Color(KodeinColors.dark_purple), RoundedCornerShape(4.dp))
                    .padding(2.dp)
            )
            Text("kotlin/Multiplatform App")
        }
    }
}

val modernPlatformAPI = VerticalSlides(
    chachapolyTitle,
    chachapolyAppleSwift,
    chachapolyAppleDef,
    chachapolyAppleKotlin,
    SwiftInKotlinLink,
    modernLanguageBridge
)
