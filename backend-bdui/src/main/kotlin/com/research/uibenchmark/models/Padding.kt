package com.research.uibenchmark.models

import kotlinx.serialization.Serializable

@Serializable
data class Padding(
    val left: Int = 0,
    val top: Int = 0,
    val right: Int = 0,
    val bottom: Int = 0
)