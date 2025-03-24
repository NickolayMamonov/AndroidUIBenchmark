package com.research.uibenchmark.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Контейнерный компонент для группировки других компонентов
 */
@Serializable
@SerialName("container")
data class ContainerComponent(
    override val id: String,
    override val type: String = "container",
    val orientation: String = "vertical", // vertical или horizontal
    val components: List<UIComponent>,
    val padding: Padding? = null,
    val margin: Margin? = null,
    val background: String? = null
) : UIComponent()