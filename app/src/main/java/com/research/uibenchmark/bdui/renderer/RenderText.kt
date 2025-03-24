package com.research.uibenchmark.bdui.renderer

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.research.uibenchmark.bdui.model.UIComponentWrapper
import com.research.uibenchmark.bdui.renderer.UIUtils.parseColor
import com.research.uibenchmark.bdui.renderer.UIUtils.toModifierPadding
import com.research.uibenchmark.bdui.renderer.UIUtils.toPaddingValues

private const val TAG = "RenderText"

/**
 * Компонент для отображения текста
 */
@Composable
fun RenderText(
    component: UIComponentWrapper?,
    modifier: Modifier = Modifier
) {
    if (component == null) {
        Log.e(TAG, "Text component is null")
        ErrorComponent("Текстовый компонент отсутствует", modifier)
        return
    }
    
    // Получаем свойства
    val text = component.text ?: ""
    val textColor = component.textColor?.let { UIUtils.parseColor(it) } ?: MaterialTheme.colorScheme.onBackground
    
    // Определяем жирность шрифта
    val fontWeight = when (component.fontWeight) {
        "bold" -> FontWeight.Bold
        "medium" -> FontWeight.Medium
        "light" -> FontWeight.Light
        else -> FontWeight.Normal
    }
    
    // Определяем выравнивание текста
    val textAlign = when (component.textAlign) {
        "center" -> TextAlign.Center
        "end", "right" -> TextAlign.End
        else -> TextAlign.Start
    }
    
    // Определяем размер текста
    val textStyle = when (component.textSize) {
        null -> MaterialTheme.typography.bodyMedium
        in 0..12 -> MaterialTheme.typography.bodySmall
        in 13..16 -> MaterialTheme.typography.bodyMedium
        in 17..20 -> MaterialTheme.typography.bodyLarge
        in 21..24 -> MaterialTheme.typography.titleMedium
        else -> MaterialTheme.typography.titleLarge
    }
    
    // Применяем стили
    val textModifier = modifier
        .padding(component.margin.toModifierPadding())
        .padding(component.padding.toPaddingValues())
    
    Box(
        modifier = textModifier,
        contentAlignment = when (textAlign) {
            TextAlign.Center -> Alignment.Center
            TextAlign.End -> Alignment.CenterEnd
            else -> Alignment.CenterStart
        }
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = fontWeight,
            textAlign = textAlign,
            style = textStyle
        )
    }
}
