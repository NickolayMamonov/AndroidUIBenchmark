package com.research.uibenchmark.bdui.model

import com.google.gson.annotations.SerializedName

// Класс для представления ответа API
data class BDUIResponse(
    // Поле должно быть nullable, так как может прийти null с сервера
    @SerializedName("screen")
    val screen: Screen? = null,

    val version: String = "",
    val timestamp: Long = 0
)