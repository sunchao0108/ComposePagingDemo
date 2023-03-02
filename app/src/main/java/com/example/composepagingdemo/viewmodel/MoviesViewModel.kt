package com.example.composepagingdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.composepagingdemo.data.MoviesPagingDataSource
import com.example.composepagingdemo.data.MoviesRepository
import kotlinx.coroutines.launch

class MoviesViewModel(
    moviesRepository: MoviesRepository = MoviesRepository(totalCount = 20)
) : ViewModel() {

    private val pageConfig = PagingConfig(pageSize = 5)
    private val pagingDataSource = MoviesPagingDataSource(moviesRepository, pageConfig.pageSize)

    val moviesPagingDataSource = Pager(pageConfig) {
        pagingDataSource
    }.flow.cachedIn(viewModelScope)

    fun loadNextPage() {
        viewModelScope.launch {
            pagingDataSource.loadNextPage()
        }
    }

}