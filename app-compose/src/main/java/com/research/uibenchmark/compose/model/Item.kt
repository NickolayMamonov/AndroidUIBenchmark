package com.research.uibenchmark.compose.model

data class Item(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val timestamp: Long
)
