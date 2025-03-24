package com.research.uibenchmark.bdui.model

import com.research.uibenchmark.bdui.utils.UIComponentWrapper

// Контейнерный компонент для группировки других компонентов
data class ContainerComponent(
    override val id: String,
    override val type: String,
    val orientation: String,
    val components: List<UIComponentWrapper>,
    val padding: Padding?,
    val margin: Margin?,
    val background: String?
) : UIComponent