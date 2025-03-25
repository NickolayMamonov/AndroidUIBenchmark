package com.research.uibenchmark

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicInteger

/**
 * Класс для анализа сложности кода в разных типах UI реализаций
 * Не является бенчмарком производительности, а скорее статистическим анализатором
 */
class CodeComplexityAnalyzer {

    /**
     * Рассчитывает статистику по кодовой базе
     * @param rootPath корневой путь проекта
     * @param moduleName имя модуля
     * @return метрики сложности кода
     */
    fun analyzeCodebase(rootPath: String, moduleName: String): CodeComplexityMetrics {
        val moduleDir = File("$rootPath/$moduleName")
        if (!moduleDir.exists() || !moduleDir.isDirectory) {
            throw IllegalArgumentException("Module directory does not exist: ${moduleDir.absolutePath}")
        }
        
        val sourceFiles = collectSourceFiles(moduleDir)
        val totalLinesOfCode = countTotalLinesOfCode(sourceFiles)
        val cyclomaticComplexity = estimateCyclomaticComplexity(sourceFiles)
        val xmlFiles = countXmlLayoutFiles(moduleDir)
        
        return CodeComplexityMetrics(
            moduleName = moduleName,
            totalFiles = sourceFiles.size,
            totalLinesOfCode = totalLinesOfCode,
            estimatedCyclomaticComplexity = cyclomaticComplexity,
            layoutFilesCount = xmlFiles
        )
    }
    
    /**
     * Собирает все исходные файлы в директории
     */
    private fun collectSourceFiles(dir: File): List<File> {
        val result = mutableListOf<File>()
        
        dir.walkTopDown()
            .filter { it.isFile && isSourceFile(it) }
            .forEach { result.add(it) }
        
        return result
    }
    
    /**
     * Является ли файл исходным кодом
     */
    private fun isSourceFile(file: File): Boolean {
        val extensions = setOf(".kt", ".java", ".xml")
        return extensions.any { file.name.endsWith(it) }
    }
    
    /**
     * Подсчитывает общее количество строк кода
     */
    private fun countTotalLinesOfCode(files: List<File>): Int {
        var totalLines = 0
        
        files.forEach { file ->
            try {
                val lines = Files.readAllLines(Paths.get(file.absolutePath))
                
                // Отфильтровываем пустые строки и комментарии
                val nonEmptyNonCommentLines = lines.count { line ->
                    val trimmed = line.trim()
                    trimmed.isNotEmpty() && 
                        !trimmed.startsWith("//") && 
                        !trimmed.startsWith("/*") && 
                        !trimmed.startsWith("*") && 
                        !trimmed.startsWith("*/") &&
                        !trimmed.startsWith("<!--") &&
                        !trimmed.startsWith("-->")
                }
                
                totalLines += nonEmptyNonCommentLines
            } catch (e: Exception) {
                println("Error counting lines in file ${file.absolutePath}: ${e.message}")
            }
        }
        
        return totalLines
    }
    
    /**
     * Оценивает цикломатическую сложность кода
     * (упрощенная приблизительная формула)
     */
    private fun estimateCyclomaticComplexity(files: List<File>): Int {
        val complexity = AtomicInteger(0)
        
        files.forEach { file ->
            try {
                if (file.extension == "kt" || file.extension == "java") {
                    val content = file.readText()
                    
                    // Подсчитываем конструкции, увеличивающие цикломатическую сложность
                    val ifCount = "\\bif\\b".toRegex().findAll(content).count()
                    val forCount = "\\bfor\\b".toRegex().findAll(content).count()
                    val whileCount = "\\bwhile\\b".toRegex().findAll(content).count()
                    val catchCount = "\\bcatch\\b".toRegex().findAll(content).count()
                    val whenCount = "\\bwhen\\b".toRegex().findAll(content).count() // для Kotlin
                    val switchCount = "\\bswitch\\b".toRegex().findAll(content).count() // для Java
                    val andOperatorCount = "&&".toRegex().findAll(content).count()
                    val orOperatorCount = "\\|\\|".toRegex().findAll(content).count()
                    
                    // Базовая сложность метода = 1, плюс сложность от каждой конструкции
                    val methodCount = "\\bfun\\b|\\bprivate|public|protected\\s+.*\\(".toRegex().findAll(content).count()
                    
                    val fileComplexity = methodCount + ifCount + forCount + whileCount + catchCount + 
                            whenCount + switchCount + andOperatorCount + orOperatorCount
                    
                    complexity.addAndGet(fileComplexity)
                }
            } catch (e: Exception) {
                println("Error analyzing complexity in file ${file.absolutePath}: ${e.message}")
            }
        }
        
        return complexity.get()
    }
    
    /**
     * Подсчитывает количество XML-файлов разметки
     */
    private fun countXmlLayoutFiles(dir: File): Int {
        return dir.walkTopDown()
            .filter { it.isFile && it.name.endsWith(".xml") && it.path.contains("layout") }
            .count()
    }
}

/**
 * Данные о сложности кода
 */
data class CodeComplexityMetrics(
    val moduleName: String,
    val totalFiles: Int,
    val totalLinesOfCode: Int,
    val estimatedCyclomaticComplexity: Int,
    val layoutFilesCount: Int
) {
    /**
     * Рассчитывает средний показатель сложности на файл
     */
    val complexityPerFile: Double
        get() = if (totalFiles > 0) estimatedCyclomaticComplexity.toDouble() / totalFiles else 0.0
    
    /**
     * Возвращает отчет по метрикам в виде строки
     */
    fun generateReport(): String {
        return """
        |=== Code Complexity Report for $moduleName ===
        |Total source files: $totalFiles
        |Total lines of code: $totalLinesOfCode
        |XML layout files: $layoutFilesCount
        |Estimated cyclomatic complexity: $estimatedCyclomaticComplexity
        |Average complexity per file: ${"%.2f".format(complexityPerFile)}
        |===================================================
        """.trimMargin()
    }
}
