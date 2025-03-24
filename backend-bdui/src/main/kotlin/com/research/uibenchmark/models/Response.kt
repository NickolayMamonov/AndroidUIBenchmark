package com.research.uibenchmark.models

import kotlinx.serialization.Serializable


/**
 * Класс для представления ответа API
 */
@Serializable
data class BDUIResponse(
    val screen: Screen,
    val version: String = "1.0",
    val timestamp: Long = System.currentTimeMillis()
)