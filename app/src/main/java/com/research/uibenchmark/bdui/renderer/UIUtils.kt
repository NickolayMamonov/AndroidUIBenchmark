package com.research.uibenchmark.bdui.renderer

import android.graphics.Color as AndroidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.research.uibenchmark.bdui.model.Margin
import com.research.uibenchmark.bdui.model.Padding

/**
 * Утилиты для работы с BDUI компонентами
 */
object UIUtils {
    
    /**
     * Преобразует цветовую строку в Compose Color
     */
    fun parseColor(colorString: String?): Color? {
        if (colorString == null) return null
        
        return try {
            Color(AndroidColor.parseColor(colorString))
        } catch (e: Exception) {
            Color.Black
        }
    }
    
    /**
     * Преобразует Padding в отступы для Compose
     */
    fun Padding?.toPaddingValues(): androidx.compose.foundation.layout.PaddingValues {
        if (this == null) return androidx.compose.foundation.layout.PaddingValues(0.dp)
        
        return androidx.compose.foundation.layout.PaddingValues(
            start = start.dp,
            top = top.dp,
            end = end.dp,
            bottom = bottom.dp
        )
    }
    
    /**
     * Преобразует Margin в модификатор отступов для Compose
     */
    fun Margin?.toModifierPadding(): androidx.compose.foundation.layout.PaddingValues {
        if (this == null) return androidx.compose.foundation.layout.PaddingValues(0.dp)
        
        return androidx.compose.foundation.layout.PaddingValues(
            start = start.dp,
            top = top.dp,
            end = end.dp,
            bottom = bottom.dp
        )
    }
}
