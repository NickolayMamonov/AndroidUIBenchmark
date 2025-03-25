//package com.research.uibenchmark.bdui.renderer
//
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import com.research.uibenchmark.bdui.model.UIComponentWrapper
//import com.research.uibenchmark.bdui.renderer.UIUtils.parseColor
//import com.research.uibenchmark.bdui.renderer.UIUtils.toModifierPadding
//import com.research.uibenchmark.bdui.renderer.UIUtils.toPaddingValues
//
//private const val TAG = "RenderContainer"
//
///**
// * Компонент для отображения контейнера с вложенными элементами
// */
//@Composable
//fun RenderContainer(
//    component: UIComponentWrapper?,
//    onNavigate: (String) -> Unit,
//    onApiCall: (String, Map<String, String>?) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    if (component == null) {
//        Log.e(TAG, "Container component is null")
//        ErrorComponent("Контейнер отсутствует", modifier)
//        return
//    }
//
//    // Получаем все необходимые свойства
//    val isVertical = component.orientation != "horizontal"
//    val backgroundColor = component.background?.let { parseColor(it) }
//    val padding = component.padding?.toPaddingValues() ?: PaddingValues(0.dp)
//    val components = component.components
//
//    // Логирование для отладки
//    Log.d(TAG, "Container: id=${component.id}, orientation=${if (isVertical) "vertical" else "horizontal"}")
//
//    // Создаем модификатор с применением стилей
//    val containerModifier = modifier
//        .padding(component.margin.toModifierPadding())
//        .let { mod ->
//            // Добавляем фон, если указан
//            backgroundColor?.let { bgColor ->
//                mod.background(bgColor, shape = if (component.cornerRadius != null) RoundedCornerShape(component.cornerRadius.dp) else RoundedCornerShape(0.dp))
//                   .clip(if (component.cornerRadius != null) RoundedCornerShape(component.cornerRadius.dp) else RoundedCornerShape(0.dp))
//            } ?: mod
//        }
//        .padding(padding)
//
//    // Проверяем наличие дочерних компонентов
//    if (components.isNullOrEmpty()) {
//        Log.w(TAG, "Container has no components: ${component.id}")
//        // Отображаем пустой стилизованный контейнер
//        if (isVertical) {
//            Column(modifier = containerModifier) {
//                // Пустой контейнер
//            }
//        } else {
//            Row(modifier = containerModifier) {
//                // Пустой контейнер
//            }
//        }
//        return
//    }
//
//    // Рендерим дочерние компоненты в зависимости от ориентации
//    if (isVertical) {
//        Column(modifier = containerModifier) {
//            components.forEachIndexed { index, childComponent ->
//                if (childComponent != null) {
//                    RenderComponent(
//                        component = childComponent,
//                        onNavigate = onNavigate,
//                        onApiCall = onApiCall,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                } else {
//                    Log.e(TAG, "Child component $index is null in container: ${component.id}")
//                    // Отображаем заглушку для отсутствующего компонента
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp)
//                            .background(Color.LightGray.copy(alpha = 0.3f))
//                            .padding(16.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "Отсутствующий компонент",
//                            color = MaterialTheme.colorScheme.error
//                        )
//                    }
//                }
//            }
//        }
//    } else {
//        Row(modifier = containerModifier) {
//            components.forEachIndexed { index, childComponent ->
//                if (childComponent != null) {
//                    RenderComponent(
//                        component = childComponent,
//                        onNavigate = onNavigate,
//                        onApiCall = onApiCall,
//                        modifier = Modifier.weight(1f)
//                    )
//                } else {
//                    Log.e(TAG, "Child component $index is null in container: ${component.id}")
//                    // Отображаем заглушку для отсутствующего компонента
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(8.dp)
//                            .background(Color.LightGray.copy(alpha = 0.3f))
//                            .padding(16.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "Отсутствующий компонент",
//                            color = MaterialTheme.colorScheme.error
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
