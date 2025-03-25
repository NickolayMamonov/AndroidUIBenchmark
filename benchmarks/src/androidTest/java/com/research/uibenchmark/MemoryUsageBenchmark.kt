package com.research.uibenchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Бенчмарк для измерения использования памяти
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class MemoryUsageBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    /**
     * Проверяет использование памяти после загрузки главного экрана
     */
    @Test
    fun composeMemoryUsageOnMainScreen() {
        val packageName = "com.research.uibenchmark.compose"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.MEMORY_USAGE,
            packageName = packageName,
            setup = {
                // Запустить приложение и дождаться загрузки главного экрана
                BenchmarkUtils.startApp(packageName)
                Thread.sleep(1000) // Даем время для полной загрузки
            },
            testBlock = {
                // Измерение памяти производится в функции measureUiMetric
            },
            teardown = {
                // Закрыть приложение
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }
    
    @Test
    fun xmlMemoryUsageOnMainScreen() {
        val packageName = "com.research.uibenchmark.xml"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.MEMORY_USAGE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                Thread.sleep(1000)
            },
            testBlock = { /* измерение выполняется автоматически */ },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }
    
    @Test
    fun bduiMemoryUsageOnMainScreen() {
        val packageName = "com.research.uibenchmark.bdui"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.MEMORY_USAGE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                Thread.sleep(1000)
            },
            testBlock = { /* измерение выполняется автоматически */ },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }
    
    /**
     * Проверяет пиковое использование памяти при загрузке списка данных
     */
    @Test
    fun composeMemoryUsageWithDataLoading() {
        val packageName = "com.research.uibenchmark.compose"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.MEMORY_USAGE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                
                // Нажимаем на кнопку загрузки данных 
                // (предполагается, что есть кнопка с ID load_data_button)
                val loadButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/load_data_button"))
                loadButton?.click()
                
                // Ждем загрузки данных
                Thread.sleep(2000)
            },
            testBlock = { /* измерение выполняется автоматически */ },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }
    
    @Test
    fun xmlMemoryUsageWithDataLoading() {
        val packageName = "com.research.uibenchmark.xml"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.MEMORY_USAGE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                val loadButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/load_data_button"))
                loadButton?.click()
                Thread.sleep(2000)
            },
            testBlock = { /* измерение выполняется автоматически */ },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }
    
    @Test
    fun bduiMemoryUsageWithDataLoading() {
        val packageName = "com.research.uibenchmark.bdui"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.MEMORY_USAGE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                val loadButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/load_data_button"))
                loadButton?.click()
                Thread.sleep(2000)
            },
            testBlock = { /* измерение выполняется автоматически */ },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }
}
