import net.kodein.cup.Slides
import net.kodein.cup.cupApplication
import net.kodein.theme.cup.KodeinPresentation
import net.kodein.theme.cup.slides.kodeinActivities
import slides.*


fun main() = cupApplication(
    title = "KC24: Native lib"
) {
    KodeinPresentation(
        slides = presentationSlides,
    )
}

val presentationSlides = Slides(
    intro,

    objectives,
    platforms,
    sha256platformAPIs,
    cLibs,
    dynamicCLib,
    staticCLib,
    // Cross-compilation
    // Swift & CSharp platform lib
    // JS
    // JS emscriptem
    // Conclusion

    kodeinActivities,
    outro
)
