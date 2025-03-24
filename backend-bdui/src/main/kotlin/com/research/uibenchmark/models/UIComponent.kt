package com.research.uibenchmark.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator("componentType")
sealed class UIComponent {
    abstract val id: String
    abstract val type: String
}