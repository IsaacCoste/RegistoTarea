package com.example.registotarea.presentation.tarea

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.registotarea.data.local.entities.ProyectoEntity
import com.example.registotarea.data.local.entities.TareaEntity
import com.example.registotarea.data.local.entities.UsuarioEntity
import com.example.registotarea.data.remote.dto.ProyectoDto
import com.example.registotarea.data.remote.dto.TareaDto
import com.example.registotarea.data.remote.dto.UsuarioDto

@Composable
fun TareaListScreen(
    viewModel: TareaViewModel = hiltViewModel(),
    createTarea: () -> Unit,
    editTarea: (Int) -> Unit,
    deleteTarea: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getTareas()
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TareaBodyListScreen(
        uiState = uiState,
        isLoading = uiState.isLoading,
        createTarea = createTarea,
        editTarea = editTarea,
        deleteTarea = deleteTarea
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaBodyListScreen(
    uiState: UiState,
    isLoading: Boolean,
    createTarea: () -> Unit,
    editTarea: (Int) -> Unit,
    deleteTarea: (Int) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lista de Tareas",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = createTarea,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Tarea")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                IndeterminateLinearProgressIndicatorSample(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(uiState.tareas) { tarea ->
                            TareaRow(
                                tarea = tarea,
                                usuario = uiState.usuarios,
                                proyecto = uiState.proyectos,
                                editTarea = editTarea,
                                deleteTarea = deleteTarea
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IndeterminateLinearProgressIndicatorSample(modifier: Modifier = Modifier) {
    LinearProgressIndicator(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun TareaRow(
    tarea: TareaEntity,
    usuario: List<UsuarioEntity>,
    proyecto: List<ProyectoEntity>,
    editTarea: (Int) -> Unit,
    deleteTarea: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val descripcionProyecto = proyecto.find { it.proyectoId == tarea.proyectoId }?.nombre
    val nombreUsuario = usuario.find { it.usuarioId == tarea.usuarioId }?.nombre

    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "ID: ${tarea.tareaId}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Descripción: ${tarea.descripcion}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = if (tarea.completada) "Completada" else "Pendiente",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "UsuarioID: ${nombreUsuario}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "ProyectoID: ${descripcionProyecto}",
                style = MaterialTheme.typography.bodyLarge
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            expanded = false
                            editTarea(tarea.tareaId!!)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar Tarea"
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = {
                            expanded = false
                            deleteTarea(tarea.tareaId!!)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar Tarea"
                            )
                        }
                    )
                }
            }
        }
    }
}
