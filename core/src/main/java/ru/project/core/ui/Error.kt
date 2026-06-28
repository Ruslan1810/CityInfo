package ru.project.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.project.core.R
import ru.project.core.utils.toErrorInfo

@Composable
fun ErrorScreenContent(
    modifier: Modifier = Modifier,
    throwable: Throwable? = null,
    customTitle: String? = null,
    customMessage: String? = null,
    onClick: () -> Unit,
) {

    val (title, message) = when {
        customTitle != null && customMessage != null -> customTitle to customMessage
        throwable != null -> throwable.toErrorInfo()
        else -> "Ошибка" to "Что-то пошло не так"
    }
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_processing_loader),
                contentDescription = null,
                modifier = Modifier.padding(bottom = 24.dp),
                tint = Color.Unspecified,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 23.sp,
                    lineHeight = 26.sp,
                    letterSpacing = 0.sp,
                ),
                color = Color(0xFF25222B),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            Text(
                text = message,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    letterSpacing = 0.sp,
                ),
                color = Color(0xFF777381),
                textAlign = TextAlign.Center,
            )
        }

        ButtonText(
            text = "Попробовать снова",
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp),
        )
    }
}

