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
 * Бенчмарк для измерения производительности анимаций
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class AnimationPerformanceBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun composeAnimationPerformance() {
        val packageName = "com.research.uibenchmark.compose"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.ANIMATION_PERFORMANCE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                
                // Переход на экран с анимациями
                val animationScreenButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/animation_screen_button"))
                animationScreenButton?.click()
                
                // Ждем загрузки экрана с анимациями
                val animationContainer = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/animation_container"))
                animationContainer?.wait(Until.visible(5000))
                
                Thread.sleep(1000) // Стабилизация
            },
            testBlock = {
                // Запускаем анимацию нажатием на кнопку
                val startAnimationButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/start_animation_button"))
                if (startAnimationButton != null) {
                    repeat(5) {
                        startAnimationButton.click()
                        
                        // Ждем завершения анимации перед следующим запуском
                        Thread.sleep(2000) 
                    }
                }
            },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }

    @Test
    fun xmlAnimationPerformance() {
        val packageName = "com.research.uibenchmark.xml"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.ANIMATION_PERFORMANCE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                
                val animationScreenButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/animation_screen_button"))
                animationScreenButton?.click()
                
                val animationContainer = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/animation_container"))
                animationContainer?.wait(Until.visible(5000))
                
                Thread.sleep(1000)
            },
            testBlock = {
                val startAnimationButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/start_animation_button"))
                if (startAnimationButton != null) {
                    repeat(5) {
                        startAnimationButton.click()
                        Thread.sleep(2000)
                    }
                }
            },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }

    @Test
    fun bduiAnimationPerformance() {
        val packageName = "com.research.uibenchmark.bdui"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.ANIMATION_PERFORMANCE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                
                val animationScreenButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/animation_screen_button"))
                animationScreenButton?.click()
                
                val animationContainer = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/animation_container"))
                animationContainer?.wait(Until.visible(5000))
                
                Thread.sleep(1000)
            },
            testBlock = {
                val startAnimationButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/start_animation_button"))
                if (startAnimationButton != null) {
                    repeat(5) {
                        startAnimationButton.click()
                        Thread.sleep(2000)
                    }
                }
            },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }

    @Test
    fun composeComplexAnimationPerformance() {
        val packageName = "com.research.uibenchmark.compose"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.ANIMATION_PERFORMANCE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                
                // Переход на экран со сложными анимациями
                val complexAnimationButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/complex_animation_button"))
                complexAnimationButton?.click()
                
                // Ждем загрузки экрана
                val complexAnimationContainer = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/complex_animation_container"))
                complexAnimationContainer?.wait(Until.visible(5000))
                
                Thread.sleep(1000)
            },
            testBlock = {
                val startComplexAnimationButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/start_complex_animation_button"))
                if (startComplexAnimationButton != null) {
                    // Запускаем сложную анимацию
                    startComplexAnimationButton.click()
                    
                    // Ждем её выполнения (более длинная, около 5 секунд)
                    Thread.sleep(5000)
                }
            },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }
}
