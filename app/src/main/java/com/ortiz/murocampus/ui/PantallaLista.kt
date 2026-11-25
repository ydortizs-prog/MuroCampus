package com.ortiz.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ortiz.murocampus.viewmodel.PostsViewModel
import com.ortiz.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    modifier: Modifier = Modifier
) {
    val estado by vm.lista.collectAsStateWithLifecycle()

    when (val e = estado) {
        is UiState.Cargando -> Cargando(modifier = modifier)
        is UiState.Error -> ErrorConReintento(
            mensaje = e.mensaje,
            onReintentar = { vm.cargar() },
            modifier = modifier
        )
        is UiState.Exito -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(e.datos, key = { it.id }) { post ->
                TarjetaPost(
                    userId = post.userId,
                    titulo = post.title,
                    cuerpo = post.body,
                    onClick = { }
                )
            }
        }
    }
}
