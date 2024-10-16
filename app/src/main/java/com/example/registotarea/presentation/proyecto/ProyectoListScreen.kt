package com.example.registotarea.presentation.proyecto

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
import com.example.registotarea.data.remote.dto.ProyectoDto

@Composable
fun ProyectoListScreen(
    viewModel: ProyectoViewModel = hiltViewModel(),
    createProyecto: () -> Unit,
    editProyecto: (Int) -> Unit,
    deleteProyecto: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getProyectos()
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ProyectoBodyListScreen(
        uiState = uiState,
        isLoading = uiState.isLoading,
        createProyecto = createProyecto,
        editProyecto = editProyecto,
        deleteProyecto = deleteProyecto
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectoBodyListScreen(
    uiState: UiState,
    isLoading: Boolean,
    createProyecto: () -> Unit,
    editProyecto: (Int) -> Unit,
    deleteProyecto: (Int) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lista de Proyectos",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = createProyecto,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Proyecto")
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
                        items(uiState.proyectos) { proyecto ->
                            ProyectoRow(
                                proyecto = proyecto,
                                editProyecto = editProyecto,
                                deleteProyecto = deleteProyecto
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
fun ProyectoRow(
    proyecto: ProyectoEntity,
    editProyecto: (Int) -> Unit,
    deleteProyecto: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
                text = "ID: ${proyecto.proyectoId}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Nombre: ${proyecto.nombre}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Descripción: ${proyecto.descripcion}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Presupuesto: ${proyecto.presupuesto}",
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
                            editProyecto(proyecto.proyectoId!!)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar Proyecto"
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = {
                            expanded = false
                            deleteProyecto(proyecto.proyectoId!!)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar Proyecto"
                            )
                        }
                    )
                }
            }
        }
    }
}
