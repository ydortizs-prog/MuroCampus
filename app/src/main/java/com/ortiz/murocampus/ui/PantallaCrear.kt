package com.ortiz.murocampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ortiz.murocampus.ui.theme.DegradadoPrincipal
import com.ortiz.murocampus.viewmodel.PostsViewModel
import com.ortiz.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(
    vm: PostsViewModel,
    modifier: Modifier = Modifier
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var contenido by rememberSaveable { mutableStateOf("") }
    val estado by vm.publicacion.collectAsStateWithLifecycle()

    // Si se publicó bien, se limpia el formulario.
    // Si hubo error, se conserva lo escrito para reintentar.
    LaunchedEffect(estado) {
        if (estado is UiState.Exito) {
            titulo = ""
            contenido = ""
        }
    }

    val enviando = estado is UiState.Cargando
    val habilitado = titulo.isNotBlank() && contenido.isNotBlank() && !enviando

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "✍️  Nueva publicación",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Comparte algo con el campus. Se publicará como Usuario 1.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = contenido,
                    onValueChange = { contenido = it },
                    label = { Text("Contenido") },
                    minLines = 4,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Botón con degradado cuando está habilitado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(50))
                        .then(if (habilitado) Modifier.background(DegradadoPrincipal) else Modifier)
                ) {
                    Button(
                        onClick = { vm.publicar(titulo, contenido) },
                        enabled = habilitado,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (enviando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Text("Publicar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        when (val e = estado) {
            is UiState.Exito -> MensajeResultado(
                texto = "✅  Publicado con id ${e.datos.id}",
                fondo = MaterialTheme.colorScheme.secondaryContainer,
                colorTexto = MaterialTheme.colorScheme.onSecondaryContainer
            )
            is UiState.Error -> MensajeResultado(
                texto = "⚠️  ${e.mensaje}",
                fondo = MaterialTheme.colorScheme.errorContainer,
                colorTexto = MaterialTheme.colorScheme.onErrorContainer
            )
            else -> Unit
        }
    }
}

@Composable
private fun MensajeResultado(texto: String, fondo: Color, colorTexto: Color) {
    Surface(
        color = fondo,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = texto,
            color = colorTexto,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(16.dp)
        )
    }
}
