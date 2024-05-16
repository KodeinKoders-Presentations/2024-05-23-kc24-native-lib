import net.kodein.cup.Slides
import net.kodein.cup.cupApplication
import net.kodein.theme.cup.KodeinPresentation
import net.kodein.theme.cup.slides.kodeinActivities
import slides.*


fun main() = cupApplication(
    // TODO: Change title
    title = "KC24: Native lib"
) {
    KodeinPresentation(
        slides = presentationSlides,
    )
}

// TODO: Write your own slides!
val presentationSlides = Slides(
    intro,

    objectives,
    platforms,
    sha256platformAPIs,
    cLibs,
    dynCLib,

    kodeinActivities,
    outro
)
