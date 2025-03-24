package com.research.uibenchmark.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Компонент ввода текста
 */
@Serializable
@SerialName("input")
data class InputComponent(
    override val id: String,
    override val type: String = "input",
    val hint: String? = null,
    val initialValue: String? = null,
    val inputType: String = "text", // text, number, email, password
    val maxLength: Int? = null,
    val backgroundColor: String? = null,
    val cornerRadius: Int? = null,
    val padding: Padding? = null,
    val margin: Margin? = null
) : UIComponent()