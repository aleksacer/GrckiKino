package com.example.grckikino.model

import com.google.gson.annotations.SerializedName

data class Game(
    @SerializedName("gameId") val gameId: Int,
    @SerializedName("drawId") val drawId: Int,
    @SerializedName("drawTime") val drawTime: Long,
    @SerializedName("winningNumbers") val winningNumbers: WinningNumbers
)

data class WinningNumbers(
    @SerializedName("list") val list: List<Int>,
)
