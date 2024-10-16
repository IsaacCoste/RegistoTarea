package com.example.registotarea.presentation.usuario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
fun UsuarioScreen(
    viewModel: UsuarioViewModel = hiltViewModel(),
    usuarioId: Int,
    goBack: () -> Unit,
    isUsuarioDelete: Boolean
) {
    LaunchedEffect(usuarioId) {
        viewModel.getUsuario(usuarioId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UsuarioBodyScreen(
        uiState = uiState,
        onUsuarioIdChange = viewModel::onUsuarioIdChange,
        onNameChange = viewModel::onNameChange,
        onCorreoChange = viewModel::onCorreoChange,
        onDirrecionChange = viewModel::onDirrecionChange,
        onTelefonoChange = viewModel::onTelefonoChange,
        saveUsuario = viewModel::save,
        deleteUsuario = viewModel::delete,
        goBack = goBack,
        isUsuarioDelete = isUsuarioDelete
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioBodyScreen(
    uiState: UiState,
    onUsuarioIdChange: (Int) -> Unit,
    onNameChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onDirrecionChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    saveUsuario: () -> Unit,
    deleteUsuario: () -> Unit,
    goBack: () -> Unit,
    isUsuarioDelete: Boolean
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)) {
            Text(
                text = "Registro de Usuarios",
                modifier = Modifier.fillMaxWidth()
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
                onValueChange = onNameChange
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
                label = { Text("Correo Electronico") },
                value = uiState.correoElectronico,
                onValueChange = onCorreoChange
            )
            uiState.errorCorreo.let {
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
                label = { Text("Dirección") },
                value = uiState.dirrecion,
                onValueChange = onDirrecionChange
            )
            uiState.errorDirrecion.let {
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
                label = { Text("Teléfono") },
                value = uiState.telefono,
                onValueChange = onTelefonoChange,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
            )
            uiState.errorTelefono.let {
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
                modifier = Modifier.fillMaxWidth()
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
                if (!isUsuarioDelete) {
                    OutlinedButton(onClick = saveUsuario) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Guardar"
                        )
                        Text("Guardar")
                    }
                } else {
                    OutlinedButton(onClick = {
                        deleteUsuario()
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