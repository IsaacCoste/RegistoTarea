package com.example.registotarea.presentation.proyecto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registotarea.data.local.entities.ProyectoEntity
import com.example.registotarea.data.remote.dto.ProyectoDto
import com.example.registotarea.data.repository.ProyectoRepository
import com.example.registotarea.data.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProyectoViewModel @Inject constructor(
    private val proyectoRepository: ProyectoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getProyectos()
    }
    fun save() {
        viewModelScope.launch {
            if (validate()) {
                proyectoRepository.saveProyecto(_uiState.value.toEntity()).collectLatest { result ->
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
                                    errorPresupuesto = "Error al guardar el proyecto: ${result.message}"
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
                    proyectoId = 0,
                    nombre = "",
                    descripcion = "",
                    presupuesto = 0.0,
                )
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
                                errorName = it.errorName,
                                errorDescripcion = it.errorDescripcion,
                                errorPresupuesto = it.errorPresupuesto,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
    fun getproyecto(proyectoId: Int) {
        viewModelScope.launch {
            proyectoRepository.getProyecto(proyectoId).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                proyectoId = result.data?.proyectoId ?: 0,
                                nombre = result.data?.nombre ?: "",
                                descripcion = result.data?.descripcion ?: "",
                                presupuesto = result.data?.presupuesto ?: 0.0,
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorName = it.errorName,
                                errorDescripcion = it.errorDescripcion,
                                errorPresupuesto = it.errorPresupuesto,
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
            proyectoRepository.deleteProyecto(_uiState.value.proyectoId).let { result ->
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
                                errorPresupuesto = it.errorPresupuesto,
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
        if (_uiState.value.presupuesto <= 0.0) {
            _uiState.update {
                it.copy(errorPresupuesto = "El presupuesto es requerido")
            }
            isValid = false
        } else {
            _uiState.update {
                it.copy(errorPresupuesto = "")
            }
        }
        return isValid
    }
    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(nombre = name)
        }
    }
    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }
    fun onPresupuestoChange(presupuesto: Double) {
        _uiState.update {
            it.copy(presupuesto = presupuesto)
        }
    }
    fun onProyectoIdChange(proyectoId: Int) {
        _uiState.update {
            it.copy(proyectoId = proyectoId)
        }
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val proyectoId: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val presupuesto: Double = 0.0,
    val errorName: String = "",
    val errorDescripcion: String = "",
    val errorPresupuesto: String = "",
    val proyectos: List<ProyectoEntity> = emptyList()
)
fun UiState.toEntity() = ProyectoDto(
    proyectoId = proyectoId,
    nombre = nombre,
    descripcion = descripcion,
    presupuesto = presupuesto
)