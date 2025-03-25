package com.research.uibenchmark.compose.repository

import com.research.uibenchmark.compose.network.RetrofitClient
import com.research.uibenchmark.compose.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemRepository {
    private val apiService = RetrofitClient.apiService
    
    suspend fun getItems(): List<Item> = withContext(Dispatchers.IO) {
        val response = apiService.getItems()
        if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }
    
    suspend fun getItem(id: Long): Item? = withContext(Dispatchers.IO) {
        val response = apiService.getItem(id)
        if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
    
    suspend fun createItem(item: Item): Item? = withContext(Dispatchers.IO) {
        val response = apiService.createItem(item)
        if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
    
    suspend fun updateItem(id: Long, item: Item): Item? = withContext(Dispatchers.IO) {
        val response = apiService.updateItem(id, item)
        if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
    
    suspend fun deleteItem(id: Long): Boolean = withContext(Dispatchers.IO) {
        val response = apiService.deleteItem(id)
        response.isSuccessful
    }
}
