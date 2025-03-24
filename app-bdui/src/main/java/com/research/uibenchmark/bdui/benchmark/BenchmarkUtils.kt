package com.research.uibenchmark.bdui.benchmark

import android.util.Log
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

/**
 * Утилиты для измерения производительности UI
 */
object BenchmarkUtils {
    private const val TAG = "BduiBenchmark"
    
    /**
     * Измеряет время инициализации UI
     */
    fun measureInitTime(tag: String, block: () -> Unit): Long {
        val startTime = System.nanoTime()
        block()
        val endTime = System.nanoTime()
        val duration = (endTime - startTime) / 1_000_000 // в миллисекундах
        
        Log.d(TAG, "$tag-init: $duration ms")
        return duration
    }
    
    /**
     * Измеряет время отклика UI при обновлении данных
     */
    fun measureUpdateTime(tag: String, block: () -> Unit): Long {
        val duration = measureTimeMillis { block() }
        Log.d(TAG, "$tag-update: $duration ms")
        return duration
    }
    
    /**
     * Измеряет использование памяти
     */
    fun measureMemoryUsage(tag: String): Long {
        val runtime = Runtime.getRuntime()
        val usedMemory = runtime.totalMemory() - runtime.freeMemory()
        
        // Принудительный GC для более точных измерений
        runtime.gc()
        System.runFinalization()
        
        val formattedMemory = usedMemory / (1024 * 1024) // в МБ
        Log.d(TAG, "$tag-memory: $formattedMemory MB")
        return usedMemory
    }
    
    /**
     * Измеряет производительность прокрутки списка
     */
    fun measureScrollPerformance(tag: String, frames: Int, onFrame: () -> Unit): Double {
        var totalFrameTime = 0L
        
        for (i in 0 until frames) {
            val frameTime = measureNanoTime { onFrame() }
            totalFrameTime += frameTime
        }
        
        val avgFrameTimeMs = totalFrameTime / frames / 1_000_000.0
        val fps = 1000.0 / avgFrameTimeMs
        
        Log.d(TAG, "$tag-scroll: $avgFrameTimeMs ms per frame, $fps FPS")
        return fps
    }
    
    /**
     * Собирает все метрики в один отчет
     */
    data class BenchmarkResult(
        val uiType: String,
        val initTimeMs: Long,
        val updateTimeMs: Long,
        val memoryUsageMb: Long,
        val scrollFps: Double,
        val codeComplexity: Int?
    )
}
