package com.research.uibenchmark

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Тест для анализа сложности кода в разных UI реализациях
 * Это не бенчмарк производительности, а инструмент для статистического анализа кодовой базы
 */
@RunWith(AndroidJUnit4::class)
class CodeComplexityBenchmark {

    @Test
    fun analyzeUIImplementationComplexity() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val rootPath = File(context.applicationInfo.sourceDir).parentFile?.parentFile?.parentFile?.absolutePath
            ?: throw RuntimeException("Could not determine project root path")
        
        val analyzer = CodeComplexityAnalyzer()
        
        // Анализируем каждую реализацию UI
        val composeMetrics = analyzer.analyzeCodebase(rootPath, "app-compose")
        val xmlMetrics = analyzer.analyzeCodebase(rootPath, "app-xml") 
        val bduiMetrics = analyzer.analyzeCodebase(rootPath, "app-bdui")
        
        // Сохраняем результаты анализа
        val report = """
            |=== UI IMPLEMENTATION COMPLEXITY COMPARISON ===
            |
            |${composeMetrics.generateReport()}
            |
            |${xmlMetrics.generateReport()}
            |
            |${bduiMetrics.generateReport()}
            |
            |=== COMPARISON SUMMARY ===
            |
            |Lines of Code:
            |Compose: ${composeMetrics.totalLinesOfCode}
            |XML: ${xmlMetrics.totalLinesOfCode}
            |BDUI: ${bduiMetrics.totalLinesOfCode}
            |
            |Cyclomatic Complexity:
            |Compose: ${composeMetrics.estimatedCyclomaticComplexity}
            |XML: ${xmlMetrics.estimatedCyclomaticComplexity}
            |BDUI: ${bduiMetrics.estimatedCyclomaticComplexity}
            |
            |Layout Files:
            |Compose: ${composeMetrics.layoutFilesCount}
            |XML: ${xmlMetrics.layoutFilesCount}
            |BDUI: ${bduiMetrics.layoutFilesCount}
            |
            |Average Complexity Per File:
            |Compose: ${"%.2f".format(composeMetrics.complexityPerFile)}
            |XML: ${"%.2f".format(xmlMetrics.complexityPerFile)}
            |BDUI: ${"%.2f".format(bduiMetrics.complexityPerFile)}
        """.trimMargin()
        
        // Сохраняем отчет в файл
        val outputFile = File(context.getExternalFilesDir(null), "code_complexity_report.txt")
        outputFile.writeText(report)
        
        println("Code complexity report saved to: ${outputFile.absolutePath}")
    }
}
