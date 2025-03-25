package com.research.uibenchmark.bdui.di

import com.google.gson.GsonBuilder
import com.research.uibenchmark.bdui.network.ApiService
import com.research.uibenchmark.bdui.repository.BDUIRepository
import com.research.uibenchmark.bdui.viewmodel.BDUIViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    // Network components
    single { 
        HttpLoggingInterceptor().apply { 
            level = HttpLoggingInterceptor.Level.BODY 
        } 
    }
    
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    single {
        val gson = GsonBuilder()
            .setLenient()
            .create()
            
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/") // Используем 10.0.2.2 для доступа к localhost с эмулятора
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    // API Service
    single { get<Retrofit>().create(ApiService::class.java) }
    
    // Repository
    single { BDUIRepository(get()) }
    
    // ViewModel
    viewModel { BDUIViewModel(get()) }
}