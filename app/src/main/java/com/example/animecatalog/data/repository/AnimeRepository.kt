package com.example.animecatalog.data.repository

import com.example.animecatalog.data.local.AnimeDbHelper
import com.example.animecatalog.data.model.Anime
import com.example.animecatalog.data.network.KitsuAnimeData
import com.example.animecatalog.data.network.RetrofitClient

class AnimeRepository(
    private val dbHelper: AnimeDbHelper
) {

    suspend fun getAnimeList(): List<Anime> {
        val response = RetrofitClient.apiService.getAnimeList()

        return response.data.map { item ->
            val anime = item.toAnime()

            anime.copy(
                isFavorite = dbHelper.isFavorite(anime.id)
            )
        }
    }

    suspend fun searchAnime(query: String): List<Anime> {
        val response = RetrofitClient.apiService.searchAnime(query)

        return response.data.map { item ->
            val anime = item.toAnime()

            anime.copy(
                isFavorite = dbHelper.isFavorite(anime.id)
            )
        }
    }

    private fun KitsuAnimeData.toAnime(): Anime {
        return Anime(
            id = id,
            title = attributes.canonicalTitle,
            score = attributes.averageRating?.toDoubleOrNull(),
            synopsis = attributes.synopsis,
            episodes = attributes.episodeCount,
            status = attributes.status,
            imageUrl = attributes.posterImage?.small,
            largeImageUrl = attributes.posterImage?.large
                ?: attributes.posterImage?.original,
            genres = emptyList()
        )
    }

    fun getFavorites(): List<Anime> {
        return dbHelper.getAllFavorites()
    }

    fun getFavoriteById(id: String): Anime? {
        return dbHelper.getFavoriteById(id)
    }

    fun isFavorite(id: String): Boolean {
        return dbHelper.isFavorite(id)
    }

    suspend fun toggleFavorite(anime: Anime): Boolean {
        return if (anime.isFavorite) {
            dbHelper.deleteFavorite(anime.id)
            false
        } else {
            val animeToSave = if(anime.genres.isEmpty()){
                getAnimeDetail(anime.id)
            } else {
                anime
            }
            dbHelper.insertFavorite(animeToSave)
            true
        }
    }

    suspend fun getAnimeDetail(id: String): Anime {
        val response = RetrofitClient.apiService.getAnimeDetail(id)

        val anime = response.data.toAnime()

        val genres = response.included
            ?.filter { it.type == "categories" }
            ?.map { it.attributes.title }
            ?:emptyList()

        return anime.copy(
            genres = genres,
            isFavorite = dbHelper.isFavorite(anime.id)
        )
    }
}