package com.rwmobi.giphytrending.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconButtonWithToolTip(
    modifier: Modifier = Modifier,
    tooltipText: String,
    painter: Painter,
    onClick: () -> Unit,
) {
    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip {
                Text(tooltipText)
            }
        },
        state = rememberTooltipState(),
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painter,
                contentDescription = tooltipText,
                tint = LocalContentColor.current.copy(alpha = 0.68f),
            )
        }
    }
}
