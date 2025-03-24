package com.research.uibenchmark.bdui.ui.renders

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.research.uibenchmark.bdui.ui.utils.padding
import com.research.uibenchmark.bdui.ui.utils.parseColor
import com.research.uibenchmark.bdui.ui.utils.toModifierPadding
import com.research.uibenchmark.bdui.ui.utils.toPaddingValues
import com.research.uibenchmark.bdui.utils.UIComponentWrapper

@Composable
fun RenderList(
    component: UIComponentWrapper,
    onNavigate: (String) -> Unit,
    onApiCall: (String, Map<String, String>?) -> Unit,
    modifier: Modifier = Modifier
) {
    val isVertical = component.orientation != "horizontal"
    val items = component.items ?: emptyList()
    val dividerEnabled = component.dividerEnabled
    val dividerColor = component.dividerColor?.let { parseColor(it) } ?: MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

    val listModifier = modifier
        .padding(component.margin.toModifierPadding())
        .padding(component.padding.toPaddingValues())

    if (isVertical) {
        LazyColumn(
            modifier = listModifier
        ) {
            items(items) { item ->
                RenderComponent(
                    component = item,
                    onNavigate = onNavigate,
                    onApiCall = onApiCall,
                    modifier = Modifier.fillMaxWidth()
                )

                if (dividerEnabled && item != items.last()) {
                    HorizontalDivider(color = dividerColor)
                }
            }
        }
    } else {
        LazyRow(
            modifier = listModifier
        ) {
            items(items) { item ->
                RenderComponent(
                    component = item,
                    onNavigate = onNavigate,
                    onApiCall = onApiCall,
                    modifier = Modifier.wrapContentWidth()
                )

                if (dividerEnabled && item != items.last()) {
                    HorizontalDivider(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight(),
                        color = dividerColor
                    )
                }
            }
        }
    }
}