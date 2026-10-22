package com.example.recursosconfiables.data

import com.example.recursosconfiables.Resource
import kotlinx.coroutines.flow.Flow

interface ResourceRepository {
    val resourcesFlow: Flow<List<Resource>>
    suspend fun saveResources(resources: List<Resource>)
    suspend fun toggleSaveResource(resourceId: Int)
}
