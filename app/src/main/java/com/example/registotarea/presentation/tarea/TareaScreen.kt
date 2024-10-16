package com.example.registotarea.presentation.tarea

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TareaScreen(
    viewModel: TareaViewModel = hiltViewModel(),
    tareaId: Int,
    goBack: () -> Unit,
    isTareaDelete: Boolean
) {
    LaunchedEffect(tareaId) {
        viewModel.getTarea(tareaId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TareaBodyScreen(
        uiState = uiState,
        onTareaIdChange = viewModel::onTareaIdChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onCompletadaChange = viewModel::onCompletadaChange,
        onUsuarioIdChange = viewModel::onUsuarioIdChange,
        onProyectoIdChange = viewModel::onProyectoIdChange,
        saveTarea = viewModel::save,
        deleteTarea = viewModel::delete,
        goBack = goBack,
        isTareaDelete = isTareaDelete
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaBodyScreen(
    uiState: UiState,
    onTareaIdChange: (Int) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onCompletadaChange: (Boolean) -> Unit,
    onUsuarioIdChange: (Int) -> Unit,
    onProyectoIdChange: (Int) -> Unit,
    saveTarea: () -> Unit,
    deleteTarea: () -> Unit,
    goBack: () -> Unit,
    isTareaDelete: Boolean
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Text(
                text = "Registro de Tareas",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Descripción") },
                value = uiState.descripcion,
                onValueChange = onDescripcionChange
            )
            uiState.errorDescripcion.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Completada", modifier = Modifier.weight(1f))
                Switch(
                    checked = uiState.completada,
                    onCheckedChange = onCompletadaChange
                )
            }
            uiState.errorCompletada.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }

            var expandedUsuario by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { expandedUsuario = true },
                    label = { Text("Usuario") },
                    value = if (uiState.usuarioId != 0) uiState.usuarios.find { it.usuarioId == uiState.usuarioId }?.nombre
                        ?: "" else "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.clickable { expandedUsuario = true }
                        )
                    }
                )
                DropdownMenu(
                    expanded = expandedUsuario,
                    onDismissRequest = { expandedUsuario = false }
                ) {
                    uiState.usuarios.forEach { usuario ->
                        DropdownMenuItem(
                            text = { Text(usuario.nombre) },
                            onClick = {
                                onUsuarioIdChange(usuario.usuarioId!!)
                                expandedUsuario = false
                            }
                        )
                    }
                }
            }
            uiState.errorUsuarioId.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }

            var expandedProyecto by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { expandedProyecto = true },
                    label = { Text("Proyecto") },
                    value = if (uiState.proyectoId != 0) uiState.proyectos.find { it.proyectoId == uiState.proyectoId }?.nombre
                        ?: "" else "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.clickable { expandedProyecto = true }
                        )
                    }
                )
                DropdownMenu(
                    expanded = expandedProyecto,
                    onDismissRequest = { expandedProyecto = false }
                ) {
                    uiState.proyectos.forEach { proyecto ->
                        DropdownMenuItem(
                            text = { Text(proyecto.nombre) },
                            onClick = {
                                onProyectoIdChange(proyecto.proyectoId!!)
                                expandedProyecto = false
                            }
                        )
                    }
                }
            }
            uiState.errorProyectoId.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = goBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                    Text("Volver")
                }
                if (!isTareaDelete) {
                    OutlinedButton(onClick = {
                        val isSaved = saveTarea()
                        if (isSaved != null) {
                            goBack()
                        }
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Guardar"
                        )
                        Text("Guardar")
                    }
                } else {
                    OutlinedButton(onClick = deleteTarea) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            tint = Color.Red,
                            contentDescription = "Eliminar"
                        )
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}
