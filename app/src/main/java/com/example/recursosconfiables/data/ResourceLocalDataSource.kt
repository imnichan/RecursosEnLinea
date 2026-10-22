package com.example.recursosconfiables.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.recursosconfiables.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "resources_prefs")

class ResourceLocalDataSource(private val context: Context) {
    private val gson = Gson()
    private val resourcesKey = stringPreferencesKey("resources_json_key")

    private val initialResources = listOf(
        Resource(1, "Cálculo Multivariable", "Matemáticas", "https://example.com/1", 15, true),
        Resource(2, "Historia Universal", "Historia", "https://example.com/2", 20, false),
        Resource(3, "Introducción a la Filosofía", "Filosofía", "https://example.com/3", 10, true),
        Resource(4, "Física Cuántica Básica", "Ciencias", "https://example.com/4", 25, true),
        Resource(5, "Álgebra Lineal", "Matemáticas", "https://example.com/5", 8, false)
    )

    val resourcesFlow: Flow<List<Resource>> = context.dataStore.data
        .map { preferences ->
            val json = preferences[resourcesKey]
            if (json.isNullOrEmpty()) {
                initialResources
            } else {
                val type = object : TypeToken<List<Resource>>() {}.type
                gson.fromJson<List<Resource>>(json, type) ?: initialResources
            }
        }

    suspend fun saveResources(resources: List<Resource>) {
        val json = gson.toJson(resources)
        context.dataStore.edit { preferences ->
            preferences[resourcesKey] = json
        }
    }

    suspend fun toggleSaveResource(resourceId: Int) {
        context.dataStore.edit { preferences ->
            val json = preferences[resourcesKey]
            val currentList = if (json.isNullOrEmpty()) {
                initialResources
            } else {
                val type = object : TypeToken<List<Resource>>() {}.type
                gson.fromJson<List<Resource>>(json, type) ?: initialResources
            }
            val updatedList = currentList.map { resource ->
                if (resource.id == resourceId) {
                    resource.copy(isSaved = !resource.isSaved)
                } else {
                    resource
                }
            }
            preferences[resourcesKey] = gson.toJson(updatedList)
        }
    }
}
