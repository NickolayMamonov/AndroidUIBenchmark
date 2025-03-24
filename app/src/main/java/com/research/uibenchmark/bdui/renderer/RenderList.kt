package com.research.uibenchmark.bdui.renderer

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.research.uibenchmark.bdui.model.UIComponentWrapper
import com.research.uibenchmark.bdui.renderer.UIUtils.parseColor
import com.research.uibenchmark.bdui.renderer.UIUtils.toModifierPadding
import com.research.uibenchmark.bdui.renderer.UIUtils.toPaddingValues

private const val TAG = "RenderList"

/**
 * Компонент для отображения списка элементов
 */
@Composable
fun RenderList(
    component: UIComponentWrapper?,
    onNavigate: (String) -> Unit,
    onApiCall: (String, Map<String, String>?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (component == null) {
        Log.e(TAG, "List component is null")
        ErrorComponent("Компонент списка отсутствует", modifier)
        return
    }
    
    // Получаем свойства
    val isVertical = component.orientation != "horizontal"
    val items = component.items ?: emptyList()
    val dividerEnabled = component.dividerEnabled
    val dividerColor = component.dividerColor?.let { parseColor(it) }
    
    // Применяем стили
    val listModifier = modifier
        .padding(component.margin.toModifierPadding())
        .padding(component.padding.toPaddingValues())
    
    Log.d(TAG, "Rendering list with ${items.size} items, orientation=${if (isVertical) "vertical" else "horizontal"}")
    
    // Проверяем наличие элементов
    if (items.isEmpty()) {
        Log.w(TAG, "List has no items: ${component.id}")
        // Отображаем пустой список
        Column(modifier = listModifier) {
            // Пустой список
        }
        return
    }
    
    // Отображаем список в зависимости от ориентации
    if (isVertical) {
        LazyColumn(
            modifier = listModifier,
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(items, key = { it.id ?: "${System.currentTimeMillis()}_${it.hashCode()}" }) { item ->
                RenderComponent(
                    component = item,
                    onNavigate = onNavigate,
                    onApiCall = onApiCall,
                    modifier = Modifier.animateContentSize() // Добавляем анимацию как в Compose
                )
                
                // Добавляем разделитель, если включено
                if (dividerEnabled) {
                    Divider(
                        color = dividerColor ?: androidx.compose.ui.graphics.Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    } else {
        LazyRow(
            modifier = listModifier,
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(items) { item ->
                RenderComponent(
                    component = item,
                    onNavigate = onNavigate,
                    onApiCall = onApiCall,
                    modifier = Modifier.animateContentSize().padding(horizontal = 4.dp)
                )
            }
        }
    }
}
