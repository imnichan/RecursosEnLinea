package com.example.recursosconfiables.ui

import androidx.lifecycle.ViewModel
import com.example.recursosconfiables.Resource
import com.example.recursosconfiables.ResourceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ResourceViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ResourceUiState(
            allResources = listOf(
                Resource(1, "Cálculo Multivariable", "Matemáticas", "https://example.com/1", 15, true),
                Resource(2, "Historia Universal", "Historia", "https://example.com/2", 20, false),
                Resource(3, "Introducción a la Filosofía", "Filosofía", "https://example.com/3", 10, true),
                Resource(4, "Física Cuántica Básica", "Ciencias", "https://example.com/4", 25, true),
                Resource(5, "Álgebra Lineal", "Matemáticas", "https://example.com/5", 8, false)
            )
        )
    )
    val uiState: StateFlow<ResourceUiState> = _uiState.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onMaxReadTimeChanged(maxTime: Int) {
        _uiState.update { it.copy(maxReadTime = maxTime) }
    }

    fun onAcademicToggled(onlyAcademic: Boolean) {
        _uiState.update { it.copy(onlyAcademic = onlyAcademic) }
    }

    fun onToggleSaveResource(resourceId: Int) {
        _uiState.update { currentState ->
            val updatedResources = currentState.allResources.map { resource ->
                if (resource.id == resourceId) {
                    resource.copy(isSaved = !resource.isSaved)
                } else {
                    resource
                }
            }
            currentState.copy(allResources = updatedResources)
        }
    }

    fun onTextChanged(newText: String) {
        _uiState.update { it.copy(text = newText) }
    }

    fun onUpdateMsn() {
        _uiState.update { it.copy(msn = it.text) }
    }
}