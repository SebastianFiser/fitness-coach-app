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
import androidx.compose.ui.draw.clip

@Composable
fun ComingSoonOverlay(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .drawWithContent {
            drawContent()
            val spacing = 80f
            val lineCount = ((size.width / spacing).toInt() + 1) * 2
            for (i in 0..lineCount) {
                val middle = lineCount / 2
                val offset: Float 
                if (i < middle) {
                    offset = -spacing * (middle - i)
                } else {
                    offset = spacing * (i - middle)
                }
                drawLine(
                    color = Color.Red.copy(alpha = 0.5f),
                    strokeWidth = 5f,
                    start = Offset( 0f + offset, 0f ),
                    end = Offset( size.width + offset , size.height ),
                )
            }
        }
        .border(
            width = 5.dp,
            color = Color.Red.copy(alpha = 0.7f),
            shape = RoundedCornerShape(8.dp)
        )
    ) {
        content()
    }
}
