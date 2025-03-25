package com.research.uibenchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Бенчмарк для измерения времени запуска UI
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class StartupBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    // Тесты для Compose UI
    @Test
    fun composeStartupTime() {
        val packageName = "com.research.uibenchmark.compose"
        
        benchmarkRule.measureRepeated {
            // Используем runWithTimingDisabled, чтобы не включать в замеры дополнительные операции
            runWithTimingDisabled {
                // Закрыть приложение, если оно уже открыто
                BenchmarkUtils.uiDevice.pressHome()
            }
            
            // Запускаем приложение и ждем его инициализации
            BenchmarkUtils.startApp(packageName)
        }
    }

    // Тесты для XML UI
    @Test
    fun xmlStartupTime() {
        val packageName = "com.research.uibenchmark.xml"
        
        benchmarkRule.measureRepeated {
            runWithTimingDisabled {
                BenchmarkUtils.uiDevice.pressHome()
            }
            
            BenchmarkUtils.startApp(packageName)
        }
    }

    // Тесты для BDUI
    @Test
    fun bduiStartupTime() {
        val packageName = "com.research.uibenchmark.bdui"
        
        benchmarkRule.measureRepeated {
            runWithTimingDisabled {
                BenchmarkUtils.uiDevice.pressHome()
            }
            
            BenchmarkUtils.startApp(packageName)
        }
    }
}
