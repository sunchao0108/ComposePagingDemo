package com.example.composepagingdemo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.composepagingdemo.data.Movies
import com.example.composepagingdemo.viewmodel.MoviesViewModel

@Composable
fun MoviesList(viewModel: MoviesViewModel) {

    val moviesList = viewModel.moviesPagingDataSource.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Paging Demo with show more button",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }
        items(moviesList) { item ->
            item?.let {
                MoviesCard(movie = it)
            }
        }

        when (val state = moviesList.loadState.append) {
            is LoadState.NotLoading -> {
                if (state.endOfPaginationReached) {
                    item {
                        Text(
                            text = "No more items",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
            LoadState.Loading -> {
                item {
                    LoadMoreItem(viewModel = viewModel)
                }
            }
            is LoadState.Error -> {
                item {
                    ErrorItem(message = "Some error occurred")
                }
            }
        }

        when (moviesList.loadState.refresh) {
            is LoadState.NotLoading -> Unit
            LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            is LoadState.Error -> {}
        }
    }
}

@Composable
fun MoviesCard(movie: Movies) {
    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            )
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.large
            )
            .padding(12.dp)
    ) {
        Text(
            text = movie.name,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = movie.description,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun LoadMoreItem(viewModel: MoviesViewModel) {
    Box(
        contentAlignment = Center
    ) {
        OutlinedButton(onClick = { viewModel.loadNextPage() }) {
            Text(
                text = "Load next page",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun ErrorItem(message: String) {
    Text(text = message)
}
