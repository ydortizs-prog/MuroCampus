package com.ortiz.murocampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ortiz.murocampus.ui.theme.DegradadoPrincipal
import com.ortiz.murocampus.ui.theme.colorUsuario
import com.ortiz.murocampus.viewmodel.PostsViewModel
import com.ortiz.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    id: Int,
    onBorrado: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Pide los datos cuando se abre (o cambia) la publicación
    LaunchedEffect(id) { vm.abrir(id) }

    val estado by vm.detalle.collectAsStateWithLifecycle()
    val borrado by vm.borrado.collectAsStateWithLifecycle()
    var confirmar by remember { mutableStateOf(false) }

    // Si se borró bien, volvemos a la lista
    LaunchedEffect(borrado) {
        if (borrado is UiState.Exito) {
            vm.reiniciarBorrado()
            onBorrado()
        }
    }

    if (confirmar) {
        AlertDialog(
            onDismissRequest = { confirmar = false },
            title = { Text("Eliminar publicación") },
            text = { Text("¿Seguro que quieres eliminar esta publicación?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmar = false
                    vm.borrar(id)
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmar = false }) { Text("Cancelar") }
            }
        )
    }

    when (val e = estado) {
        is UiState.Cargando -> Cargando(texto = "Cargando publicación...", modifier = modifier)
        is UiState.Error -> ErrorConReintento(
            mensaje = e.mensaje,
            onReintentar = { vm.abrir(id) },
            modifier = modifier
        )
        is UiState.Exito -> {
            val post = e.datos.post
            val comentarios = e.datos.comentarios
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Tarjeta principal con degradado
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(DegradadoPrincipal)
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Avatar(texto = "U${post.userId}", color = Color.White.copy(alpha = 0.25f))
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text(
                                    "Usuario ${post.userId}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Publicación #${post.id}",
                                    color = Color.White.copy(alpha = 0.8f),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        Text(
                            text = post.title.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }

                // Texto completo y botón Eliminar
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = post.body,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(Modifier.height(14.dp))
                            OutlinedButton(
                                onClick = { confirmar = true },
                                enabled = borrado !is UiState.Cargando,
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text(
                                    if (borrado is UiState.Cargando) "Eliminando..." else "🗑  Eliminar",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            (borrado as? UiState.Error)?.let {
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = it.mensaje,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                // Título de comentarios con contador
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = "Comentarios (${comentarios.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                items(comentarios, key = { it.id }) { comentario ->
                    TarjetaComentario(
                        email = comentario.email,
                        texto = comentario.body,
                        color = colorUsuario(comentario.id)
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaComentario(email: String, texto: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(14.dp)) {
            Avatar(texto = email.first().uppercase(), color = color, tamano = 36)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = email,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = texto,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
