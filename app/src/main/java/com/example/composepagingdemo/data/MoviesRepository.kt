package com.example.composepagingdemo.data

class MoviesRepository(private val totalCount: Int) {

    fun getMovies(page: Int, size: Int): MovieData {
        if (page * size > totalCount) return MovieData(
            number = page,
            content = listOf()
        ) else {
            return MovieData(
                number = page,
                content = List(size = size) {
                    val id = it + ((page - 1) * size)
                    Movies(
                        id = id,
                        name = "The movie $id",
                        description = "This is the movie $id"
                    )
                }
            )
        }
    }
}