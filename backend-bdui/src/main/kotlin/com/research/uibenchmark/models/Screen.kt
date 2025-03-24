package com.research.uibenchmark.models

import kotlinx.serialization.Serializable

/**
 * Класс для представления полного экрана
 */
@Serializable
data class Screen(
    val id: String,
    val title: String? = null,
    val rootComponent: ContainerComponent,
    val toolbarColor: String? = null,
    val backgroundColor: String? = null,
    val statusBarColor: String? = null
)