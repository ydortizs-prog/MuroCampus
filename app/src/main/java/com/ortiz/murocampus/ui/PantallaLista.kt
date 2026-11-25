package com.ortiz.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ortiz.murocampus.ui.theme.colorUsuario
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
        // Buscador con forma de píldora
        OutlinedTextField(
            value = busqueda,
            onValueChange = { vm.buscar(it) },
            placeholder = { Text("Buscar por título...") },
            leadingIcon = { Text("🔍", fontSize = 18.sp) },
            singleLine = true,
            shape = RoundedCornerShape(50),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            trailingIcon = {
                if (busqueda.isNotEmpty()) {
                    TextButton(onClick = { vm.buscar("") }) { Text("Limpiar") }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 14.dp)
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📭", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = if (busqueda.isBlank()) "Sin publicaciones"
                                else "Sin resultados para \"$busqueda\"",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "${e.datos.size} publicaciones",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
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
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(opciones) { userId ->
            // "Todos" usa el color principal; cada usuario su propio color
            val color = if (userId == null) MaterialTheme.colorScheme.primary else colorUsuario(userId)
            FilterChip(
                selected = seleccionado == userId,
                onClick = { onElegir(userId) },
                label = {
                    Text(
                        text = if (userId == null) "Todos" else "Usuario $userId",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                shape = RoundedCornerShape(50),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = color,
                    selectedContainerColor = color,
                    selectedLabelColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = seleccionado == userId,
                    borderColor = color.copy(alpha = 0.4f),
                    selectedBorderColor = color
                )
            )
        }
    }
}
