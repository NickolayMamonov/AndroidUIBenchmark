package com.research.uibenchmark.bdui.repository

import com.research.uibenchmark.bdui.model.BDUIResponse
import com.research.uibenchmark.bdui.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Репозиторий для работы с BDUI API
 */
class BDUIRepository(
    private val apiService: ApiService
) {
    suspend fun getMainScreen(): Result<BDUIResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getMainScreen()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetailScreen(id: String): Result<BDUIResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getDetailScreen(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSettingsScreen(): Result<BDUIResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getSettingsScreen()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfileScreen(): Result<BDUIResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getProfileScreen()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitData(data: Map<String, String>): Result<BDUIResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.submitData(data)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}