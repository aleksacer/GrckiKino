package com.example.grckikino.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.grckikino.model.Game
import com.example.grckikino.network.GrckiKinoApiService
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ResultsPagingSource(
    private val apiService: GrckiKinoApiService
): PagingSource<Int, Game>() {

    override val keyReuseSupported: Boolean = true

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        return try {
            val currentPage = params.key ?: 0
            val date = getCurrentDate()
            val resultData = apiService.getResultsForDates(
                date,
                date,
                currentPage,
                PAGE_SIZE
            )

            val currentPageNumber = if (currentPage == 0) null else currentPage - 1
            val nextPageNumber = if (currentPageNumber != null) currentPageNumber + 1 else 0

            LoadResult.Page(
                data = resultData.content,
                prevKey = currentPageNumber,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Game>): Int? = state.anchorPosition

    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }
}

private const val PAGE_SIZE = 10
