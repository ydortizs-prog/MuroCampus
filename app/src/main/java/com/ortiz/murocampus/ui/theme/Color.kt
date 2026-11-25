package com.ortiz.murocampus.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ---------- Paleta MuroCampus ----------
val Indigo = Color(0xFF5B4CF0)
val IndigoOscuro = Color(0xFF3D2FC4)
val IndigoClaro = Color(0xFFE6E3FF)
val Violeta = Color(0xFF8E44EC)
val Rosa = Color(0xFFEC4899)
val Turquesa = Color(0xFF00B8A9)
val TurquesaClaro = Color(0xFFD5F6F2)
val Coral = Color(0xFFFF6B6B)
val CoralClaro = Color(0xFFFFE3E3)
val FondoLavanda = Color(0xFFF6F5FF)
val TextoOscuro = Color(0xFF1E1B3A)
val TextoSuave = Color(0xFF6B6888)

// Modo oscuro
val IndigoNoche = Color(0xFFB4ABFF)
val FondoNoche = Color(0xFF12101F)
val SuperficieNoche = Color(0xFF1E1B33)

// Degradado del encabezado y de los elementos destacados
val DegradadoPrincipal = Brush.linearGradient(listOf(Indigo, Violeta, Rosa))

// Un color distinto para cada usuario (1 al 10)
private val ColoresUsuario = listOf(
    Color(0xFF5B4CF0), Color(0xFF00B8A9), Color(0xFFFF6B6B), Color(0xFFF59E0B),
    Color(0xFF8E44EC), Color(0xFF0EA5E9), Color(0xFFEC4899), Color(0xFF22C55E),
    Color(0xFFEF6C00), Color(0xFF6366F1)
)

fun colorUsuario(userId: Int): Color = ColoresUsuario[(userId - 1).mod(ColoresUsuario.size)]
