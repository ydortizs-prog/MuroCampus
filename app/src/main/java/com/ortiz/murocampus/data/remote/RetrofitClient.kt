package com.ortiz.murocampus.data.remote

import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/" // termina con /

    // Ignora campos del JSON que no estén en nuestras data class
    private val json = Json { ignoreUnknownKeys = true }

    // Agrega el encabezado Accept: application/json a TODAS las peticiones
    private val interceptorAccept = Interceptor { chain ->
        val peticion = chain.request().newBuilder()
            .header("Accept", "application/json")
            .build()
        chain.proceed(peticion)
    }

    // Registro del tráfico HTTP en Logcat (solo para desarrollo)
    private val cliente = OkHttpClient.Builder()
        .addInterceptor(interceptorAccept) // primero el encabezado, luego el log lo muestra
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    // Se crea una sola vez, la primera vez que se usa
    val api: PostApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cliente)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PostApi::class.java)
    }
}
