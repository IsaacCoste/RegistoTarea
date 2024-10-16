package com.example.registotarea.presentation.usuario

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
import com.example.registotarea.data.local.entities.UsuarioEntity
import com.example.registotarea.data.remote.dto.UsuarioDto

@Composable
fun UsuarioListScreen(
    viewModel: UsuarioViewModel = hiltViewModel(),
    createUsuario: () -> Unit,
    editUsuario: (Int) -> Unit,
    deleteUsuario: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getUsuarios()
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UsuarioBodyListScreen(
        uiState = uiState,
        isLoading = uiState.isLoading,
        createUsuario = createUsuario,
        editUsuario = editUsuario,
        deleteUsuario = deleteUsuario
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioBodyListScreen(
    uiState: UiState,
    isLoading: Boolean,
    createUsuario: () -> Unit,
    editUsuario: (Int) -> Unit,
    deleteUsuario: (Int) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lista de Usuarios",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = createUsuario,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Usuario")
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
                        items(uiState.usuarios) { usuario ->
                            UsuarioRow(
                                usuario = usuario,
                                editUsuario = editUsuario,
                                deleteUsuario = deleteUsuario
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
fun UsuarioRow(
    usuario: UsuarioEntity,
    editUsuario: (Int) -> Unit,
    deleteUsuario: (Int) -> Unit
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
                text = "ID: ${usuario.usuarioId}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Nombre: ${usuario.nombre}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Correo: ${usuario.correoElectronico}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Dirección: ${usuario.dirrecion}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Teléfono: ${usuario.telefono}",
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
                            editUsuario(usuario.usuarioId!!)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar Usuario"
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = {
                            expanded = false
                            deleteUsuario(usuario.usuarioId!!)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar Usuario"
                            )
                        }
                    )
                }
            }
        }
    }
}
