package com.ortiz.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ortiz.murocampus.viewmodel.PostsViewModel
import com.ortiz.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onAbrir: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by vm.listaVisible.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = busqueda,
            onValueChange = { vm.buscar(it) },
            placeholder = { Text("Buscar por título...") },
            singleLine = true,
            trailingIcon = {
                if (busqueda.isNotEmpty()) {
                    TextButton(onClick = { vm.buscar("") }) { Text("Limpiar") }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp)
        )
        FiltroUsuarios(
            seleccionado = filtro,
            onElegir = { vm.filtrar(it) }
        )

        when (val e = estado) {
            is UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorConReintento(
                mensaje = e.mensaje,
                onReintentar = { vm.cargar() }
            )
            is UiState.Exito -> {
                if (e.datos.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            if (busqueda.isBlank()) "Sin publicaciones"
                            else "Sin resultados para \"$busqueda\""
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(e.datos, key = { it.id }) { post ->
                            TarjetaPost(
                                userId = post.userId,
                                titulo = post.title,
                                cuerpo = post.body,
                                onClick = { onAbrir(post.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// Fila desplazable de chips: Todos, Usuario 1 ... Usuario 10
@Composable
private fun FiltroUsuarios(
    seleccionado: Int?,
    onElegir: (Int?) -> Unit
) {
    val opciones: List<Int?> = listOf(null) + (1..10).toList()
    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(opciones) { userId ->
            FilterChip(
                selected = seleccionado == userId,
                onClick = { onElegir(userId) },
                label = { Text(if (userId == null) "Todos" else "Usuario $userId") }
            )
        }
    }
}
