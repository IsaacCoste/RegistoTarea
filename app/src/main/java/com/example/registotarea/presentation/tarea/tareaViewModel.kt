package com.example.registotarea.presentation.tarea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registotarea.data.local.entities.ProyectoEntity
import com.example.registotarea.data.local.entities.TareaEntity
import com.example.registotarea.data.local.entities.UsuarioEntity
import com.example.registotarea.data.remote.dto.ProyectoDto
import com.example.registotarea.data.remote.dto.TareaDto
import com.example.registotarea.data.remote.dto.UsuarioDto
import com.example.registotarea.data.repository.ProyectoRepository
import com.example.registotarea.data.repository.TareaRepository
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
class TareaViewModel @Inject constructor(
    private val tareaRepository: TareaRepository,
    private val usuarioRepository: UsuarioRepository,
    private val proyectoRepository: ProyectoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    init {
        getTareas()
        getUsuarios()
        getProyectos()
    }
    fun save() {
        viewModelScope.launch {
            if (validate()) {
                tareaRepository.saveTarea(_uiState.value.toEntity()).collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _uiState.update {
                                it.copy(isLoading = true)
                            }
                        }
                        is Resource.Success -> {
                            nuevo()
                            _uiState.update {
                                it.copy(isLoading = false)
                            }
                        }
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorProyectoId = "Error al guardar la tarea: ${result.message}"
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
                    tareaId = 0,
                    descripcion = "",
                    completada = false,
                    usuarioId = 0,
                    proyectoId = 0,
                )
            }
        }
    }
    fun getTareas() {
        viewModelScope.launch {
            tareaRepository.getTareas().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                tareas = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorDescripcion = it.errorDescripcion,
                                errorCompletada = it.errorCompletada,
                                errorUsuarioId = it.errorUsuarioId,
                                errorProyectoId = it.errorProyectoId,
                            )
                        }
                    }
                }
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
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
    fun getProyectos() {
        viewModelScope.launch {
            proyectoRepository.getProyectos().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                proyectos = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
    fun getTarea(tareaId: Int) {
        viewModelScope.launch {
            tareaRepository.getTarea(tareaId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                tareaId = result.data?.tareaId ?: 0,
                                descripcion = result.data?.descripcion ?: "",
                                completada = result.data?.completada ?: false,
                                usuarioId = result.data?.usuarioId ?: 0,
                                proyectoId = result.data?.proyectoId ?: 0,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorDescripcion = it.errorDescripcion,
                                errorCompletada = it.errorCompletada,
                                errorUsuarioId = it.errorUsuarioId,
                                errorProyectoId = it.errorProyectoId,
                            )
                        }
                    }
                }
            }
        }
    }
    fun delete() {
        viewModelScope.launch {
            tareaRepository.deleteTarea(_uiState.value.tareaId).let { result ->
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
                                errorProyectoId = it.errorProyectoId,
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
        if (_uiState.value.descripcion.isBlank()) {
            _uiState.update {
                it.copy(errorDescripcion = "La descripcion es requerida")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorDescripcion = "")
            }
        }
        if (_uiState.value.usuarioId <= 0) {
            _uiState.update {
                it.copy(errorUsuarioId = "El usuario es requerido")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorUsuarioId = "")
            }
        }
        if (_uiState.value.proyectoId <= 0) {
            _uiState.update {
                it.copy(errorProyectoId = "El proyecto es requerido")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorProyectoId = "")
            }
        }
        return isValid
    }
    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }
    fun onCompletadaChange(completada: Boolean) {
        _uiState.update {
            it.copy(completada = completada)
        }
    }
    fun onUsuarioIdChange(usuarioId: Int) {
        _uiState.update {
            it.copy(usuarioId = usuarioId)
        }
    }
    fun onProyectoIdChange(proyectoId: Int) {
        _uiState.update {
            it.copy(proyectoId = proyectoId)
        }
    }
    fun onTareaIdChange(tareaId: Int) {
        _uiState.update {
            it.copy(tareaId = tareaId)
        }
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val tareaId: Int = 0,
    val descripcion: String = "",
    val completada: Boolean = false,
    val usuarioId: Int = 0,
    val proyectoId: Int = 0,
    val errorDescripcion: String = "",
    val errorCompletada: String = "",
    val errorUsuarioId: String = "",
    val errorProyectoId: String = "",
    val tareas: List<TareaEntity> = emptyList(),
    val usuarios: List<UsuarioEntity> = emptyList(),
    val proyectos: List<ProyectoEntity> = emptyList()
)
fun UiState.toEntity() = TareaDto(
    tareaId = tareaId,
    descripcion = descripcion,
    completada = completada,
    usuarioId = usuarioId,
    proyectoId = proyectoId
)