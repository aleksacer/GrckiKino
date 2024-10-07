package com.example.grckikino.network

import com.example.grckikino.model.Game
import com.example.grckikino.model.GameResults
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GrckiKinoApiService {

    @GET("draws/v3.0/1100/upcoming/20")
    suspend fun getFutureGames(): List<Game>

    @GET("draws/v3.0/1100/{drawId}")
    suspend fun getGameByDrawId(@Path("drawId") drawId: Int): Game

    @GET("draws/v3.0/1100/draw-date/{fromDate}/{toDate}")
    suspend fun getResultsForDates(
        @Path("fromDate") startDate: String,
        @Path("toDate") toDate: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GameResults
}
