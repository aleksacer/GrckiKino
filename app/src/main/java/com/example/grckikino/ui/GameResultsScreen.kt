package com.example.grckikino.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import com.example.grckikino.R
import com.example.grckikino.formatAsDateTime
import com.example.grckikino.model.Game
import com.example.grckikino.model.WinningNumbers
import com.example.grckikino.ui.theme.GrckiKinoTheme

@Composable
fun GameResultsScreen(
    results: LazyPagingItems<Game>
) {
    LazyColumn {
        items(results.itemCount) { index ->
            results[index]?.let { result ->
                SingleGameResult(result = result)
            }
        }
    }
}

@Composable
fun SingleGameResult(
    result: Game
) {
    Column {
        GameResultsHeader(timeOfGame = result.drawTime, drawId = result.drawId)
        GameNumbers(numbers = result.winningNumbers.list)
    }
}

@Composable
fun GameResultsHeader(
    timeOfGame: Long,
    drawId: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.DarkGray)
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp),
            text = stringResource(id = R.string.time_of_game_expanded, timeOfGame.formatAsDateTime(), drawId),
            style = TextStyle(
                color = Color.LightGray,
                fontSize = 12.sp
            )
        )
    }
}

@Composable
fun GameNumbers(
    numbers: List<Int>
) {
    Column {
        numbers.chunked(7).forEach { numbersRow ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.DarkGray)
            ) {
                numbersRow.forEach { number ->
                    Ball(
                        number = number,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        false,
                        { _ -> },
                        isSelected = true
                    )
                }

                val spacerSize = 7 - numbersRow.size
                if (spacerSize > 0) {
                    Spacer(
                        modifier = Modifier
                            .weight(spacerSize.toFloat())
                    )
                }
            }
        }
    }
}
