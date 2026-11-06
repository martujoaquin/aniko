package com.example.animecatalog.data.local

import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.animecatalog.data.model.Anime

class AnimeDbHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "anime_catalog.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_FAVORITES = "favorites"

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_SCORE = "score"
        const val COLUMN_SYNOPSIS = "synopsis"
        const val COLUMN_EPISODES = "episodes"
        const val COLUMN_STATUS = "status"
        const val COLUMN_GENRES = "genres"
        const val COLUMN_IMAGE_URL = "image_url"
        const val COLUMN_LARGE_IMAGE_URL = "large_image_url"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_FAVORITES (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_SCORE REAL,
                $COLUMN_SYNOPSIS TEXT,
                $COLUMN_EPISODES INTEGER,
                $COLUMN_STATUS TEXT,
                $COLUMN_GENRES TEXT,
                $COLUMN_IMAGE_URL TEXT,
                $COLUMN_LARGE_IMAGE_URL TEXT
            )
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
        onCreate(db)
    }

    fun insertFavorite(anime: Anime): Boolean {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_ID, anime.id)
            put(COLUMN_TITLE, anime.title)
            put(COLUMN_SCORE, anime.score)
            put(COLUMN_SYNOPSIS, anime.synopsis)
            put(COLUMN_EPISODES, anime.episodes)
            put(COLUMN_STATUS, anime.status)
            put(COLUMN_GENRES, anime.genres.joinToString(","))
            put(COLUMN_IMAGE_URL, anime.imageUrl)
            put(COLUMN_LARGE_IMAGE_URL, anime.largeImageUrl)
        }

        val result = db.insertWithOnConflict(
            TABLE_FAVORITES,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )

        db.close()

        return result != -1L
    }

    fun deleteFavorite(id: String): Boolean {
        val db = writableDatabase

        val result = db.delete(
            TABLE_FAVORITES,
            "$COLUMN_ID = ?",
            arrayOf(id)
        )

        db.close()

        return result > 0
    }

    fun isFavorite(id: String): Boolean {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT 1 FROM $TABLE_FAVORITES WHERE $COLUMN_ID = ?",
            arrayOf(id)
        )

        val exists = cursor.count > 0

        cursor.close()
        db.close()

        return exists
    }

    fun getAllFavorites(): List<Anime> {
        val favorites = mutableListOf<Anime>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_FAVORITES",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val anime = Anime(
                    id = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_ID)
                    ),

                    title = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_TITLE)
                    ),

                    score = if (
                        cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_SCORE))
                    ) null else {
                        cursor.getDouble(
                            cursor.getColumnIndexOrThrow(COLUMN_SCORE)
                        )
                    },

                    synopsis = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_SYNOPSIS)
                    ),

                    episodes = if (
                        cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_EPISODES))
                    ) null else {
                        cursor.getInt(
                            cursor.getColumnIndexOrThrow(COLUMN_EPISODES)
                        )
                    },

                    status = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_STATUS)
                    ),

                    imageUrl = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)
                    ),

                    largeImageUrl = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_LARGE_IMAGE_URL)
                    ),

                    genres = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_GENRES)
                    )?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),

                    isFavorite = true

                )

                favorites.add(anime)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return favorites
    }

    fun getFavoriteById(id: String): Anime? {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_FAVORITES WHERE $COLUMN_ID = ?",
            arrayOf(id)
        )

        var anime: Anime? = null

        if (cursor.moveToFirst()) {
            anime = Anime(
                id = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_ID)
                ),

                title = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_TITLE)
                ),

                score = if (
                    cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_SCORE))
                ) null else {
                    cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_SCORE)
                    )
                },

                synopsis = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_SYNOPSIS)
                ),

                episodes = if (
                    cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_EPISODES))
                ) null else {
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_EPISODES)
                    )
                },

                status = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_STATUS)
                ),

                imageUrl = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)
                ),

                largeImageUrl = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_LARGE_IMAGE_URL)
                ),

                genres = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_GENRES)
                )?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),

                isFavorite = true
            )
        }

        cursor.close()
        db.close()

        return anime
    }
}