package com.ortiz.murocampus.data

import com.ortiz.murocampus.data.remote.PostApi
import com.ortiz.murocampus.model.Comentario
import com.ortiz.murocampus.model.NuevoPost
import com.ortiz.murocampus.model.Post
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

// Contrato que conoce el ViewModel: nunca lanza excepciones, todo es Result.
interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
    suspend fun borrar(id: Int): Result<Unit>
}

// Implementación que usa el servicio web (Retrofit).
class PostRepositoryRemote(private val api: PostApi) : PostRepository {

    override suspend fun listar(): Result<List<Post>> = llamar { api.listar() }

    override suspend fun detalle(id: Int): Result<Post> = llamar { api.detalle(id) }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> =
        llamar { api.comentarios(id) }

    override suspend fun porUsuario(userId: Int): Result<List<Post>> =
        llamar { api.porUsuario(userId) }

    override suspend fun crear(nuevo: NuevoPost): Result<Post> = llamar { api.crear(nuevo) }

    override suspend fun borrar(id: Int): Result<Unit> = llamar {
        val respuesta = api.borrar(id)
        // Un 404 o 500 no lanza excepción solo: lo convertimos en HttpException
        if (!respuesta.isSuccessful) throw HttpException(respuesta)
    }

    // Un solo try/catch para las cinco funciones
    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e // nunca se traga la cancelación
        } catch (e: Exception) {
            Result.failure(e)
        }
}
