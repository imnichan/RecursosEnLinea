package com.example.recursosconfiables.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recursosconfiables.Resource
import com.example.recursosconfiables.ResourceUiState

@Composable
fun ResourceApp(viewModel: ResourceViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    ResourceScreenContent(
        uiState = uiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onCategorySelected = viewModel::onCategorySelected,
        onMaxReadTimeChanged = viewModel::onMaxReadTimeChanged,
        onAcademicToggled = viewModel::onAcademicToggled,
        onToggleSaveResource = viewModel::onToggleSaveResource,
        onTextChanged = viewModel::onTextChanged,
        onUpdateMsn = viewModel::onUpdateMsn
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceScreenContent(
    uiState: ResourceUiState,
    onSearchQueryChanged: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onMaxReadTimeChanged: (Int) -> Unit,
    onAcademicToggled: (Boolean) -> Unit,
    onToggleSaveResource: (Int) -> Unit,
    onTextChanged: (String) -> Unit,
    onUpdateMsn: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recursos en Línea [En Desarrollo]") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = uiState.text,
                onValueChange = onTextChanged,
                label = { Text("Texto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = onUpdateMsn,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Actualizar")
            }

            Text(
                text = uiState.msn
            )

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChanged,
                label = { Text("Buscar recursos...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            val categories = listOf("Todas", "Matemáticas", "Historia", "Filosofía", "Ciencias")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    FilterChip(
                        selected = uiState.selectedCategory == category,
                        onClick = { onCategorySelected(category) },
                        label = { Text(category) }
                    )
                }
            }

            Column {
                Text("Tiempo máximo de lectura: ${uiState.maxReadTime} min")
                Slider(
                    value = uiState.maxReadTime.toFloat(),
                    onValueChange = { onMaxReadTimeChanged(it.toInt()) },
                    valueRange = 5f..30f,
                    steps = 5
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Solo fuentes académicas/verificadas")
                Switch(
                    checked = uiState.onlyAcademic,
                    onCheckedChange = onAcademicToggled
                )
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Recursos guardados: ${uiState.savedCount} de ${uiState.allResources.size}")
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { uiState.savedProgress },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            HorizontalDivider()

            Text(
                text = "Resultados (${uiState.filteredResources.size}):",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.filteredResources, key = { it.id }) { resource ->
                    ResourceCard(
                        resource = resource,
                        onToggleSave = { onToggleSaveResource(resource.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ResourceCard(
    resource: Resource,
    onToggleSave: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (resource.isSaved) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = resource.title,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Categoría: ${resource.category} • ${resource.readTimeMinutes} min",
                    style = MaterialTheme.typography.bodySmall
                )
                if (resource.isAcademic) {
                    Text(
                        text = "Fuente Académica Verificada",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            TextButton(onClick = onToggleSave) {
                Text(if (resource.isSaved) "Guardado" else "Guardar")
            }
        }
    }
}