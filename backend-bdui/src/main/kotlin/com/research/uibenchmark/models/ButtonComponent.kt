package com.research.uibenchmark.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("button")
data class ButtonComponent(
    override val id: String,
    override val type: String = "button",
    val text: String,
    val action: Action,
    val textColor: String? = null,
    val backgroundColor: String? = null,
    val cornerRadius: Int? = null,
    val padding: Padding? = null,
    val margin: Margin? = null,
    val enabled: Boolean = true
) : UIComponent()