package slides

import IsOutro
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.kodein.cup.SLIDE_SIZE_16_9
import net.kodein.cup.Slide
import net.kodein.theme.compose.m2.Link
import net.kodein.theme.cup.ui.KodeinLogo


val outro by Slide(
    specs = { copy(size = SLIDE_SIZE_16_9) },
    user = IsOutro
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(end = 256.dp)
    ) {
        Text(
            text = "Thank you!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h1
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Salomon BRYS",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary
        )

        KodeinLogo("Koders", Modifier.height(32.dp)) {}

        Spacer(Modifier.height(16.dp))

        LinksToThisPresentation()

        Spacer(Modifier.height(16.dp))

        Row {
            ProvideTextStyle(MaterialTheme.typography.caption) {
                Text("(Made with ")
                Link(
                    text = "KodeinKoders/CuP",
                    uri = "https://github.com/KodeinKoders/CuP"
                )
                Text(")")
            }
        }
    }

}
