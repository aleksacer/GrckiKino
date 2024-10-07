package com.example.grckikino.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grckikino.R
import com.example.grckikino.formatAsTime
import com.example.grckikino.formatAsTimeUntilGame
import com.example.grckikino.model.Game
import com.example.grckikino.ui.theme.GrckiKinoTheme
import kotlinx.coroutines.delay

@Composable
fun PlayGameScreen(
    game: Game?,
    selectedBalls: List<Int>,
    onBallClicked: (Int) -> Unit,
    onRandomlySelectNumbersClick: (Int) -> Unit,
    resetSelectedBalls: () -> Unit,
    onGameExpired: () -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        SimpleMessageDialog(message = stringResource(id = R.string.game_expired)) {
            showDialog = false
            onGameExpired()
        }
    }

    LaunchedEffect(Unit) {
        resetSelectedBalls()
    }

    if (game != null) {
        val timeRemaining = remember(game.drawId) { mutableLongStateOf(game.drawTime - System.currentTimeMillis()) }

        LaunchedEffect(game.drawId) {
            while (timeRemaining.longValue > 0) {
                delay(1000)
                timeRemaining.longValue -= 1000

                if (timeRemaining.longValue <= 0) {
                    delay(timeRemaining.longValue)
                    showDialog = true
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            PlayGameHeader(
                timeOfGame = game.drawTime,
                drawId = game.drawId,
                numberOfSelectedBalls = selectedBalls.size,
                timeRemaining = timeRemaining.longValue,
                onRandomlySelectNumbersClick
            )

            SingleGame(selectedBalls, onBallClicked)
        }
    }
}

@Composable
fun SingleGame(
    selectedBalls: List<Int>,
    onBallClicked: (Int) -> Unit
) {
    Column {
        for (i in 0..7) {
            BallsRow(
                startNumber = i * 10 + 1,
                numberOfBalls = 10,
                selectedBalls,
                onBallClicked
            )
        }
    }
}

@Composable
fun PlayGameHeader(
    timeOfGame: Long,
    drawId: Int,
    numberOfSelectedBalls: Int,
    timeRemaining: Long,
    onRandomlySelectNumbersClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color(40, 45, 54))
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.DarkGray)
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 5.dp),
                text = stringResource(id = R.string.time_of_game_expanded, timeOfGame.formatAsTime(), drawId),
                style = TextStyle(
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 5.dp),
                text = stringResource(id = R.string.number_of_selected_balls, numberOfSelectedBalls),
                style = TextStyle(
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            )
        }

        Row {
            Text(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 5.dp),
                text = stringResource(id = R.string.time_until_draw, timeRemaining.formatAsTimeUntilGame()),
                style = TextStyle(
                    color = if (timeRemaining < 30000) {
                        Color.Red
                    } else {
                        Color.Green
                    },
                    fontSize = 12.sp
                )
            )
        }

        RowOfQuotes(quotes = listOf(
            stringResource(id = R.string.b_k),
            stringResource(id = R.string.bk_1),
            stringResource(id = R.string.bk_2),
            stringResource(id = R.string.bk_3),
            stringResource(id = R.string.bk_4),
            stringResource(id = R.string.bk_5),
            stringResource(id = R.string.bk_6),
            stringResource(id = R.string.bk_7))
        )

        Divider(
            modifier = Modifier
                .padding(start = 40.dp, top = 3.dp, bottom = 3.dp)
        )

        RowOfQuotes(quotes = listOf(
            stringResource(id = R.string.quote),
            stringResource(id = R.string.q_1),
            stringResource(id = R.string.q_2),
            stringResource(id = R.string.q_3),
            stringResource(id = R.string.q_4),
            stringResource(id = R.string.q_5),
            stringResource(id = R.string.q_6),
            stringResource(id = R.string.q_7)))

        val dropdownItems = listOf(8, 9, 10, 11, 12, 13, 14, 15)

        var isExpanded by remember { mutableStateOf(false) }
        var selectedItem by remember { mutableIntStateOf(dropdownItems[0]) }

        Row {
            Button(modifier = Modifier.weight(6f), onClick = {
                onRandomlySelectNumbersClick(selectedItem)
            }) {
                Text(text = stringResource(id = R.string.choose_random))
            }

            Spacer(
                modifier = Modifier
                    .weight(6f)
            )

            Row(
                modifier = Modifier
                    .weight(3f)
                    .clickable { isExpanded = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = selectedItem.toString(),
                    modifier = Modifier
                        .clickable { isExpanded = true }
                        .padding(16.dp),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    tint = Color.LightGray,
                    contentDescription = ""
                )

                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {

                    dropdownItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.toString()) },
                            onClick = {
                                selectedItem = item
                                isExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
