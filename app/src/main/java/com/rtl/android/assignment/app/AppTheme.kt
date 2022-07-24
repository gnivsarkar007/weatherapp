package com.rtl.android.assignment.app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.rtl.android.assignment.R

val WHITE = Color(0xFFFFFFFF)
val BACKGROUND = Color(0xFF1C1B1F)
val PRIMARY = Color(0xFF1c1b1f)
val ON_BACKGROUND = Color(0xFF29272E)
val SECONDARY = Color(0xFF4e0feb)

@OptIn(ExperimentalUnitApi::class)
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            primary = PRIMARY,
            background = BACKGROUND,
            onBackground = ON_BACKGROUND,
            secondary = SECONDARY
        ),
        typography = MaterialTheme.typography.copy(
            h1 = TextStyle(
                color = WHITE,
                fontSize = TextUnit(48f, TextUnitType.Sp),
                fontFamily = appFontFamily,
                fontStyle = FontStyle.Normal,
                letterSpacing = TextUnit(0f, TextUnitType.Sp)
            ),
            h2 = TextStyle(
                color = WHITE,
                fontSize = TextUnit(22f, TextUnitType.Sp),
                fontFamily = appFontFamily,
                fontStyle = FontStyle.Normal,
                letterSpacing = TextUnit(0f, TextUnitType.Sp)
            ),
            h3 = TextStyle(
                color = WHITE,
                fontSize = TextUnit(16f, TextUnitType.Sp),
                fontFamily = appFontFamily,
                fontStyle = FontStyle.Normal,
                letterSpacing = TextUnit(0f, TextUnitType.Sp)
            ),
            body1 = TextStyle(
                color = WHITE,
                fontSize = TextUnit(14f, TextUnitType.Sp),
                fontFamily = appFontFamily,
                fontStyle = FontStyle.Normal,
                letterSpacing = TextUnit(0f, TextUnitType.Sp)
            ),
            button = MaterialTheme.typography.button.copy(color = WHITE)
        ),
        shapes = MaterialTheme.shapes,
        content = content
    )
}


val ROBOTO = Font(
    resId = R.font.roboto,
    weight = FontWeight.W900,
    style = FontStyle.Normal
)

val ROBOTO_ITALIC = Font(
    resId = R.font.roboto_italic,
    weight = FontWeight.W900,
    style = FontStyle.Italic
)

val ROBOTO_REGULAR = Font(
    resId = R.font.roboto_regular,
    weight = FontWeight.W700,
    style = FontStyle.Normal
)
val appFontFamily = FontFamily(
    fonts = listOf(ROBOTO, ROBOTO_ITALIC, ROBOTO_REGULAR)
)


