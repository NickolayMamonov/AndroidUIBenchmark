package com.research.uibenchmark

import android.content.Context
import android.content.Intent
import android.os.Debug
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import org.hamcrest.Matcher
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import java.io.File
import java.io.FileOutputStream

/**
 * Класс-утилита для бенчмаркингов UI
 */
object BenchmarkUtils {
    private const val LAUNCH_TIMEOUT = 5000L
    private const val UI_RESPONSE_TIMEOUT = 2000L
    val uiDevice: UiDevice by lazy { UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()) }
    private val context: Context by lazy { ApplicationProvider.getApplicationContext() }

    /**
     * Запуск приложения и ожидание появления активности
     * @param packageName пакет приложения для запуска
     * @param activityClass имя класса активности (опционально)
     */
    fun startApp(packageName: String, activityClass: String? = null): Boolean {
        // Старт с главного экрана
        uiDevice.pressHome()

        // Запуск приложения через Intent
        val intent = if (activityClass != null) {
            context.packageManager.getLaunchIntentForPackage(packageName)?.apply {
                setClassName(packageName, activityClass)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        } else {
            context.packageManager.getLaunchIntentForPackage(packageName)?.apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        }

        // Проверка, что Intent создан успешно
        if (intent == null) {
            throw RuntimeException("Could not create intent for package: $packageName, activity: $activityClass")
        }

        // Запуск приложения
        context.startActivity(intent)

        // Ждем появления приложения
        return uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), LAUNCH_TIMEOUT)
    }

    /**
     * Прокрутка списка вниз и вверх
     * @param listId resource ID списка
     * @param scrollSteps количество шагов прокрутки
     */
    fun scrollListUpAndDown(listId: String, scrollSteps: Int = 5) {
        val list = uiDevice.findObject(By.res(listId))
        require(list != null) { "Could not find list with ID: $listId" }

        // Прокрутка вниз
        repeat(scrollSteps) {
            list.swipe(androidx.test.uiautomator.Direction.DOWN, 0.8f)
            uiDevice.waitForIdle(500)
        }

        // Прокрутка вверх
        repeat(scrollSteps) {
            list.swipe(androidx.test.uiautomator.Direction.UP, 0.8f)
            uiDevice.waitForIdle(500)
        }
    }

    /**
     * Ожидание появления элемента на экране
     * @param selector селектор элемента
     * @param timeout таймаут ожидания
     */
    fun waitForElement(selector: org.hamcrest.Matcher<android.view.View>, timeout: Long = UI_RESPONSE_TIMEOUT): Boolean {
        return uiDevice.wait(Until.hasObject(By.res(selector.toString())), timeout)
    }

    /**
     * Преобразует bytes в MB
     */
    fun bytesToMB(bytes: Long): Double = bytes.toDouble() / (1024 * 1024)

    /**
     * Получить текущее использование памяти
     */
    fun getCurrentMemoryInfo(): MemoryInfo {
        val runtime = Runtime.getRuntime()
        val usedMemInMB = bytesToMB(runtime.totalMemory() - runtime.freeMemory())
        val maxHeapSizeInMB = bytesToMB(runtime.maxMemory())
        val freeHeapSizeInMB = bytesToMB(runtime.freeMemory())
        
        return MemoryInfo(usedMemInMB, maxHeapSizeInMB, freeHeapSizeInMB)
    }

    /**
     * Создает trace файл с заданным именем
     */
    fun startMethodTracing(filename: String) {
        val traceFile = File(context.getExternalFilesDir(null), "$filename.trace")
        Debug.startMethodTracing(traceFile.absolutePath)
    }

    /**
     * Останавливает трассировку
     */
    fun stopMethodTracing() {
        Debug.stopMethodTracing()
    }

    /**
     * Сохраняет результаты бенчмарка в файл
     */
    fun saveBenchmarkResult(filename: String, content: String) {
        val file = File(context.getExternalFilesDir(null), "$filename.txt")
        FileOutputStream(file).use { it.write(content.toByteArray()) }
    }
}

/**
 * Информация об использовании памяти
 */
data class MemoryInfo(
    val usedMemoryMB: Double,
    val maxHeapSizeMB: Double,
    val freeHeapSizeMB: Double
)

/**
 * Метрики производительности UI
 */
enum class UiMetricType {
    STARTUP_TIME,
    MEMORY_USAGE,
    DATA_CHANGE_RESPONSE,
    SCROLLING_PERFORMANCE,
    ANIMATION_PERFORMANCE
}

/**
 * Расширение для BenchmarkRule, позволяющее измерять UI метрики
 */
fun BenchmarkRule.measureUiMetric(
    metricType: UiMetricType,
    packageName: String,
    setup: () -> Unit = {},
    teardown: () -> Unit = {},
    testBlock: () -> Unit
) {
    var memoryBefore: MemoryInfo? = null
    var memoryAfter: MemoryInfo? = null
    
    measureRepeated {
        setup()
        
        // Если тестируем память, сохраняем состояние до теста
        if (metricType == UiMetricType.MEMORY_USAGE) {
            memoryBefore = BenchmarkUtils.getCurrentMemoryInfo()
        }
        
        // Начинаем системную трассировку для сложных метрик
        when (metricType) {
            UiMetricType.SCROLLING_PERFORMANCE,
            UiMetricType.ANIMATION_PERFORMANCE,
            UiMetricType.DATA_CHANGE_RESPONSE -> {
                BenchmarkUtils.startMethodTracing("${packageName}_${metricType.name}")
            }
            else -> { /* не включаем трассировку */ }
        }
        
        runWithTimingDisabled {
            // Запуск измеряемого кода
            testBlock()
            
            // Если тестируем память, сохраняем состояние после теста
            if (metricType == UiMetricType.MEMORY_USAGE) {
                memoryAfter = BenchmarkUtils.getCurrentMemoryInfo()
            }
            
            // Останавливаем трассировку
            when (metricType) {
                UiMetricType.SCROLLING_PERFORMANCE,
                UiMetricType.ANIMATION_PERFORMANCE,
                UiMetricType.DATA_CHANGE_RESPONSE -> {
                    BenchmarkUtils.stopMethodTracing()
                }
                else -> { /* не включали трассировку */ }
            }
        }
        
        teardown()
    }
    
    // Сохраняем дополнительные метрики памяти если это нужно
    if (metricType == UiMetricType.MEMORY_USAGE && memoryBefore != null && memoryAfter != null) {
        val memDiff = memoryAfter!!.usedMemoryMB - memoryBefore!!.usedMemoryMB
        BenchmarkUtils.saveBenchmarkResult(
            "${packageName}_memory_usage",
            """
            Memory Test Results:
            Before Test: ${memoryBefore!!.usedMemoryMB} MB used
            After Test: ${memoryAfter!!.usedMemoryMB} MB used
            Memory Increase: $memDiff MB
            Max Heap Size: ${memoryAfter!!.maxHeapSizeMB} MB
            """.trimIndent()
        )
    }
}
