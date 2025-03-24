package com.research.uibenchmark.bdui.renderer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.research.uibenchmark.bdui.model.Screen
import com.research.uibenchmark.bdui.renderer.UIUtils.parseColor

private const val TAG = "RenderScreen"

/**
 * Компонент для отображения экрана BDUI
 */
@Composable
fun RenderScreen(
    screen: Screen?,
    onNavigate: (String) -> Unit = {},
    onApiCall: (String, Map<String, String>?) -> Unit = { _, _ -> }
) {
    // Проверка на null
    if (screen == null) {
        Log.e(TAG, "Screen is null")
        ErrorContent("Screen data is missing")
        return
    }
    
    // Настройка цвета фона
    val backgroundColor = screen.backgroundColor?.let { parseColor(it) } ?: MaterialTheme.colorScheme.background
    
    // Используем Scaffold для повышения совместимости с Material Design
    Scaffold(
        content = { innerPadding ->
            // Создаем Surface с указанным цветом фона
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(backgroundColor),
                color = backgroundColor
            ) {
                // Проверяем наличие корневого компонента
                if (screen.rootComponent == null) {
                    Log.e(TAG, "Root component is null for screen: ${screen.id}")
                    ErrorContent("Screen has no content")
                    return@Surface
                }
                
                // Рендерим корневой компонент
                RenderComponent(
                    component = screen.rootComponent,
                    onNavigate = onNavigate,
                    onApiCall = onApiCall,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    )
}

/**
 * Компонент для отображения ошибок
 */
@Composable
private fun ErrorContent(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: $message",
            color = Color.Red,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
