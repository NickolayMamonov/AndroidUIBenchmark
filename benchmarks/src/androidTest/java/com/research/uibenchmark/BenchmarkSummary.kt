package com.research.uibenchmark

import androidx.test.platform.app.InstrumentationRegistry
import org.json.JSONObject
import java.io.File

/**
 * Класс для сбора и компиляции результатов всех бенчмарков
 */
class BenchmarkSummary {

    /**
     * Собирает все метрики в единый отчет
     */
    fun generateSummaryReport() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val outputDir = context.getExternalFilesDir(null)
        val resultFiles = collectResultFiles(outputDir)
        
        val summary = JSONObject().apply {
            // Базовая информация
            put("timestamp", System.currentTimeMillis())
            put("device_model", android.os.Build.MODEL)
            put("android_version", android.os.Build.VERSION.RELEASE)
            
            // Группируем метрики по категориям
            putMetricsByCategory(this, resultFiles, "compose", "com.research.uibenchmark.compose")
            putMetricsByCategory(this, resultFiles, "xml", "com.research.uibenchmark.xml")
            putMetricsByCategory(this, resultFiles, "bdui", "com.research.uibenchmark.bdui")
        }
        
        // Сохраняем итоговый отчет
        val outputFile = File(outputDir, "benchmark_summary.json")
        outputFile.writeText(summary.toString(4)) // Отступы в 4 пробела для читаемости
        
        // Создаем человекочитаемую версию отчета
        val readableReport = createReadableReport(summary)
        val readableFile = File(outputDir, "benchmark_summary.txt")
        readableFile.writeText(readableReport)
        
        println("Benchmark summary saved to: ${outputFile.absolutePath}")
        println("Human-readable report saved to: ${readableFile.absolutePath}")
    }
    
    /**
     * Собирает все файлы результатов бенчмарков
     */
    private fun collectResultFiles(dir: File): List<File> {
        return dir.listFiles { file -> 
            file.isFile && (file.name.endsWith(".txt") || file.name.endsWith(".json"))
        }?.toList() ?: emptyList()
    }
    
    /**
     * Группирует метрики по категориям UI
     */
    private fun putMetricsByCategory(json: JSONObject, files: List<File>, categoryName: String, packagePrefix: String) {
        val categoryJson = JSONObject()
        
        // Находим файлы, относящиеся к данной категории UI
        val relevantFiles = files.filter { it.name.startsWith(packagePrefix) || it.name.contains(categoryName) }
        
        // Группируем по типам метрик
        val startupFiles = relevantFiles.filter { it.name.contains("startup") }
        val memoryFiles = relevantFiles.filter { it.name.contains("memory") }
        val scrollingFiles = relevantFiles.filter { it.name.contains("scrolling") }
        val animationFiles = relevantFiles.filter { it.name.contains("animation") }
        val dataChangeFiles = relevantFiles.filter { it.name.contains("data_change") }
        val complexityFiles = relevantFiles.filter { it.name.contains("complexity") }
        
        // Выделяем файлы разных типов запуска
        val coldStartFiles = startupFiles.filter { it.name.contains("cold") }
        val warmStartFiles = startupFiles.filter { it.name.contains("warm") }
        val hotStartFiles = startupFiles.filter { it.name.contains("hot") }
        
        // Добавляем метрики в JSON по категориям
        if (startupFiles.isNotEmpty()) {
            parseMetricsFromFiles(categoryJson, startupFiles, "startup_time")
        }
        
        // Добавляем метрики для разных типов запуска
        if (coldStartFiles.isNotEmpty()) {
            parseMetricsFromFiles(categoryJson, coldStartFiles, "cold_startup")
        }
        
        if (warmStartFiles.isNotEmpty()) {
            parseMetricsFromFiles(categoryJson, warmStartFiles, "warm_startup")
        }
        
        if (hotStartFiles.isNotEmpty()) {
            parseMetricsFromFiles(categoryJson, hotStartFiles, "hot_startup")
        }
        
        if (memoryFiles.isNotEmpty()) {
            parseMetricsFromFiles(categoryJson, memoryFiles, "memory_usage")
        }
        
        if (scrollingFiles.isNotEmpty()) {
            parseMetricsFromFiles(categoryJson, scrollingFiles, "scrolling_performance")
        }
        
        if (animationFiles.isNotEmpty()) {
            parseMetricsFromFiles(categoryJson, animationFiles, "animation_performance")
        }
        
        if (dataChangeFiles.isNotEmpty()) {
            parseMetricsFromFiles(categoryJson, dataChangeFiles, "data_change_response")
        }
        
        if (complexityFiles.isNotEmpty()) {
            parseMetricsFromFiles(categoryJson, complexityFiles, "code_complexity")
        }
        
        json.put(categoryName, categoryJson)
    }
    
    /**
     * Извлекает метрики из файлов и добавляет в JSON
     */
    private fun parseMetricsFromFiles(json: JSONObject, files: List<File>, metricType: String) {
        val metricJson = JSONObject()
        
        files.forEach { file ->
            try {
                val content = file.readText()
                
                // Извлекаем числовые метрики из файла
                // (простая имплементация, предполагает определенный формат)
                val numbers = "\\d+(\\.\\d+)?".toRegex().findAll(content)
                    .map { it.value.toDoubleOrNull() ?: 0.0 }
                    .toList()
                
                if (numbers.isNotEmpty()) {
                    metricJson.put("min", numbers.minOrNull() ?: 0)
                    metricJson.put("max", numbers.maxOrNull() ?: 0)
                    metricJson.put("avg", numbers.average())
                    metricJson.put("count", numbers.size)
                }
                
                // Если это JSON-файл, пытаемся извлечь данные напрямую
                if (file.name.endsWith(".json")) {
                    try {
                        val fileJson = JSONObject(content)
                        metricJson.put("details", fileJson)
                    } catch (e: Exception) {
                        // Не JSON, игнорируем
                    }
                }
                
                // Сохраняем исходные данные
                metricJson.put("source_file", file.name)
                
            } catch (e: Exception) {
                println("Error parsing metrics from ${file.name}: ${e.message}")
            }
        }
        
        json.put(metricType, metricJson)
    }
    
    /**
     * Создает человекочитаемый отчет из JSON
     */
    private fun createReadableReport(json: JSONObject): String {
        val sb = StringBuilder()
        
        sb.appendLine("=== ANDROID UI BENCHMARK SUMMARY ===")
        sb.appendLine("Timestamp: ${json.getLong("timestamp")}")
        sb.appendLine("Device: ${json.getString("device_model")}")
        sb.appendLine("Android: ${json.getString("android_version")}")
        sb.appendLine()
        
        // Добавляем данные для каждого типа UI
        arrayOf("compose", "xml", "bdui").forEach { ui ->
            if (json.has(ui)) {
                val uiJson = json.getJSONObject(ui)
                
                sb.appendLine("=== ${ui.uppercase()} UI METRICS ===")
                
                // Стартовое время
                if (uiJson.has("startup_time")) {
                    val startupJson = uiJson.getJSONObject("startup_time")
                    sb.appendLine("Startup Time:")
                    sb.appendLine("  Avg: ${startupJson.optDouble("avg", 0.0)} ms")
                    sb.appendLine("  Min: ${startupJson.optDouble("min", 0.0)} ms")
                    sb.appendLine("  Max: ${startupJson.optDouble("max", 0.0)} ms")
                    
                    // Добавляем информацию о разных типах старта
                    if (uiJson.has("cold_startup")) {
                        val coldJson = uiJson.getJSONObject("cold_startup")
                        sb.appendLine("  Cold Start: ${coldJson.optDouble("avg", 0.0)} ms")
                    }
                    
                    if (uiJson.has("warm_startup")) {
                        val warmJson = uiJson.getJSONObject("warm_startup")
                        sb.appendLine("  Warm Start: ${warmJson.optDouble("avg", 0.0)} ms")
                    }
                    
                    if (uiJson.has("hot_startup")) {
                        val hotJson = uiJson.getJSONObject("hot_startup")
                        sb.appendLine("  Hot Start: ${hotJson.optDouble("avg", 0.0)} ms")
                    }
                    
                    sb.appendLine()
                }
                
                // Использование памяти
                if (uiJson.has("memory_usage")) {
                    val memoryJson = uiJson.getJSONObject("memory_usage")
                    sb.appendLine("Memory Usage:")
                    sb.appendLine("  Avg: ${memoryJson.optDouble("avg", 0.0)} MB")
                    sb.appendLine("  Min: ${memoryJson.optDouble("min", 0.0)} MB")
                    sb.appendLine("  Max: ${memoryJson.optDouble("max", 0.0)} MB")
                    sb.appendLine()
                }
                
                // Прокрутка списков
                if (uiJson.has("scrolling_performance")) {
                    val scrollJson = uiJson.getJSONObject("scrolling_performance")
                    sb.appendLine("Scrolling Performance:")
                    sb.appendLine("  Avg Time: ${scrollJson.optDouble("avg", 0.0)} ms")
                    sb.appendLine()
                }
                
                // Анимации
                if (uiJson.has("animation_performance")) {
                    val animJson = uiJson.getJSONObject("animation_performance")
                    sb.appendLine("Animation Performance:")
                    sb.appendLine("  Avg Time: ${animJson.optDouble("avg", 0.0)} ms")
                    sb.appendLine()
                }
                
                // Отклик на изменение данных
                if (uiJson.has("data_change_response")) {
                    val dataJson = uiJson.getJSONObject("data_change_response")
                    sb.appendLine("Data Change Response:")
                    sb.appendLine("  Avg Time: ${dataJson.optDouble("avg", 0.0)} ms")
                    sb.appendLine()
                }
                
                // Сложность кода
                if (uiJson.has("code_complexity")) {
                    val complexityJson = uiJson.getJSONObject("code_complexity")
                    sb.appendLine("Code Complexity:")
                    sb.appendLine("  Lines of Code: ${complexityJson.optDouble("lines", 0.0)}")
                    sb.appendLine("  Cyclomatic Complexity: ${complexityJson.optDouble("complexity", 0.0)}")
                    sb.appendLine()
                }
                
                sb.appendLine()
            }
        }
        
        sb.appendLine("=== END OF REPORT ===")
        return sb.toString()
    }
}
