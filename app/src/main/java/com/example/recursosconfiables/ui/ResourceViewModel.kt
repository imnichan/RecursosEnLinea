package com.example.recursosconfiables.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.recursosconfiables.Resource
import com.example.recursosconfiables.ResourceUiState
import com.example.recursosconfiables.data.ResourceLocalDataSource
import com.example.recursosconfiables.data.ResourceRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResourceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ResourceRepositoryImpl(ResourceLocalDataSource(application))

    private val _uiState = MutableStateFlow(ResourceUiState())
    val uiState: StateFlow<ResourceUiState> = _uiState.asStateFlow()

    init {
        repository.resourcesFlow
            .onEach { resources ->
                _uiState.update { it.copy(allResources = resources) }
            }
            .launchIn(viewModelScope)
    }

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

    fun onResetFilters() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                selectedCategory = "Todas",
                maxReadTime = 30,
                onlyAcademic = false
            )
        }
    }

    fun onToggleSaveResource(resourceId: Int) {
        viewModelScope.launch {
            repository.toggleSaveResource(resourceId)
        }
    }

    fun onTextChanged(newText: String) {
        _uiState.update { it.copy(text = newText) }
    }

    fun onUpdateMsn() {
        _uiState.update { it.copy(msn = it.text) }
    }
}
