package slides

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.qrose.options.*
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import net.kodein.cup.Slide
import net.kodein.theme.KodeinColors
import net.kodein.theme.compose.Color
import net.kodein.theme.compose.KodeinLogo
import net.kodein.theme.compose.m2.Link
import net.kodein.theme.cup.kStyled


@Composable
fun LinksToThisPresentation() {
    Link(
        uri = "https://p.kodein.net/shortcode"
    ) {
        Text(
            text = "https://p.kodein.net/shortcode",
            style = MaterialTheme.typography.caption,
            color = Color(KodeinColors.coral)
        )
    }
    Image(
        painter = rememberQrCodePainter(
            "https://p.kodein.net/shortcode",
            shapes = QrShapes(
                ball = QrBallShape.roundCorners(.25f),
                frame = QrFrameShape.roundCorners(.25f),
                darkPixel = QrPixelShape.circle(),
            ),
            colors = QrColors(
                dark = QrBrush.solid(Color(KodeinColors.coral))
            )
        ),
        contentDescription = "This presentation",
        modifier = Modifier.padding(8.dp)
    )
}

val intro by Slide {

    KodeinLogo(Modifier.height(64.dp))

    Spacer(Modifier.height(16.dp))

    Text(
        text = "Name of the presentation",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h1
    )

    Text(
        text = kStyled { "${+m}Event${-m} - Date" },
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.subtitle2,
        fontWeight = FontWeight.Light
    )

    Text(
        text = "Presenter(s)",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.subtitle1,
        color = Color(KodeinColors.salmon)
    )

    Spacer(Modifier.height(32.dp))

    LinksToThisPresentation()

}
