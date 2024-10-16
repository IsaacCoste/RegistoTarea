package com.example.registotarea.presentation.usuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registotarea.data.local.entities.UsuarioEntity
import com.example.registotarea.data.remote.dto.UsuarioDto
import com.example.registotarea.data.repository.UsuarioRepository
import com.example.registotarea.data.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getUsuarios()
    }
    fun save() {
        viewModelScope.launch {
            if (validate()) {
                usuarioRepository.saveUsuario(_uiState.value.toEntity()).collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _uiState.update {
                                it.copy(isLoading = true)
                            }
                        }
                        is Resource.Success -> {
                            nuevo()
                            _uiState.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                        }
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorTelefono = "Error al guardar el usuario: ${result.message}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    private fun nuevo() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    usuarioId = 0,
                    nombre = "",
                    correoElectronico = "",
                    dirrecion = "",
                    telefono = "",
                    errorName = "",
                    errorCorreo = "",
                    errorDirrecion = "",
                    errorTelefono = ""
                )
            }
        }
    }
    fun getUsuarios() {
        viewModelScope.launch {
            usuarioRepository.getUsuarios().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                usuarios = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorName = it.errorName,
                                errorCorreo = it.errorCorreo,
                                errorDirrecion = it.errorDirrecion,
                                errorTelefono = it.errorTelefono,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
    fun getUsuario(usuarioId: Int) {
        viewModelScope.launch {
            usuarioRepository.getUsuario(usuarioId).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                usuarioId = result.data?.usuarioId ?: 0,
                                nombre = result.data?.nombre ?: "",
                                correoElectronico = result.data?.correoElectronico ?: "",
                                dirrecion = result.data?.dirrecion ?: "",
                                telefono = result.data?.telefono ?: "",
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorName = it.errorName,
                                errorCorreo = it.errorCorreo,
                                errorDirrecion = it.errorDirrecion,
                                errorTelefono = it.errorTelefono,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
    fun delete() {
        viewModelScope.launch {
            usuarioRepository.deleteUsuario(_uiState.value.usuarioId).let { result ->
                when(result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorTelefono = it.errorTelefono,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
    private fun validate(): Boolean {
        var isValid = true
        if (_uiState.value.nombre.isBlank()) {
            _uiState.update {
                it.copy(errorName = "El nombre es requerido")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorName = "")
            }
        }
        if (_uiState.value.correoElectronico.isBlank()) {
            _uiState.update {
                it.copy(errorCorreo = "El email no puede estar vacío")
            }
            isValid = false
        } else if (!isValidEmail(_uiState.value.correoElectronico)) {
            _uiState.update {
                it.copy(errorCorreo = "El formato del email no es válido")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorCorreo = "")
            }
        }
        if (_uiState.value.dirrecion.isBlank()) {
            _uiState.update {
                it.copy(errorDirrecion = "La dirrecion es requerida")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorDirrecion = "")
            }
        }
        if (_uiState.value.telefono.length > 13 || _uiState.value.telefono.length < 10) {
            _uiState.update {
                it.copy(errorTelefono = "El telefono es requerido")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorTelefono = "")
            }
        }
        return isValid
    }
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[\\p{L}0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        return email.matches(emailPattern.toRegex())
    }
    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(nombre = name)
        }
    }
    fun onCorreoChange(correo: String) {
        _uiState.update {
            it.copy(correoElectronico = correo)
        }
    }
    fun onDirrecionChange(dirrecion: String) {
        _uiState.update {
            it.copy(dirrecion = dirrecion)
        }
    }
    fun onTelefonoChange(telefono: String) {
        _uiState.update {
            it.copy(telefono = telefono)
        }
    }
    fun onUsuarioIdChange(usuarioId: Int) {
        _uiState.update {
            it.copy(usuarioId = usuarioId)
        }
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val usuarioId: Int = 0,
    val nombre: String = "",
    val correoElectronico: String = "",
    val dirrecion: String = "",
    val telefono: String = "",
    val errorName: String = "",
    val errorCorreo: String = "",
    val errorDirrecion: String = "",
    val errorTelefono: String = "",
    val usuarios: List<UsuarioEntity> = emptyList()
)

fun UiState.toEntity() = UsuarioDto(
    usuarioId = usuarioId,
    nombre = nombre,
    correoElectronico = correoElectronico,
    dirrecion = dirrecion,
    telefono = telefono
)