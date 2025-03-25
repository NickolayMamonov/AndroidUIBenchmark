package com.research.uibenchmark.xml.network

import com.research.uibenchmark.xml.model.Item
import retrofit2.Response
import retrofit2.http.*

/**
 * Интерфейс для работы с API
 */
interface ApiService {
    @GET("ui/main")
    suspend fun getMainScreenUI(): Response<Map<String, Any>>
    
    @GET("ui/items")
    suspend fun getItemsScreenUI(): Response<Map<String, Any>>
    
    @GET("ui/items/{id}")
    suspend fun getItemDetailScreenUI(@Path("id") id: Long): Response<Map<String, Any>>
    
    @GET("ui/create-item")
    suspend fun getCreateItemScreenUI(): Response<Map<String, Any>>
    
    @GET("ui/benchmark")
    suspend fun getBenchmarkUI(): Response<Map<String, Any>>
    
    // Оригинальные методы для работы с данными
    @GET("items")
    suspend fun getItems(): Response<List<Item>>
    
    @GET("items/{id}")
    suspend fun getItem(@Path("id") id: Long): Response<Item>
    
    @POST("items")
    suspend fun createItem(@Body item: Item): Response<Item>
    
    @PUT("items/{id}")
    suspend fun updateItem(@Path("id") id: Long, @Body item: Item): Response<Item>
    
    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: Long): Response<Unit>
}
