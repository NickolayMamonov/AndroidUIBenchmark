//package com.research.uibenchmark.bdui.renderer
//
//import android.util.Log
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import com.research.uibenchmark.R
//import com.research.uibenchmark.bdui.model.UIComponentWrapper
//import com.research.uibenchmark.bdui.renderer.UIUtils.toModifierPadding
//import com.research.uibenchmark.bdui.renderer.UIUtils.toPaddingValues
//
//private const val TAG = "RenderImage"
//
///**
// * Компонент для отображения изображения
// */
//@Composable
//fun RenderImage(
//    component: UIComponentWrapper?,
//    modifier: Modifier = Modifier
//) {
//    if (component == null) {
//        Log.e(TAG, "Image component is null")
//        ErrorComponent("Компонент изображения отсутствует", modifier)
//        return
//    }
//
//    // Получаем свойства
//    val cornerRadius = component.cornerRadius ?: 0
//    val contentScaleParam = component.contentScale ?: "crop"
//
//    // Определяем ContentScale
//    val contentScale = when (contentScaleParam) {
//        "fit" -> ContentScale.Fit
//        "fill" -> ContentScale.FillBounds
//        "inside" -> ContentScale.Inside
//        else -> ContentScale.Crop
//    }
//
//    // Применяем стили
//    val imageModifier = modifier
//        .padding(component.margin.toModifierPadding())
//        .padding(component.padding.toPaddingValues())
//        .let { mod ->
//            // Применяем указанные размеры, если они есть
//            var result = mod
//            if (component.width != null) {
//                result = result.width(component.width.dp)
//            }
//            if (component.height != null) {
//                result = result.height(component.height.dp)
//            }
//            result
//        }
//        // Применяем скругление углов
//        .clip(RoundedCornerShape(cornerRadius.dp))
//
//    Box(
//        modifier = imageModifier,
//        contentAlignment = Alignment.Center
//    ) {
//        // В реальном приложении здесь бы загружалось изображение по URL
//        // В нашем примере используем ресурс по умолчанию
//        Image(
//            painter = painterResource(id = R.drawable.ic_launcher_foreground),
//            contentDescription = "Image",
//            contentScale = contentScale,
//            modifier = Modifier.matchParentSize()
//        )
//    }
//}
