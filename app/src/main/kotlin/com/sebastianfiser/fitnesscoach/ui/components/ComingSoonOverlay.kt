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
            val spacing = 40f
            val lineCount = (size.width / spacing).toInt() + 1
            for (i in 0..lineCount) {
                val offset: Float = spacing * i
                val minWidth = size.width * -1
                val minHeight = size.height * -1
                drawLine(
                    color = Color.Red.copy(alpha = 0.5f),
                    strokeWidth = 5f,
                    start = Offset( minWidth + offset, minHeight ),
                    end = Offset( size.width + offset , size.height ),
                )
            }
        }
        .border(
            width = 5.dp,
            color = Color.Red,
            shape = RoundedCornerShape(8.dp)
        )
    ) {
        content()
    }
}
