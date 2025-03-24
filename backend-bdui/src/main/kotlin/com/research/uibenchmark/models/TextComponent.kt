package com.research.uibenchmark.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Компонент текста
 */
@Serializable
@SerialName("text")
data class TextComponent(
    override val id: String,
    override val type: String = "text",
    val text: String,
    val textSize: Int? = null,
    val textColor: String? = null,
    val fontWeight: String? = null, // normal, bold, etc.
    val textAlign: String? = null, // left, center, right
    val padding: Padding? = null,
    val margin: Margin? = null
) : UIComponent()