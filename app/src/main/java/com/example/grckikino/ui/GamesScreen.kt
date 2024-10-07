package com.example.grckikino.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.grckikino.R
import com.example.grckikino.formatAsTime
import com.example.grckikino.formatAsTimeUntilGame
import com.example.grckikino.model.Game
import com.example.grckikino.model.WinningNumbers
import com.example.grckikino.ui.theme.GrckiKinoTheme
import kotlinx.coroutines.delay

@Composable
fun GamesScreen(
    gamesList: List<Game>,
    refreshGames: () -> Unit,
    onGameClicked: (Int) -> Unit,
    onGameAlreadyStarted: () -> Unit
) {

    // Fetchovanje podataka kad se korisnik vrati na ekran.
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                refreshGames()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        GamesScreenHeader()

        GamesList(gamesList = gamesList, onGameClicked = onGameClicked, onGameAlreadyStarted = onGameAlreadyStarted)
    }
}

@Composable
fun GamesScreenHeader() {
    Box(
        modifier = Modifier
            .background(Color.Gray)
            .fillMaxWidth()
    ) {

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                        .height(30.dp)
                        .aspectRatio(1f),
                    painter = painterResource(id = R.drawable.flag_of_greece),
                    contentDescription = ""
                )

                Text(
                    text = stringResource(id = R.string.game_name),
                    style = TextStyle(
                        color = Color.LightGray
                    )
                )
            }

            Divider(
                modifier = Modifier
                    .padding(horizontal = 3.dp, vertical = 5.dp),
                color = Color(12, 77, 168),
                thickness = 1.dp
            )

            Row {
                Text(
                    modifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 3.dp)
                        .weight(4f),
                    text = stringResource(id = R.string.time_of_game),
                    style = TextStyle(
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                )

                Spacer(modifier = Modifier.weight(6f))

                Text(
                    modifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 3.dp)
                        .weight(4f),
                    text = stringResource(id = R.string.time_for_playing),
                    style = TextStyle(
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                )
            }
        }

    }
}

@Composable
fun GamesList(
    gamesList: List<Game>,
    onGameClicked: (Int) -> Unit,
    onGameAlreadyStarted: () -> Unit
) {
    LazyColumn {
        items(gamesList) { game ->
            GameItem(game, onGameClicked, onGameAlreadyStarted)
        }
    }
}

@Composable
fun GameItem(
    game: Game,
    onGameItemClicked: (Int) -> Unit,
    onGameAlreadyStarted: () -> Unit
) {
    val timeRemaining = remember { mutableLongStateOf(game.drawTime - System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (timeRemaining.longValue > 0) {
            delay(1000)
            timeRemaining.longValue -= 1000

            if (timeRemaining.longValue <= 0) {
                delay(timeRemaining.longValue)
                onGameAlreadyStarted()
            }
        }
    }

    Column(
        modifier = Modifier
            .background(Color.Black),
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    if (timeRemaining.value > 1000) {
                        onGameItemClicked(game.drawId)
                    }
                }
                .padding(2.dp)
                .padding(vertical = 3.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(2f),
                text = game.drawTime.formatAsTime(),
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(7f))
            Text(
                modifier = Modifier
                    .weight(2f),
                text = timeRemaining.longValue.formatAsTimeUntilGame(),
                textAlign = TextAlign.Center,
                color = if (timeRemaining.longValue < 30000) {
                    Color.Red
                } else {
                    Color.Green
                }
            )
        }

        Divider(
            modifier = Modifier
                .padding(horizontal = 3.dp, vertical = 2.dp),
            color = Color.LightGray,
            thickness = 0.5.dp
        )
    }
}
