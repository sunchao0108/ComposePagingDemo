package com.example.composepagingdemo.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel

class MoviesPagingDataSource(
    private val repo: MoviesRepository,
    private val pageSize: Int
) : PagingSource<Int, Movies>() {

    private val channel: Channel<Unit> = Channel(1, BufferOverflow.DROP_LATEST)

    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        return try {
            val nextPageNumber = params.key ?: 1
            // load initial page
            if (nextPageNumber == 1) loadNextPage()
            val response = repo.getMovies(page = nextPageNumber, size = pageSize)

            if (response.content.isEmpty()) {
                LoadResult.Page(
                    data = response.content,
                    prevKey = response.number - 1,
                    nextKey = null
                )
            } else {
                /*
                * block next page data return, when user click the button,
                * call loadNextPage. if you don't want to automatically
                * request data as the user scrolls toward the end of the
                * loaded data. put this line above the repo.getMovies.
                **/
                channel.receive()
                LoadResult.Page(
                    data = response.content,
                    prevKey = response.number - 1,
                    nextKey = response.number + 1
                )
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    suspend fun loadNextPage() {
        channel.send(Unit)
    }
}
