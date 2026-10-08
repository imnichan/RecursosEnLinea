package com.example.recursosconfiables

data class Resource(
    val id: Int,
    val title: String,
    val category: String,
    val url: String,
    val readTimeMinutes: Int,
    val isAcademic: Boolean,
    val isSaved: Boolean = false
)

data class ResourceUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "Todas",
    val maxReadTime: Int = 30,
    val onlyAcademic: Boolean = false,
    val allResources: List<Resource> = emptyList(),
    val isLoading: Boolean = false,
    val text: String = "",
    val msn: String = "" /*<----------------------------------*/
) {
    val filteredResources: List<Resource>
        get() = allResources.filter { resource ->
            val matchesQuery = resource.title.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "Todas" || resource.category.equals(selectedCategory, ignoreCase = true)
            val matchesReadTime = resource.readTimeMinutes <= maxReadTime
            val matchesAcademic = !onlyAcademic || resource.isAcademic

            matchesQuery && matchesCategory && matchesReadTime && matchesAcademic
        }

    val savedCount: Int
        get() = allResources.count { it.isSaved }

    val savedProgress: Float
        get() = if (allResources.isNotEmpty()) savedCount.toFloat() / allResources.size else 0f
}