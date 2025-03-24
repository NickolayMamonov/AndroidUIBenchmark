package com.research.uibenchmark.bdui.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import com.research.uibenchmark.bdui.model.Margin

fun Margin?.toModifierPadding(): PaddingValues {
    if (this == null) return PaddingValues(0.dp)
    return PaddingValues(
        start = left.dp,
        top = top.dp,
        end = right.dp,
        bottom = bottom.dp
    )
}