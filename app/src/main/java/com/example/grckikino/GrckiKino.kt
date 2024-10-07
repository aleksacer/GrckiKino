package com.example.grckikino

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.grckikino.ui.GameLiveScreen
import com.example.grckikino.ui.GameResultsScreen
import com.example.grckikino.ui.GamesScreen
import com.example.grckikino.ui.PlayGameScreen

@Composable
fun GrckiKino(grckiKinoViewModel: GrckiKinoViewModel = viewModel()) {

    val navController = rememberNavController()

    val futureGames = grckiKinoViewModel.futureGames.collectAsState()

    val selectedGame = grckiKinoViewModel.selectedGame.collectAsState()

    val gameResults = grckiKinoViewModel.results.collectAsLazyPagingItems()

    val selectedNumbers = grckiKinoViewModel.selectedNumbers.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) { padding ->

        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = Screen.GamesScreen
        ) {

            composable(Screen.GameLiveScreen) {
                GameLiveScreen()
            }

            composable(Screen.GamesScreen) {
                GamesScreen(gamesList = futureGames.value, onGameClicked = {
                    grckiKinoViewModel.gameSelected(it)
                    navController.navigate(Screen.PlayGameScreen)
                },
                    refreshGames = { grckiKinoViewModel.refreshGameData() }) {
                    grckiKinoViewModel.refreshGameData()
                }
            }

            composable(Screen.GameResultsScreen) {
                GameResultsScreen(results = gameResults)
            }

            composable(Screen.PlayGameScreen) {
                PlayGameScreen(
                    selectedGame.value,
                    selectedNumbers.value,
                    grckiKinoViewModel::ballClicked,
                    grckiKinoViewModel::generateRandomBalls,
                    grckiKinoViewModel::resetBallsSelection
                ) {
                    navController.navigate(Screen.GamesScreen)
                    grckiKinoViewModel.refreshGameData()
                }
            }
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    BottomNavigation {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Menu, contentDescription = "") },
            label = { Text("Games") },
            selected = currentRoute == Screen.GamesScreen,
            onClick = { navController.navigate(Screen.GamesScreen) }
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Filled.PlayArrow, contentDescription = "") },
            label = { Text("Live") },
            selected = currentRoute == Screen.GameLiveScreen,
            onClick = { navController.navigate(Screen.GameLiveScreen) }
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Info, contentDescription = "") },
            label = { Text("Results") },
            selected = currentRoute == Screen.GameResultsScreen,
            onClick = { navController.navigate(Screen.GameResultsScreen) }
        )
    }
}

object Screen {
    const val GamesScreen = "GamesScreen"
    const val GameLiveScreen = "GameLiveScreen"
    const val GameResultsScreen = "GameResultsScreen"
    const val PlayGameScreen = "PlayGameScreen"
}
