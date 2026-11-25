package com.ortiz.murocampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Indigo,
    onPrimary = Color.White,
    primaryContainer = IndigoClaro,
    onPrimaryContainer = IndigoOscuro,
    secondary = Turquesa,
    onSecondary = Color.White,
    secondaryContainer = TurquesaClaro,
    onSecondaryContainer = Color(0xFF00524B),
    tertiary = Coral,
    tertiaryContainer = CoralClaro,
    background = FondoLavanda,
    onBackground = TextoOscuro,
    surface = Color.White,
    onSurface = TextoOscuro,
    surfaceVariant = Color(0xFFEDEBFA),
    onSurfaceVariant = TextoSuave,
    error = Color(0xFFE5484D),
    errorContainer = CoralClaro,
    onErrorContainer = Color(0xFF8A1C1C)
)

private val DarkColorScheme = darkColorScheme(
    primary = IndigoNoche,
    onPrimary = Color(0xFF1B1366),
    primaryContainer = IndigoOscuro,
    onPrimaryContainer = IndigoClaro,
    secondary = Turquesa,
    secondaryContainer = Color(0xFF00524B),
    onSecondaryContainer = TurquesaClaro,
    tertiary = Coral,
    background = FondoNoche,
    surface = SuperficieNoche,
    surfaceVariant = Color(0xFF2A2645),
    errorContainer = Color(0xFF5C1A1A),
    onErrorContainer = CoralClaro
)

@Composable
fun MuroCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
