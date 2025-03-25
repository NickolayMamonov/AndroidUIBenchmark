//package com.research.uibenchmark.network
//
//import com.research.uibenchmark.model.Item
//import retrofit2.Response
//import retrofit2.http.*
//
//interface ApiService {
//    @GET("items")
//    suspend fun getItems(): Response<List<Item>>
//
//    @GET("items/{id}")
//    suspend fun getItem(@Path("id") id: Long): Response<Item>
//
//    @POST("items")
//    suspend fun createItem(@Body item: Item): Response<Item>
//
//    @PUT("items/{id}")
//    suspend fun updateItem(@Path("id") id: Long, @Body item: Item): Response<Item>
//
//    @DELETE("items/{id}")
//    suspend fun deleteItem(@Path("id") id: Long): Response<Unit>
//}
