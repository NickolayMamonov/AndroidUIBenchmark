package com.research.uibenchmark.bdui

import android.app.Application
import com.research.uibenchmark.bdui.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin DI
        startKoin {
            androidLogger(Level.ERROR) // Set to ERROR to avoid Koin crashes on Kotlin Metadata
            androidContext(this@App)
            modules(appModule)
        }
    }
}
