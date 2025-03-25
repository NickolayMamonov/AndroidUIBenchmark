package com.research.uibenchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Бенчмарк для измерения производительности прокрутки списков
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class ScrollingPerformanceBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun composeListScrollingPerformance() {
        val packageName = "com.research.uibenchmark.compose"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.SCROLLING_PERFORMANCE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                
                // Предполагаем, что есть кнопка для перехода к экрану со списком
                val listScreenButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/list_screen_button"))
                listScreenButton?.click()
                
                // Ждем загрузки списка
                val listView = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/item_list"))
                listView?.wait(Until.visible(5000))
                
                Thread.sleep(1000) // Стабилизация
            },
            testBlock = {
                // Находим список и выполняем плавную прокрутку несколько раз
                val listView = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/item_list"))
                if (listView != null) {
                    // Прокрутка вниз
                    repeat(10) {
                        listView.swipe(Direction.DOWN, 0.8f)
                        BenchmarkUtils.uiDevice.waitForIdle(300)
                    }
                    
                    // Прокрутка вверх
                    repeat(10) {
                        listView.swipe(Direction.UP, 0.8f)
                        BenchmarkUtils.uiDevice.waitForIdle(300)
                    }
                }
            },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }

    @Test
    fun xmlListScrollingPerformance() {
        val packageName = "com.research.uibenchmark.xml"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.SCROLLING_PERFORMANCE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                
                val listScreenButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/list_screen_button"))
                listScreenButton?.click()
                
                val listView = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/item_list"))
                listView?.wait(Until.visible(5000))
                
                Thread.sleep(1000)
            },
            testBlock = {
                val listView = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/item_list"))
                if (listView != null) {
                    repeat(10) {
                        listView.swipe(Direction.DOWN, 0.8f)
                        BenchmarkUtils.uiDevice.waitForIdle(300)
                    }
                    
                    repeat(10) {
                        listView.swipe(Direction.UP, 0.8f)
                        BenchmarkUtils.uiDevice.waitForIdle(300)
                    }
                }
            },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }

    @Test
    fun bduiListScrollingPerformance() {
        val packageName = "com.research.uibenchmark.bdui"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.SCROLLING_PERFORMANCE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                
                val listScreenButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/list_screen_button"))
                listScreenButton?.click()
                
                val listView = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/item_list"))
                listView?.wait(Until.visible(5000))
                
                Thread.sleep(1000)
            },
            testBlock = {
                val listView = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/item_list"))
                if (listView != null) {
                    repeat(10) {
                        listView.swipe(Direction.DOWN, 0.8f)
                        BenchmarkUtils.uiDevice.waitForIdle(300)
                    }
                    
                    repeat(10) {
                        listView.swipe(Direction.UP, 0.8f)
                        BenchmarkUtils.uiDevice.waitForIdle(300)
                    }
                }
            },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }

    @Test
    fun composeRecyclerViewScrollingPerformance() {
        val packageName = "com.research.uibenchmark.compose"
        
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.SCROLLING_PERFORMANCE,
            packageName = packageName,
            setup = {
                BenchmarkUtils.startApp(packageName)
                
                // Предполагаем, что есть кнопка для перехода к экрану с большим списком данных
                val largeListButton = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/large_list_button"))
                largeListButton?.click()
                
                // Ждем загрузки списка
                val recyclerView = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/recycler_view"))
                recyclerView?.wait(Until.visible(5000))
                
                Thread.sleep(1000) // Стабилизация
            },
            testBlock = {
                // Тест быстрой прокрутки - fling
                val recyclerView = BenchmarkUtils.uiDevice.findObject(By.res("$packageName:id/recycler_view"))
                if (recyclerView != null) {
                    // Быстрая прокрутка вниз
                    recyclerView.fling(Direction.DOWN)
                    BenchmarkUtils.uiDevice.waitForIdle(1000)
                    
                    // Быстрая прокрутка вверх
                    recyclerView.fling(Direction.UP)
                    BenchmarkUtils.uiDevice.waitForIdle(1000)
                    
                    // Длинная прокрутка вниз
                    repeat(20) {
                        recyclerView.swipe(Direction.DOWN, 0.8f)
                        BenchmarkUtils.uiDevice.waitForIdle(200)
                    }
                }
            },
            teardown = {
                BenchmarkUtils.uiDevice.pressHome()
            }
        )
    }
}
