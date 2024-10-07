package com.example.grckikino

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.grckikino.data.ResultsRepository
import com.example.grckikino.model.Game
import com.example.grckikino.network.GrckiKinoApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GrckiKinoViewModel @Inject constructor(
    private val apiService: GrckiKinoApiService,
    private val resultsRepository: ResultsRepository
) : ViewModel() {

    private val _futureGames: MutableStateFlow<List<Game>> = MutableStateFlow(emptyList())
    val futureGames: StateFlow<List<Game>> = _futureGames

    private val _selectedGame: MutableStateFlow<Game?> = MutableStateFlow(null)
    val selectedGame: StateFlow<Game?> = _selectedGame

    private val _selectedNumbers: MutableStateFlow<List<Int>> = MutableStateFlow(mutableListOf())
    val selectedNumbers: StateFlow<List<Int>> = _selectedNumbers

    private val _results: MutableStateFlow<PagingData<Game>> = MutableStateFlow(PagingData.empty())
    val results: StateFlow<PagingData<Game>> = _results

    init {
        viewModelScope.launch {
            getFutureGames()
        }
        loadResults()
    }

    fun refreshGameData() {
        viewModelScope.launch {
            getFutureGames()
        }
    }

    fun gameSelected(id: Int) {
        viewModelScope.launch {
            _selectedGame.value = withContext(Dispatchers.IO) {
                apiService.getGameByDrawId(id)
            }
        }
    }

    fun ballClicked(number: Int) {
        if (_selectedNumbers.value.contains(number)) {
            _selectedNumbers.value = _selectedNumbers.value.filterNot { it == number }
        } else {
            _selectedNumbers.value = _selectedNumbers.value + number
        }
    }

    fun generateRandomBalls(numberToGenerate: Int) {
        val generatedBalls = mutableListOf<Int>()
        var numberOfGenerated = 0

        while (numberOfGenerated < numberToGenerate) {
            val newNumber = Random.nextInt(1, 81)
            if (!generatedBalls.contains(newNumber)) {
                generatedBalls.add(newNumber)
                numberOfGenerated++
            }
        }

        _selectedNumbers.value = generatedBalls
    }

    fun resetBallsSelection() {
        _selectedNumbers.value = mutableListOf()
    }

    private suspend fun getFutureGames() {
        withContext(Dispatchers.Main) {
            _futureGames.value = emptyList()
            _futureGames.value = withContext(Dispatchers.IO) {
                val futureGames = apiService.getFutureGames()
                // Api ponekad vrati i isteklu igru u trenutku ovog poziva, filtriranje svega sto istice za manje od 3s
                futureGames.filter { it.drawTime - System.currentTimeMillis() > 3000 }
            }
        }
    }

    private fun loadResults() {
        viewModelScope.launch {
            resultsRepository.getResults()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _results.value = it
                }
        }
    }
}
