package com.research.uibenchmark.bdui.network

import com.research.uibenchmark.bdui.model.BDUIResponse
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("api/ui/main")
    suspend fun getMainScreen(): Response<BDUIResponse>

    @GET("api/ui/details/{id}")
    suspend fun getDetailScreen(@Path("id") id: String): Response<BDUIResponse>

    @GET("api/ui/settings")
    suspend fun getSettingsScreen(): Response<BDUIResponse>

    @GET("api/ui/profile")
    suspend fun getProfileScreen(): Response<BDUIResponse>

    @POST("api/ui/submit")
    suspend fun submitData(@FieldMap data: Map<String, String>): Response<BDUIResponse>
}