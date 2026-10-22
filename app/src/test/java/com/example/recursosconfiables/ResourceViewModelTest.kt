package com.example.recursosconfiables

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResourceViewModelTest {

    private val sampleResources = listOf(
        Resource(1, "Cálculo Multivariable", "Matemáticas", "https://example.com/1", 15, true),
        Resource(2, "Historia Universal", "Historia", "https://example.com/2", 20, false),
        Resource(3, "Introducción a la Filosofía", "Filosofía", "https://example.com/3", 10, true),
        Resource(4, "Física Cuántica Básica", "Ciencias", "https://example.com/4", 25, true),
        Resource(5, "Álgebra Lineal", "Matemáticas", "https://example.com/5", 8, false)
    )

    @Test
    fun uiState_filteringLogic_worksCorrectly() {
        val state = ResourceUiState(
            searchQuery = "Historia",
            allResources = sampleResources
        )
        val filtered = state.filteredResources
        assertEquals(1, filtered.size)
        assertEquals("Historia Universal", filtered[0].title)
    }

    @Test
    fun uiState_academicFilter_worksCorrectly() {
        val state = ResourceUiState(
            onlyAcademic = true,
            allResources = sampleResources
        )
        val filtered = state.filteredResources
        assertEquals(3, filtered.size)
        assertTrue(filtered.all { it.isAcademic })
    }
}
