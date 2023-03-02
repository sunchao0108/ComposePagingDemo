package com.example.composepagingdemo.data

data class Movies(
    val id: Int,
    val name: String,
    val description: String
)

data class MovieData(
    val number: Int,
    val content: List<Movies>
)