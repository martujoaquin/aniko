package com.example.animecatalog.data.model

data class Anime (
    val id: String,
    val title: String,
    val score: Double?,
    val synopsis: String?,
    val episodes: Int?,
    val status: String?,
    val imageUrl: String?,
    val largeImageUrl: String?,
    val genres: List<String>,
    var isFavorite: Boolean = false
) {

}