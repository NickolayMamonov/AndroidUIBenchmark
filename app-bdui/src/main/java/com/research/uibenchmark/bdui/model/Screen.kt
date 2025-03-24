package com.research.uibenchmark.bdui.model

import com.google.gson.annotations.SerializedName
import com.research.uibenchmark.bdui.utils.UIComponentWrapper

// Класс для представления полного экрана
data class Screen(
    val id: String,
    val title: String? = null,

    // Поле должно быть nullable, так как может прийти null с сервера
    @SerializedName("rootComponent")
    val rootComponent: UIComponentWrapper? = null,

    val toolbarColor: String? = null,
    val backgroundColor: String? = null,
    val statusBarColor: String? = null
)