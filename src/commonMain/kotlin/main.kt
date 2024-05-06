import net.kodein.cup.Slides
import net.kodein.cup.cupApplication
import net.kodein.theme.cup.KodeinPresentation
import net.kodein.theme.cup.slides.kodeinActivities
import slides.intro
import slides.outro


fun main() = cupApplication(
    // TODO: Change title
    title = "Kodein Koders Presentation!"
) {
    KodeinPresentation(
        slides = presentationSlides,
    )
}

// TODO: Write your own slides!
val presentationSlides = Slides(
    intro,
    kodeinActivities,
    outro
)
