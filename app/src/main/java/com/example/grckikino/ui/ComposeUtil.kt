package com.example.grckikino.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grckikino.R


@Composable
fun BallsRow(
    startNumber: Int,
    numberOfBalls: Int,
    selectedBalls: List<Int>,
    onClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        for (i in startNumber until (startNumber + numberOfBalls)) {
            Ball(
                number = i,
                modifier = Modifier
                    .weight(1f),
                selectedBalls.size < 15,
                onClick,
                selectedBalls.contains(i)
            )
        }
    }
}

@Composable
fun Ball(
    number: Int,
    modifier: Modifier = Modifier,
    isClickable: Boolean,
    onClick: (Int) -> Unit,
    isSelected: Boolean
) {

    val borderColor = if (number < 11) {
        Color.Yellow
    } else if (number < 21) {
        Color(234, 135, 30)
    } else if (number < 31) {
        Color.Red
    } else if (number < 41) {
        Color(204, 51, 204)
    } else if (number < 51) {
        Color(51, 0, 114)
    } else if (number < 61) {
        Color(69, 177, 232)
    } else if (number < 71) {
        Color.Green
    } else {
        Color.Blue
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .border(
                BorderStroke(2.dp, Color.Black),
                shape = RectangleShape
            )
            .let {
                if (isClickable || isSelected) {
                    it.clickable {
                        onClick(number)
                    }
                } else {
                    it
                }
            }
            .background(Color.Gray)
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize(0.8f)
                .let {
                    if (isSelected) {
                        it
                            .background(Color.White, CircleShape)
                            .border(
                                BorderStroke(2.dp, borderColor),
                                shape = CircleShape
                            )
                    } else {
                        it.background(Color.Gray, CircleShape)
                    }
                }
        ) {
            Text(
                text = number.toString(),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}

@Composable
fun RowOfQuotes(
    quotes: List<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        quotes.forEach { quote ->
            Quote(quoteText = quote)
        }
    }
}

@Composable
fun RowScope.Quote(
    quoteText: String
) {
    Text(
        modifier = Modifier
            .padding(3.dp)
            .weight(1f),
        text = quoteText,
        style = TextStyle(
            color = Color.LightGray,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    )
}

@Composable
fun SimpleMessageDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(stringResource(id = R.string.ok))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.game_expired))
        },
        text = {
            Text(text = message)
        }
    )
}
