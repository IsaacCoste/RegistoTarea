package com.example.registotarea.presentation.proyecto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProyectoScreen(
    viewModel: ProyectoViewModel = hiltViewModel(),
    proyectoId: Int,
    goBack: () -> Unit,
    isProyectoDelete: Boolean
) {
    LaunchedEffect(proyectoId) {
        viewModel.getproyecto(proyectoId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ProyectoBodyScreen(
        uiState = uiState,
        onProyectoIdChange = viewModel::onProyectoIdChange,
        onNombreChange = viewModel::onNameChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onPresupuestoChange = viewModel::onPresupuestoChange,
        saveProyecto = viewModel::save,
        deleteProyecto = viewModel::delete,
        goBack = goBack,
        isProyectoDelete = isProyectoDelete
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectoBodyScreen(
    uiState: UiState,
    onProyectoIdChange: (Int) -> Unit,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPresupuestoChange: (Double) -> Unit,
    saveProyecto: () -> Unit,
    deleteProyecto: () -> Unit,
    goBack: () -> Unit,
    isProyectoDelete: Boolean
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Text(
                text = "Registro de Proyectos",
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
                label = { Text("Nombre") },
                value = uiState.nombre,
                onValueChange = onNombreChange
            )
            uiState.errorName.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = it, color = Color.Red)
                }
            }
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
                    Text(text = it, color = Color.Red)
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Presupuesto") },
                value = if (uiState.presupuesto != 0.0) uiState.presupuesto.toString() else "",
                onValueChange = { newPresupuesto ->
                    val presupuesto = newPresupuesto.toDoubleOrNull() ?: 0.0
                    onPresupuestoChange(presupuesto)
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            uiState.errorPresupuesto.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = it, color = Color.Red)
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
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text("Volver")
                }
                if (!isProyectoDelete) {
                    OutlinedButton(onClick = saveProyecto) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Guardar"
                        )
                        Text("Guardar")
                    }
                } else {
                    OutlinedButton(onClick = {
                        deleteProyecto()
                    }
                    ) {
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
