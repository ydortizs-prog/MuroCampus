package com.ortiz.murocampus.data

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

// Traduce la excepción de un Result.failure a un texto para el usuario.
// Vive en data/ para que el ViewModel no conozca HttpException.
fun mensajeDe(e: Throwable): String = when (e) {
    is IOException -> "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."
    is HttpException -> "Error del servidor (${e.code()})"
    is SerializationException -> "Respuesta inesperada"
    else -> "Error desconocido"
}
