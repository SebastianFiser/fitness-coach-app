package com.sebastianfiser.fitnesscoach.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun ComingSoonOverlay(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier
        .drawWithContent {
            drawContent()
            val spacing = size.width / 10
            for (i in 0..5) {
                val offset: Float = spacing * i
                drawLine(
                    color = Color.Red.copy(alpha = 0.5f),
                    strokeWidth = 5f,
                    start = Offset(0f + offset, 0f ),
                    end = Offset( size.width + offset , size.height ),
                )
            }
        }
        .border(
            width = 2.dp,
            color = Color.Red,
            shape = RoundedCornerShape(8.dp)
        )
    ) {
        content()
    }
}
