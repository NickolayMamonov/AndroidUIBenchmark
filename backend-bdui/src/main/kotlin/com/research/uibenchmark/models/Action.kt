package com.research.uibenchmark.models

import kotlinx.serialization.Serializable

@Serializable
data class Action(
    val type: String, // navigate, api_call, toggle
    val url: String? = null,
    val payload: Map<String, String>? = null
)