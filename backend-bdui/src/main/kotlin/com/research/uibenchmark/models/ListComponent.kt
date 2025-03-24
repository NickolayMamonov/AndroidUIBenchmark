package com.research.uibenchmark.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("list")
data class ListComponent(
    override val id: String,
    override val type: String = "list",
    val items: List<UIComponent>,
    val orientation: String = "vertical",
    val padding: Padding? = null,
    val margin: Margin? = null,
    val dividerEnabled: Boolean = false,
    val dividerColor: String? = null
) : UIComponent()