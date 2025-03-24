package com.research.uibenchmark.bdui.model

/**
 * Представление экрана в BDUI
 */
data class Screen(
    val id: String,
    val title: String? = null,
    val rootComponent: UIComponentWrapper? = null,
    val toolbarColor: String? = null,
    val backgroundColor: String? = null,
    val statusBarColor: String? = null
)
