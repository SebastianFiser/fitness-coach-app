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
    onSurfaceVariant = TextSecondaryDark,
    outline = OutlineDark,
    surfaceVariant = SurfaceVariantDark,
    primary = CyanAccent,
    onPrimary = OnPrimary
)

val LightColors = lightColorScheme(
    background = LightBackground,
    surface = LightSurface,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = OutlineLight,
    surfaceVariant = SurfaceVariantLight,
    primary = CyanAccent,
    onPrimary = OnPrimary
)

@Composable
fun FitCoachTheme(viewModel: AppViewModel, darkTheme: Boolean, content: @Composable () -> Unit) {
    val colors = when (darkTheme) {
        true -> DarkColors
        false -> LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
