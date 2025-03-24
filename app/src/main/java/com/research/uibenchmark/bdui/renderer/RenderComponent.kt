package com.research.uibenchmark.bdui.renderer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.research.uibenchmark.bdui.model.UIComponentWrapper

private const val TAG = "RenderComponent"

/**
 * Основной компонент для рендеринга элементов UI
 */
@Composable
fun RenderComponent(
    component: UIComponentWrapper?,
    onNavigate: (String) -> Unit,
    onApiCall: (String, Map<String, String>?) -> Unit,
    modifier: Modifier = Modifier
) {
    // Проверка на null
    if (component == null) {
        Log.e(TAG, "Component is null")
        ErrorComponent("Компонент отсутствует", modifier)
        return
    }
    
    Log.d(TAG, "Rendering component: id=${component.id}, type=${component.componentType ?: component.type}")
    
    // Определяем тип компонента
    val componentType = component.determineComponentType()
    
    // Рендерим компонент в зависимости от его типа
    when (componentType) {
        "container" -> RenderContainer(component, onNavigate, onApiCall, modifier)
        "text" -> RenderText(component, modifier)
        "button" -> RenderButton(component, onNavigate, onApiCall, modifier)
        "image" -> RenderImage(component, modifier)
        "list" -> RenderList(component, onNavigate, onApiCall, modifier)
        else -> {
            Log.e(TAG, "Unknown component type: $componentType")
            ErrorComponent("Неизвестный тип компонента: $componentType", modifier)
        }
    }
}

/**
 * Компонент для отображения ошибок
 */
@Composable
fun ErrorComponent(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .background(Color.Red.copy(alpha = 0.1f))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.Red,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
