package com.example.animecatalog.ui.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.animecatalog.viewmodel.AnimeViewModel

class AnimeDetailActivity : ComponentActivity() {

    private val viewModel: AnimeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animeId = intent.getStringExtra("animeId") ?: return

        val fromFavorites = intent.getBooleanExtra("fromFavorites", false)

        if (fromFavorites) {
            viewModel.loadFavoriteDetail(animeId)
        } else {
            viewModel.loadAnimeDetail(animeId)
        }

        setContent {
            MaterialTheme {
                val anime by viewModel.selectedAnime.observeAsState()

                anime?.let{
                    AnimeDetailScreen(
                        anime = it,
                        onBackClick = { finish() },
                        onFavoriteClick = {
                            viewModel.toggleFavorite(it)
                        }
                    )
                }
            }
        }
    }
}