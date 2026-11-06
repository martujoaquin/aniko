package com.example.animecatalog.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface KitsuApiService {
    @GET("anime")
    suspend fun getAnimeList(
        @Query("page[limit]") limit: Int = 20
    ): KitsuResponse

    @GET("anime")
    suspend fun searchAnime(
        @Query("filter[text]") query: String
    ): KitsuResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetail(
        @Path("id") id: String,
        @Query("include") include: String = "categories"
    ): KitsuDetailResponse
}