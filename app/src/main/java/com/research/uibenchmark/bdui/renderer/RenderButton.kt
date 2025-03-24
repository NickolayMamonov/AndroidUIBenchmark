package com.research.uibenchmark.bdui.renderer

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.research.uibenchmark.bdui.model.UIComponentWrapper
import com.research.uibenchmark.bdui.renderer.UIUtils.parseColor
import com.research.uibenchmark.bdui.renderer.UIUtils.toModifierPadding
import com.research.uibenchmark.bdui.renderer.UIUtils.toPaddingValues

private const val TAG = "RenderButton"

/**
 * Компонент для отображения кнопки
 */
@Composable
fun RenderButton(
    component: UIComponentWrapper?,
    onNavigate: (String) -> Unit,
    onApiCall: (String, Map<String, String>?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (component == null) {
        Log.e(TAG, "Button component is null")
        ErrorComponent("Компонент кнопки отсутствует", modifier)
        return
    }
    
    // Получаем свойства
    val text = component.text ?: "Кнопка"
    val action = component.action
    val textColor = component.textColor?.let { parseColor(it) } ?: Color.White
    val backgroundColor = component.backgroundColor?.let { parseColor(it) } 
        ?: MaterialTheme.colorScheme.primary
    val cornerRadius = component.cornerRadius ?: 8
    val enabled = component.enabled
    
    // Применяем стили
    val buttonModifier = modifier
        .padding(component.margin.toModifierPadding())
        .padding(component.padding.toPaddingValues())
    
    // Проверяем, является ли это круглой FAB кнопкой (как "+" в примере)
    if (text == "+" && cornerRadius >= 20) {
        // Это FAB кнопка - стилизуем как FloatingActionButton для соответствия Compose/XML
        FloatingActionButton(
            onClick = {
                if (action != null) {
                    when (action.type) {
                        "navigation" -> {
                            action.url?.let { onNavigate(it) }
                        }
                        "api_call" -> {
                            action.url?.let { onApiCall(it, action.payload) }
                        }
                        else -> {
                            Log.w(TAG, "Unknown action type: ${action.type}")
                        }
                    }
                }
            },
            modifier = buttonModifier,
            containerColor = backgroundColor,
            contentColor = textColor,
            shape = CircleShape
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center
            )
        }
    } else {
        // Обычная кнопка
        Button(
            onClick = {
                if (action != null) {
                    when (action.type) {
                        "navigation" -> {
                            action.url?.let { onNavigate(it) }
                        }
                        "api_call" -> {
                            action.url?.let { onApiCall(it, action.payload) }
                        }
                        else -> {
                            Log.w(TAG, "Unknown action type: ${action.type}")
                        }
                    }
                }
            },
            modifier = buttonModifier,
            enabled = enabled,
            shape = RoundedCornerShape(cornerRadius.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = textColor,
                disabledContainerColor = backgroundColor.copy(alpha = 0.5f),
                disabledContentColor = textColor.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center
            )
        }
    }
}
