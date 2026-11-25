package com.ortiz.murocampus.viewmodel

// Los tres estados posibles de una pantalla que pide datos a la red
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}
