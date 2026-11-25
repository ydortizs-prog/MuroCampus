package com.ortiz.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ortiz.murocampus.ui.Encabezado
import com.ortiz.murocampus.ui.PantallaCrear
import com.ortiz.murocampus.ui.PantallaDetalle
import com.ortiz.murocampus.ui.PantallaLista
import com.ortiz.murocampus.ui.theme.MuroCampusTheme
import com.ortiz.murocampus.viewmodel.PostsViewModel
import com.ortiz.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuroCampusTheme {
                val vm: PostsViewModel = viewModel(factory = PostsViewModelFactory)
                // null = se ve la lista; un id = se ve el detalle de esa publicación
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                // true = se ve el formulario para crear una publicación
                var creando by rememberSaveable { mutableStateOf(false) }

                val volver = {
                    idSeleccionado = null
                    creando = false
                }

                // El botón Atrás del sistema vuelve a la lista
                BackHandler(enabled = idSeleccionado != null || creando) { volver() }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        when {
                            creando -> Encabezado(
                                titulo = "MuroCampus",
                                subtitulo = "Crear publicación",
                                onAtras = volver
                            )
                            idSeleccionado != null -> Encabezado(
                                titulo = "MuroCampus",
                                subtitulo = "Detalle y comentarios",
                                onAtras = volver
                            )
                            else -> Encabezado(
                                titulo = "MuroCampus",
                                subtitulo = "El muro de publicaciones del campus"
                            )
                        }
                    },
                    floatingActionButton = {
                        // El botón + solo aparece en la lista
                        if (idSeleccionado == null && !creando) {
                            ExtendedFloatingActionButton(
                                onClick = {
                                    vm.reiniciarPublicacion()
                                    creando = true
                                },
                                shape = RoundedCornerShape(50),
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Text("+  Publicar", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                ) { innerPadding ->
                    val id = idSeleccionado
                    if (creando) {
                        PantallaCrear(
                            vm = vm,
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else if (id == null) {
                        PantallaLista(
                            vm = vm,
                            onAbrir = { idSeleccionado = it },
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        PantallaDetalle(
                            vm = vm,
                            id = id,
                            onBorrado = { idSeleccionado = null },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
