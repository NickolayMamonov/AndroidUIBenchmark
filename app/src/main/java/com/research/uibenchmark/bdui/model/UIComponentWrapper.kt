package com.research.uibenchmark.bdui.model

import android.util.Log

/**
 * Класс-обертка для десериализации UI компонентов
 */
class UIComponentWrapper(
    // Базовые свойства компонента
    val id: String? = null,
    val type: String? = null,
    // Поле для дискриминатора
    val componentType: String? = null,
    
    // Свойства контейнера
    val orientation: String? = null,
    val components: List<UIComponentWrapper>? = null,
    val background: String? = null,
    
    // Свойства текста
    val text: String? = null,
    val textSize: Int? = null,
    val textColor: String? = null,
    val fontWeight: String? = null,
    val textAlign: String? = null,
    
    // Свойства кнопки
    val action: Action? = null,
    val backgroundColor: String? = null,
    val cornerRadius: Int? = null,
    val enabled: Boolean = true,
    
    // Свойства изображения
    val url: String? = null,
    val contentScale: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    
    // Свойства списка
    val items: List<UIComponentWrapper>? = null,
    val dividerEnabled: Boolean = false,
    val dividerColor: String? = null,
    
    // Общие свойства
    val padding: Padding? = null,
    val margin: Margin? = null
) {
    
    private val TAG = "UIComponentWrapper"
    
    /**
     * Определяет тип компонента
     */
    fun determineComponentType(): String {
        // Приоритет за явно указанными типами
        if (!componentType.isNullOrEmpty()) {
            return componentType
        }
        
        if (!type.isNullOrEmpty()) {
            return type
        }
        
        // Определение по ID
        id?.let { id ->
            when {
                id.contains("container") -> return "container"
                id.contains("text") -> return "text"
                id.contains("button") -> return "button"
                id.contains("image") -> return "image"
                id.contains("list") -> return "list"
            }
        }
        
        // Определение по полям
        when {
            components != null -> return "container"
            text != null && action == null -> return "text"
            text != null && action != null -> return "button"
            url != null -> return "image"
            items != null -> return "list"
        }
        
        return "unknown"
    }
    
    /**
     * Преобразует обертку в конкретный компонент
     */
    fun toComponent(): UIComponent? {
        try {
            val safeId = id ?: run {
                Log.e(TAG, "Component has no id")
                "_unknown_${System.currentTimeMillis()}"
            }
            
            val actualType = determineComponentType()
            
            return when (actualType) {
                "container" -> ContainerComponent(
                    id = safeId,
                    type = actualType,
                    orientation = orientation ?: "vertical",
                    components = components ?: emptyList(),
                    background = background,
                    padding = padding,
                    margin = margin
                )
                "text" -> TextComponent(
                    id = safeId,
                    type = actualType,
                    text = text ?: "",
                    textSize = textSize,
                    textColor = textColor,
                    fontWeight = fontWeight,
                    textAlign = textAlign,
                    padding = padding,
                    margin = margin
                )
                "button" -> ButtonComponent(
                    id = safeId,
                    type = actualType,
                    text = text ?: "",
                    action = action ?: Action("none", null, null),
                    textColor = textColor,
                    backgroundColor = backgroundColor,
                    cornerRadius = cornerRadius,
                    padding = padding,
                    margin = margin,
                    enabled = enabled
                )
                "image" -> ImageComponent(
                    id = safeId,
                    type = actualType,
                    url = url ?: "",
                    contentScale = contentScale,
                    width = width,
                    height = height,
                    cornerRadius = cornerRadius,
                    padding = padding,
                    margin = margin
                )
                "list" -> ListComponent(
                    id = safeId,
                    type = actualType,
                    items = items ?: emptyList(),
                    orientation = orientation ?: "vertical",
                    dividerEnabled = dividerEnabled,
                    dividerColor = dividerColor,
                    padding = padding,
                    margin = margin
                )
                else -> {
                    Log.e(TAG, "Unknown component type: $actualType")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error converting component: ${e.message}")
            return null
        }
    }
}
