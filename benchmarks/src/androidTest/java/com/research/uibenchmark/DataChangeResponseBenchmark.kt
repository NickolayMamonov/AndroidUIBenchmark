package com.research.uibenchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Бенчмарк для измерения времени отклика UI на изменение данных
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class DataChangeResponseBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun composeDataChangeResponse() {
        val packageName = "com.research.uibenchmark.compose"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.DATA_CHANGE_RESPONSE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                Thread.sleep(1000) // Даем время для инициализации
            },
            testBlock = {
                // Предполагаем, что в UI есть кнопка обновления данных
                val updateButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/update_data_button"))
                if (updateButton != null) {
                    // Замеряем время между нажатием кнопки и обновлением UI
                    updateButton.click()
                    
                    // Ждем появления индикатора загрузки
                    val loadingIndicator = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/loading_indicator"))
                    loadingIndicator?.wait(Until.gone(5000))
                    
                    // Проверяем появление обновленных данных
                    val dataContainer = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/data_container"))
                    dataContainer?.wait(Until.visible(5000))
                }
            },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }

    @Test
    fun xmlDataChangeResponse() {
        val packageName = "com.research.uibenchmark.xml"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.DATA_CHANGE_RESPONSE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                Thread.sleep(1000)
            },
            testBlock = {
                val updateButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/update_data_button"))
                if (updateButton != null) {
                    updateButton.click()
                    
                    val loadingIndicator = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/loading_indicator"))
                    loadingIndicator?.wait(Until.gone(5000))
                    
                    val dataContainer = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/data_container"))
                    dataContainer?.wait(Until.visible(5000))
                }
            },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }

    @Test
    fun bduiDataChangeResponse() {
        val packageName = "com.research.uibenchmark.bdui"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.DATA_CHANGE_RESPONSE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                Thread.sleep(1000)
            },
            testBlock = {
                val updateButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/update_data_button"))
                if (updateButton != null) {
                    updateButton.click()
                    
                    val loadingIndicator = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/loading_indicator"))
                    loadingIndicator?.wait(Until.gone(5000))
                    
                    val dataContainer = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/data_container"))
                    dataContainer?.wait(Until.visible(5000))
                }
            },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }
}
