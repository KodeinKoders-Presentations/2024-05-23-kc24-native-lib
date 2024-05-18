package slides

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.kodein.cup.PreparedSlide
import net.kodein.cup.SLIDE_SIZE_16_9
import net.kodein.cup.Slide
import net.kodein.cup.sa.SAStyle
import net.kodein.cup.sa.line
import net.kodein.cup.sa.plus
import net.kodein.cup.sa.rememberSourceCode
import net.kodein.cup.widgets.material.BulletPoints
import net.kodein.theme.KodeinColors
import net.kodein.theme.compose.Color
import net.kodein.theme.compose.JetBrainsMono
import net.kodein.theme.compose.m2.Link
import net.kodein.theme.cup.KodeinSourceCode
import net.kodein.theme.cup.kStyled
import net.kodein.theme.cup.ui.KodeinAnimatedVisibility
import net.kodein.theme.cup.ui.KodeinFadeAnimatedVisibility
import org.kodein.emoji.Emoji
import org.kodein.emoji.compose.NotoAnimatedEmoji
import org.kodein.emoji.compose.NotoImageEmoji
import org.kodein.emoji.people_body.hand_single_finger.MiddleFinger
import org.kodein.emoji.smileys_emotion.face_negative.ImpSmile
import org.kodein.emoji.smileys_emotion.face_smiling.Laughing
import utils.VerticalSlides


val secp256k1Title by Slide(
    specs = { copy(size = SLIDE_SIZE_16_9) }
) { step ->
    Text("Secp256k1: Kotlin/Native external C API (All!)", style = MaterialTheme.typography.h2)

    Spacer(Modifier.height(16.dp))

    Text("With a static library", style = MaterialTheme.typography.h4)
}

val lotsOfTargets by Slide(
    stepCount = 2
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
                text = kStyled { "Are you ready to compile for\n${+b}18 targets${-b} spanning over\n${+b}7 Operating Systems${-b} with\n${+b}5 different toolchains${-b}?" },
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color(KodeinColors.purple_dark), RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
    }
}

val secp256k1Compile by Slide(
    stepCount = 12
) { step ->
    Text("Kotlin/Native static C lib", style = MaterialTheme.typography.h2)
    Row {
        Text("Step 1: compile the lib", style = MaterialTheme.typography.h4)
        KodeinAnimatedVisibility(step in 1..4) {
            Text(" with autotools", style = MaterialTheme.typography.h4, softWrap = false)
        }
        KodeinAnimatedVisibility(step in 5..8) {
            Text(" with CMake", style = MaterialTheme.typography.h4, softWrap = false)
        }
        KodeinAnimatedVisibility(step >= 9) {
            Text(" with other tools", style = MaterialTheme.typography.h4, softWrap = false)
        }
    }
    KodeinAnimatedVisibility(step >= 9) {
        Text("Bazel, Gradle, Meson, Ninja, Premake, SCons, Tup, Waf")
    }

    Spacer(Modifier.height(16.dp))

    BulletPoints(
        Modifier.width(448.dp)
    ) {
        BulletPoint {
            Column {
                Text("For Desktop / Server targets (easy!)")
                KodeinAnimatedVisibility(step < 1) {
                    Text(
                        text = "Linux: x86_64\nMacos: arm64, x86_64\nMinGW: x86_64",
                        style = MaterialTheme.typography.caption,
                        fontFamily = JetBrainsMono,
                        color = Color(KodeinColors.purple_light)
                    )
                }
                KodeinAnimatedVisibility(step == 2) {
                    KodeinSourceCode(rememberSourceCode("bash") {
                        "./configure --host=\${HOST} && make"
                    }, fontSize = 8.sp, modifier = Modifier.fillMaxWidth())
                }
                KodeinAnimatedVisibility(step == 6) {
                    KodeinSourceCode(rememberSourceCode("bash") {
                        "cmake -DHOST=${'$'}HOST -DCMAKE_BUILD_TYPE=release && make"
                    }, fontSize = 8.sp, modifier = Modifier.fillMaxWidth())
                }
                KodeinAnimatedVisibility(step >= 10, horizontalAlignment = Alignment.Start) {
                    KodeinAnimatedVisibility(step < 11) {
                        NotoImageEmoji(Emoji.MiddleFinger, Modifier.size(48.dp))
                    }
                    KodeinAnimatedVisibility(step >= 11) {
                        Text("Figure it out!", color = Color(KodeinColors.light_purple))
                    }
                }
            }
        }
        BulletPoint {
            Column {
                Text("For all Apple device targets")
                KodeinAnimatedVisibility(step < 1) {
                    Text(
                        text = "iOS: arm64, simulator-arm64, simulator-x64\nwatchOS: arm32, arm64, simulator-arm64, simulator-x86_64\ntvOS: arm64, simulator-arm64, simulator-x86_64",
                        style = MaterialTheme.typography.caption + TextStyle(lineBreak = LineBreak.Paragraph),
                        fontFamily = JetBrainsMono,
                        color = Color(KodeinColors.purple_light),
                    )
                }
                KodeinAnimatedVisibility(step == 3) {
                    KodeinSourceCode(rememberSourceCode("bash") {
                        """
                            if [ ${'$'}{SDK} == "iphoneos" && "${'$'}ARCH" == "arm64" ]; then
                                HOST_FLAGS="-arch arm64 -arch arm64e -miphoneos-version-min=10.0 \
                                    -isysroot ${'$'}(xcrun --sdk ${'$'}{SDK} --show-sdk-path)"
                                CHOST="arm-apple-darwin"
                            elif [ ${'$'}{SDK} == "iphoneos" && "${'$'}ARCH" == "x86_64" ]; then
                                # ...
                            fi
                            export MACOSX_DEPLOYMENT_TARGET="10.4"
                            export CC=${'$'}(xcrun --find --sdk "${'$'}{SDK}" clang)
                            export CXX=${'$'}(xcrun --find --sdk "${'$'}{SDK}" clang++)
                            export CFLAGS="${'$'}{HOST_FLAGS} -O3 -g3 -fembed-bitcode"
                            export CXXFLAGS="${'$'}{HOST_FLAGS} -O3 -g3 -fembed-bitcode"
                            export LDFLAGS="${'$'}{HOST_FLAGS}"
                            
                            ./configure --host=${'$'}TARGET && make
                        """
                    }, fontSize = 8.sp, modifier = Modifier.fillMaxWidth())
                }
                KodeinAnimatedVisibility(step == 7) {
                    Link(
                        text = "github.com/leetal/ios-cmake",
                        uri = "https://github.com/leetal/ios-cmake",
                        modifier = Modifier.align(Alignment.Start)
                    )
                    KodeinSourceCode(rememberSourceCode("bash") {
                        """
                            cmake \
                                -DCMAKE_TOOLCHAIN_FILE="ios-cmake/ios.toolchain.cmake" \
                                -DPLATFORM=${'$'}PLATFORM \
                                -DCMAKE_C_FLAGS="-std=c++11 -Wno-shorten-64-to-32" \
                                -DCMAKE_CXX_FLAGS="-std=c++11 -Wno-shorten-64-to-32" 
                            make
                        """
                    }, fontSize = 8.sp, modifier = Modifier.fillMaxWidth())
                }
                KodeinAnimatedVisibility(step >= 10, horizontalAlignment = Alignment.Start) {
                    KodeinAnimatedVisibility(step < 11) {
                        NotoImageEmoji(Emoji.MiddleFinger, Modifier.size(48.dp))
                    }
                    KodeinAnimatedVisibility(step >= 11) {
                        Text("Figure it out!", color = Color(KodeinColors.light_purple))
                    }
                }
            }
        }
        BulletPoint {
            Column {
                Text("For all Android targets")
                KodeinAnimatedVisibility(step < 1) {
                    Text(
                        text = "Android-NDK: armv7a, aarch64, i686, x86_64",
                        style = MaterialTheme.typography.caption,
                        fontFamily = JetBrainsMono,
                        color = Color(KodeinColors.purple_light)
                    )
                }
                KodeinAnimatedVisibility(step == 4) {
                    KodeinSourceCode(rememberSourceCode("bash") {
                        """
                            TARGET=${'$'}SYS-linux-android
                            TOOLTARGET=${'$'}TARGET
                            if [ "${'$'}SYS" == "armv7a" ]; then
                                TARGET=armv7a-linux-androideabi
                                TOOLTARGET=arm-linux-androideabi
                            fi
                            export CC=${'$'}ANDROID_NDK/toolchains/llvm/prebuilt/${'$'}TOOLCHAIN/bin/${'$'}{TARGET}21-clang
                            export CXX=${'$'}ANDROID_NDK/toolchains/llvm/prebuilt/${'$'}TOOLCHAIN/bin/${'$'}{TARGET}21-clang++
                            export LD=${'$'}ANDROID_NDK/toolchains/llvm/prebuilt/${'$'}TOOLCHAIN/bin/${'$'}TOOLTARGET-ld
                            export AR=${'$'}ANDROID_NDK/toolchains/llvm/prebuilt/${'$'}TOOLCHAIN/bin/${'$'}TOOLTARGET-ar
                            export AS=${'$'}ANDROID_NDK/toolchains/llvm/prebuilt/${'$'}TOOLCHAIN/bin/${'$'}TOOLTARGET-as
                            export RANLIB=${'$'}ANDROID_NDK/toolchains/llvm/prebuilt/${'$'}TOOLCHAIN/bin/${'$'}TOOLTARGET-ranlib
                            export STRIP=${'$'}ANDROID_NDK/toolchains/llvm/prebuilt/${'$'}TOOLCHAIN/bin/${'$'}TOOLTARGET-strip
                            
                            ./configure --host=${'$'}TARGET && make
                        """
                    }, fontSize = 8.sp, modifier = Modifier.fillMaxWidth())
                }
                KodeinAnimatedVisibility(step == 8) {
                    Link(
                        text = "developer.android.com/ndk/guides/cmake",
                        uri = "https://developer.android.com/ndk/guides/cmake",
                        modifier = Modifier.align(Alignment.Start)
                    )
                    KodeinSourceCode(rememberSourceCode("bash") {
                        """
                            cmake \
                                -DCMAKE_TOOLCHAIN_FILE="${'$'}{NDK}/build/cmake/android.toolchain.cmake" \
                                -DANDROID_PLATFORM="android-21" \
                                -DANDROID_ABI=${'$'}PLATFORM
                            make
                        """
                    }, fontSize = 8.sp, modifier = Modifier.fillMaxWidth())
                }
                KodeinAnimatedVisibility(step >= 10, horizontalAlignment = Alignment.Start) {
                    KodeinAnimatedVisibility(step < 11) {
                        NotoImageEmoji(Emoji.MiddleFinger, Modifier.size(48.dp))
                    }
                    KodeinAnimatedVisibility(step >= 11) {
                        Text("Figure it out!", color = Color(KodeinColors.light_purple))
                    }
                }
            }
        }
    }
}

val secp256k1Def by PreparedSlide(
    stepCount = 2
) {

    val sourceCode = rememberSourceCode("properties") {
        val errorStyle = SAStyle(SpanStyle(color = Color(0xFF_FF4444))) + SAStyle.line(Color.Red, squiggle = true)
        val err by marker(styled(errorStyle, 1))

        """
            package = native.secp256k1
            
            headers = secp256k1.h secp256k1_ecdh.h
            headerFilter = secp256k1/** secp256k1.h secp256k1_ecdh.h
            
            staticLibraries = libsecp256k1.a

            libraryPaths.linux_x64 = secp256k1/build/linux/x64
            libraryPaths.mingw_x64 = secp256k1/build/mingw/x64
            libraryPaths.macos_x64 = secp256k1/build/macos/x64
            libraryPaths${err}.macos_arm64${X} = secp256k1/build/macos/arm64

            libraryPaths.ios_arm64 = secp256k1/build/ios/arm64
            linkerOpts.ios_arm64 = -framework Security -framework Foundation
            libraryPaths${err}.ios_simulator_arm64${X} = secp256k1/build/ios/arm64-sim
            linkerOpts${err}.ios_simulator_arm64${X} = -framework Security -framework Foundation
            libraryPaths.ios_x64 = secp256k1/build/ios/x86_64-sim
            linkerOpts.ios_x64 = -framework Security -framework Foundation
            
            # Same for watchos & tvos
        """
    }

    slideContent { step ->
        Text("Kotlin/Native static C lib", style = MaterialTheme.typography.h2)
        Text("Step 2: write a def file", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinSourceCode(sourceCode, step, file = "src/nativeInterop/cinterop/secp256k1.def", fontSize = 9.sp)
    }
}

val noDefUpdate by Slide(
    stepCount = 2
) { step ->
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.padding(16.dp)) {
            NotoAnimatedEmoji(Emoji.ImpSmile, iterations = 1, stopAt = 0.76f, modifier = Modifier.size(96.dp))
        }
        Text(
            text = "C-Interop definition tooling has not been updated since its creation and is not aware of newer targets.",
            fontSize = 18.sp,
            modifier = Modifier
                .padding(8.dp)
                .background(Color(KodeinColors.purple_dark), RoundedCornerShape(8.dp))
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
    Spacer(Modifier.height(8.dp))
    Link("https://youtrack.jetbrains.com/issue/KT-21922") {
        Text("KT-21922", fontSize = 24.sp)
    }
    KodeinAnimatedVisibility(visible = step >= 1) {
        Spacer(Modifier.height(4.dp))
        Text("Almost 2 years ago (2 jun 2022)")
    }
}

val secp256k1KNative by PreparedSlide {
    val sourceCode = rememberSourceCode("kotlin") {
        """
            override fun createPubKey(secKey: ByteArray): ByteArray {
                require(secKey.size == 32) { "secret key must be 32 bytes" }
                useContext(SECP256K1_CONTEXT_SIGN) { ctx ->
                    memScoped {
                        val pk = alloc<secp256k1_pubkey>()
                        secp256k1_ec_pubkey_create(
                            ctx, pk.ptr, secKey.asUByteArray().refTo(0)
                        ).check()
                        val pubKey = ByteArray(65)
                        val len = alloc<size_tVar>().also { it.value = 65.convert() }
                        secp256k1_ec_pubkey_serialize(
                            ctx, pubKey.asUByteArray().refTo(0),
                            len.ptr, pk.ptr,
                            SECP256K1_EC_UNCOMPRESSED
                        )
                        return pubKey
                    }
                }
            }
        """
    }

    slideContent {
        Text("Kotlin/Native static C lib", style = MaterialTheme.typography.h2)
        Text("Step 3: implement with Kotlin/Native", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinSourceCode(sourceCode, fontSize = 9.sp)
    }
}

val secp256k1WriteJni by PreparedSlide(
    stepCount = 3
) {
    val sourceCodeKotlin = rememberSourceCode("kotlin") {
        val ctx by marker(highlighted(1))

        """
            internal object Secp256k1Jni {
                val TYPE_SIGN = 0
                val TYPE_VERIFY = 1
            
            ${ctx}    external fun createContext(type: Int): Long
                external fun destroyContext(ctx: Long)${X}
            
                external fun verifySecKey(ctx: Long, seckey: ByteArray?): Boolean
                external fun createPubKey(ctx: Long, seckey: ByteArray?): ByteArray?
                external fun signMessage(
                    ctx: Long, msg: ByteArray?, seckey: ByteArray?
                ): ByteArray?
                external fun verifySignature(
                    ctx: Long, sig: ByteArray?, msg: ByteArray?, pubkey: ByteArray?
                ): Int
            }
        """
    }

    val sourceCodeC = rememberSourceCode("c") {
        """
            JNIEXPORT jbyteArray JNICALL Java_jni_secp256k1_Secp256k1Jni_createPubKey
              (JNIEnv *env, jobject, jlong jctx, jbyteArray jseckey)
            {
                secp256k1_context* ctx = (secp256k1_context *) jctx;
                jbyte *seckey = env->GetByteArrayElements(jseckey, 0);
                secp256k1_pubkey pub;
                int result = secp256k1_ec_pubkey_create(ctx, &pub, (unsigned char*) seckey);
                env->ReleaseByteArrayElements(jseckey, seckey, 0);
                if (result == 0) return NULL;
                jbyteArray jpubkey = env->NewByteArray(65);
                jbyte *pubkey = env->GetByteArrayElements(jpubkey, 0);
                size_t len = 65;
                result = secp256k1_ec_pubkey_serialize(
                    ctx, (unsigned char*) pubkey, &len, &pub, SECP256K1_EC_UNCOMPRESSED
                );
                env->ReleaseByteArrayElements(jpubkey, pubkey, 0);
                if (result == 0) return NULL;
                return jpubkey;
                return NULL;
            }
        """
    }

    slideContent { step ->
        Text("Kotlin/Native static C lib", style = MaterialTheme.typography.h2)
        Text("Step 4: write JNI façade", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinAnimatedVisibility(step in 0..1) {
            KodeinSourceCode(sourceCodeKotlin, step, fontSize = 9.sp)
        }
        KodeinAnimatedVisibility(step >= 2) {
            KodeinSourceCode(sourceCodeC, fontSize = 9.sp)
        }
    }
}

val secp256k1CompileJni by PreparedSlide {
    val sourceCode = rememberSourceCode("cmake") {
        """
            cmake_minimum_required(VERSION 3.10.0)
            
            project(Secp256k1Jni)
            find_package(JNI REQUIRED)
            add_library( secp256k1-jni SHARED secp256k1-jni.cpp )
            target_include_directories( secp256k1-jni
                PUBLIC ${'$'}{CMAKE_CURRENT_LIST_DIR}/secp256k1/secp256k1/include
                PRIVATE ${'$'}{JNI_INCLUDE_DIRS}
            )
            
            if( DEFINED ANDROID_ABI )
            target_link_libraries( secp256k1-jni
                ${'$'}{CMAKE_CURRENT_LIST_DIR}/../secp256k1/build/android/${'$'}{ANDROID_ABI}/libsecp256k1.a
            )
            endif()
            if( DEFINED PLATFORM AND DEFINED ARCH )
            target_link_libraries( secp256k1-jni
                ${'$'}{CMAKE_CURRENT_LIST_DIR}/../secp256k1/build/${'$'}{PLATFORM}/${'$'}{ARCH}/libsecp256k1.a
            )
            endif()
        """
    }
    slideContent {
        Text("Kotlin/Native static C lib", style = MaterialTheme.typography.h2)
        Text("Step 5: compile JNI façade", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinSourceCode(sourceCode, file = "CMakeLists.txt", fontSize = 9.sp)
    }
}

val secp256k1GradleIntegration by PreparedSlide(
    stepCount = 2
) {
    val sourceCodeAndroid = rememberSourceCode("kotlin") {
        """
            android {
                externalNativeBuild {
                    cmake {
                        path("src/cpp/secp256k1/CMakeLists.txt")
                    }
                }
            }
        """
    }
    val sourceCodeJVM = rememberSourceCode("kotlin") {
        """
            val compileJVMJni = tasks.registering {
                doLast {
                    file("${'$'}buildDir/native/${'$'}host").mkdirs()
                    exec {
                        workingDir("${'$'}buildDir/native/${'$'}host")
                        commandLine("cmake", "${'$'}projectDir/src/cpp/secp256k1")
                    }
                    exec {
                        workingDir("${'$'}buildDir/native/${'$'}host")
                        commandLine("make")
                    }
                }
            }
            
            tasks.named<ProcessResources>(
                kotlin.jvm().compilations["main"].processResourcesTaskName
            ) {
                dependsOn(compileJVMJni)
                from("${'$'}buildDir/native/${'$'}host/lib/secp256k1-jni.${'$'}dylibExt")
            }
        """
    }
    slideContent { step ->
        Text("Kotlin/Native static C lib", style = MaterialTheme.typography.h2)
        Text("Step 6: integrate JNI façade compilation", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinAnimatedVisibility(step == 0) {
            KodeinSourceCode(sourceCodeAndroid, file = "build.gradle.kts", fontSize = 10.sp)
        }
        KodeinAnimatedVisibility(step == 1) {
            KodeinSourceCode(sourceCodeJVM, file = "build.gradle.kts", fontSize = 9.sp)
        }
    }
}

val secp256k1JNILoading by PreparedSlide(
    stepCount = 3
) {
    val sourceCodeAndroid = rememberSourceCode("kotlin") {
        """
            actual fun loadSecp256k1Jni() {
                System.loadLibrary("secp256k1-jni")
            }
        """
    }
    val sourceCodeJVM = rememberSourceCode("kotlin") {
        val load by marker(hidden(0))
        """
            actual fun loadSecp256k1Jni() {
                // Find dynamic lib extension from OS
                // Create temporary folder
                // Find and extract lib from resources into tmp
                // Load extracted library
            ${load}    NativeLoader.loadLibrary("awesome")${X}
            }
        """
    }
    slideContent { step ->
        Text("Kotlin/Native static C lib", style = MaterialTheme.typography.h2)
        Text("Step 7: write loading logic", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinAnimatedVisibility(step == 0) {
            Text("Android", style = MaterialTheme.typography.h6)
            KodeinSourceCode(sourceCodeAndroid)
        }
        KodeinAnimatedVisibility(step >= 1) {
            Text("JVM", style = MaterialTheme.typography.h6)
            KodeinSourceCode(sourceCodeJVM, step - 1)
            KodeinAnimatedVisibility(step >= 2) {
                Link(
                    uri = "https://github.com/scijava/native-lib-loader",
                    text = "github.com/scijava/native-lib-loader"
                )
            }
        }
    }
}

val secp256k1Kotlin by PreparedSlide {
    val sourceCode = rememberSourceCode("cmake") {
        """
            private class Secp256k1Jvm : Secp256k1 {

                companion object {
                    init { loadSecp256k1Jni() }
                }
            
                override fun createPubKey(secKey: ByteArray): ByteArray {
                    require(secKey.size == 32) { "secret key must be 32 bytes" }
                    val ctx = Secp256k1Jni.createContext(Secp256k1Jni.TYPE_SIGN)
                    try {
                        return Secp256k1Jni.createPubKey(ctx, secKey)
                            ?: error("Operation failed")
                    } finally {
                        Secp256k1Jni.destroyContext(ctx)
                    }
                }

            }
        """
    }
    slideContent {
        Text("Kotlin/Native static C lib", style = MaterialTheme.typography.h2)
        Text("Step 8: write JVM Kotlin!", style = MaterialTheme.typography.h4)

        Spacer(Modifier.height(16.dp))

        KodeinSourceCode(sourceCode, fontSize = 10.sp)
    }
}

val staticCLib = VerticalSlides(
    secp256k1Title,
    lotsOfTargets,
    secp256k1Compile,
    secp256k1Def,
    noDefUpdate,
    secp256k1KNative,
    secp256k1WriteJni,
    secp256k1CompileJni,
    secp256k1GradleIntegration,
    secp256k1JNILoading,
    secp256k1Kotlin
)
