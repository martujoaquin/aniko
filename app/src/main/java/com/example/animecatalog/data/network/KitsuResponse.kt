package com.example.animecatalog.data.network

import com.google.gson.annotations.SerializedName

data class KitsuResponse (
    @SerializedName("data")
    val data: List<KitsuAnimeData>
)

data class KitsuAnimeData(
    @SerializedName("id")
    val id: String,

    @SerializedName("attributes")
    val attributes: KitsuAnimeAttributes
)

data class KitsuAnimeAttributes(
    @SerializedName("canonicalTitle")
    val canonicalTitle: String,

    @SerializedName("averageRating")
    val averageRating: String?,

    @SerializedName("synopsis")
    val synopsis: String?,

    @SerializedName("episodeCount")
    val episodeCount: Int?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("posterImage")
    val posterImage: KitsuImage?,

    @SerializedName("coverImage")
    val coverImage: KitsuImage?
)

data class KitsuImage(
    @SerializedName("small")
    val small: String?,

    @SerializedName("large")
    val large: String?,

    @SerializedName("original")
    val original: String?
)

data class KitsuDetailResponse(
    @SerializedName("data")
    val data: KitsuAnimeData,

    @SerializedName("included")
    val included: List<KitsuIncludedCategory>?
)

data class KitsuIncludedCategory(
    @SerializedName("type")
    val type: String,

    @SerializedName("attributes")
    val attributes: KitsuCategoryAttributes
)

data class KitsuCategoryAttributes(
    @SerializedName("title")
    val title: String
)