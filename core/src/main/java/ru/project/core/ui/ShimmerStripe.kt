package ru.project.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer


@Composable
fun ShimmerStripe(
    modifier: Modifier = Modifier,
    itemHeight: Dp = 30.dp,
    barWidthPercent: Float = 0.9f,
    barHeight: Dp = 30.dp,
    barCornerRadius: Dp = 8.dp,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(barWidthPercent)
                    .height(barHeight)
                    .shimmer()
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(barCornerRadius)
                    )
            )
        }
    }
}
