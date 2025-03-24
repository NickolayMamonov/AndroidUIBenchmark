package com.research.uibenchmark.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Компонент изображения
 */
@Serializable
@SerialName("image")
data class ImageComponent(
    override val id: String,
    override val type: String = "image",
    val url: String,
    val contentScale: String? = null, // fit, fill, crop
    val width: Int? = null,
    val height: Int? = null,
    val cornerRadius: Int? = null,
    val padding: Padding? = null,
    val margin: Margin? = null
) : UIComponent()