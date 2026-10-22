package com.example.recursosconfiables

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResourceUiStateTest {

    private val sampleResources = listOf(
        Resource(1, "Cálculo Multivariable", "Matemáticas", "https://example.com/1", 15, true),
        Resource(2, "Historia Universal", "Historia", "https://example.com/2", 20, false),
        Resource(3, "Introducción a la Filosofía", "Filosofía", "https://example.com/3", 10, true),
        Resource(4, "Física Cuántica Básica", "Ciencias", "https://example.com/4", 25, true),
        Resource(5, "Álgebra Lineal", "Matemáticas", "https://example.com/5", 8, false)
    )

    @Test
    fun filter_bySearchQuery() {
        val state = ResourceUiState(
            searchQuery = "Cálculo",
            allResources = sampleResources
        )
        val filtered = state.filteredResources
        assertEquals(1, filtered.size)
        assertEquals("Cálculo Multivariable", filtered[0].title)
    }

    @Test
    fun filter_byCategory() {
        val state = ResourceUiState(
            selectedCategory = "Filosofía",
            allResources = sampleResources
        )
        val filtered = state.filteredResources
        assertEquals(1, filtered.size)
        assertEquals("Introducción a la Filosofía", filtered[0].title)
    }

    @Test
    fun filter_byMaxReadTime() {
        val state = ResourceUiState(
            maxReadTime = 10,
            allResources = sampleResources
        )
        val filtered = state.filteredResources
        assertEquals(2, filtered.size)
    }

    @Test
    fun filter_byAcademic() {
        val state = ResourceUiState(
            onlyAcademic = true,
            allResources = sampleResources
        )
        val filtered = state.filteredResources
        assertEquals(3, filtered.size)
        assertTrue(filtered.all { it.isAcademic })
    }

    @Test
    fun savedCountAndProgress() {
        val resourcesWithSaves = sampleResources.map {
            if (it.id == 1 || it.id == 2) it.copy(isSaved = true) else it
        }
        val state = ResourceUiState(allResources = resourcesWithSaves)
        assertEquals(2, state.savedCount)
        assertEquals(0.4f, state.savedProgress)
    }
}
