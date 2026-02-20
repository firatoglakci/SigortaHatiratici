package com.sigortahatiratici.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = MaviPrimary,
    secondary = MaviSecondary,
    tertiary = MaviTertiary,
    error = KirmiziHata
)

@Composable
fun SigortaTema(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
