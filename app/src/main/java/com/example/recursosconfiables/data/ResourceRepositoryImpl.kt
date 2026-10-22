package com.example.recursosconfiables.data

import com.example.recursosconfiables.Resource
import kotlinx.coroutines.flow.Flow

class ResourceRepositoryImpl(
    private val localDataSource: ResourceLocalDataSource
) : ResourceRepository {

    override val resourcesFlow: Flow<List<Resource>> = localDataSource.resourcesFlow

    override suspend fun saveResources(resources: List<Resource>) {
        localDataSource.saveResources(resources)
    }

    override suspend fun toggleSaveResource(resourceId: Int) {
        localDataSource.toggleSaveResource(resourceId)
    }
}
