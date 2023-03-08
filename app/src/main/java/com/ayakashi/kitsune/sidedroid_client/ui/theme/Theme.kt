package com.ayakashi.kitsune.sidedroid_client.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = CornFlowerBlue,
    primaryVariant = Purple700,
    secondary = brownBorder,
    background = Color(20,20,20,255),
    onPrimary = paynegrey,

)

private val LightColorPalette = lightColors(
    primary = CornFlowerBlue,
    primaryVariant = RusViolet,
    secondary = brownBorder,
    background = justWhite,
    onPrimary = paynegrey,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun SideDroidClientTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}