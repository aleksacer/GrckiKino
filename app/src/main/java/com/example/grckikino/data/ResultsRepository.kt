package com.example.grckikino.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.grckikino.model.Game
import com.example.grckikino.network.GrckiKinoApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResultsRepository @Inject constructor(
    private val apiService: GrckiKinoApiService
) {

    fun getResults(): Flow<PagingData<Game>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 3),
            pagingSourceFactory = {
                ResultsPagingSource(apiService)
            }
        ).flow
    }
}
