package com.example.grckikino.model

import com.google.gson.annotations.SerializedName

data class GameResults(
    @SerializedName("content") val content: List<Game>,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("last") val isLast: Boolean,
    @SerializedName("numberOfElements") val numberOfElements: Int
)
