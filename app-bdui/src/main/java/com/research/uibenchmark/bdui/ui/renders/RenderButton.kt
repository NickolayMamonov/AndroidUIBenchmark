package com.research.uibenchmark.bdui.ui.renders

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.research.uibenchmark.bdui.ui.utils.padding
import com.research.uibenchmark.bdui.ui.utils.parseColor
import com.research.uibenchmark.bdui.ui.utils.toModifierPadding
import com.research.uibenchmark.bdui.ui.utils.toPaddingValues
import com.research.uibenchmark.bdui.utils.UIComponentWrapper

@Composable
fun RenderButton(
    component: UIComponentWrapper,
    onNavigate: (String) -> Unit,
    onApiCall: (String, Map<String, String>?) -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonText = component.text ?: "Button"
    val backgroundColor =
        component.backgroundColor?.let { parseColor(it) } ?: MaterialTheme.colorScheme.primary
    val textColor = component.textColor?.let { parseColor(it) } ?: Color.White
    val cornerRadius = component.cornerRadius ?: 4
    val enabled = component.enabled

    Button(
        onClick = {
            component.action?.let { action ->
                when (action.type) {
                    "navigate" -> action.url?.let { onNavigate(it) }
                    "api_call" -> onApiCall(action.url ?: "", action.payload)
                    "toggle" -> {
                        // Handle toggle action
                        action.payload?.let { onApiCall("toggle", it) }
                    }
                }
            }
        },
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
        ),
        shape = RoundedCornerShape(cornerRadius.dp),
        modifier = modifier
            .padding(component.margin.toModifierPadding())
            .padding(component.padding.toPaddingValues())
    ) {
        Text(text = buttonText)
    }
}