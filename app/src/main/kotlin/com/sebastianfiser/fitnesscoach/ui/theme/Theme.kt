package com.sebastianfiser.fitnesscoach.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme


val DarkColors = darkColorScheme(
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    primary = CyanAccent,
    onPrimary = OnPrimary
)

val LightColors = lightColorScheme(
    background = LightBackground,
    surface = LightSurface,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    primary = CyanAccent,
    onPrimary = OnPrimary
)

@Composable
fun FitCoachTheme(content: @Composable () -> Unit, viewModel: AppViewModel) {
    val isDarkTheme = viewModel.isDarkTheme.value
    val colors = when (isDarkTheme) {
        true -> DarkColors
        false -> LightColors
        null -> if (isSystemInDarkTheme()) DarkColors else LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}