package com.sebastianfiser.fitnesscoach.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.sebastianfiser.fitnesscoach.models.AppViewModel


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
fun FitCoachTheme(viewModel: AppViewModel, content: @Composable () -> Unit) {
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