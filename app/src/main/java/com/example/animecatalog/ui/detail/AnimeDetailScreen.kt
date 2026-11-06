package com.example.animecatalog.ui.detail

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.animecatalog.R
import com.example.animecatalog.data.model.Anime
import com.squareup.picasso.Picasso

@Composable
fun AnimeDetailScreen(
    anime: Anime,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TextButton(
                onClick = onBackClick,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            ) {
                Text(
                    text = "← ${stringResource(R.string.back)}"
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = anime.title,
                style = MaterialTheme.typography.headlineMedium
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        ImageView(context).apply {
                            scaleType = ImageView.ScaleType.FIT_CENTER
                        }
                    },
                    update = { imageView ->
                        Picasso.get()
                            .load(anime.largeImageUrl ?: anime.imageUrl)
                            .placeholder(android.R.drawable.ic_menu_gallery)
                            .error(android.R.drawable.ic_menu_gallery)
                            .into(imageView)
                    }
                )

                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align (Alignment.BottomEnd)
                        .size(56.dp)
                ) {
                    Image(
                        painter = painterResource(
                            id = if (anime.isFavorite) {
                                R.drawable.ic_favorite_filled
                            } else {
                                R.drawable.ic_favorite_outline
                            }
                        ),
                        contentDescription = stringResource(R.string.favorites),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                DetailInfo(
                    label = stringResource(R.string.score),
                    value = anime.score?.toString() ?: "N/A",
                    modifier = Modifier.weight(1f)
                )

                DetailInfo(
                    label = stringResource(R.string.episodes),
                    value = anime.episodes?.toString() ?: "N/A",
                    modifier = Modifier.weight(1f)
                )

                DetailInfo(
                    label = stringResource(R.string.status),
                    value = anime.status
                        ?.replaceFirstChar { it.uppercase() }
                        ?: "N/A",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = stringResource(R.string.genres),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = if (anime.genres.isEmpty()) {
                    "N/A"
                } else {
                    anime.genres.joinToString(", ")
                },
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = stringResource(R.string.synopsis),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = anime.synopsis ?: "No synopsis available",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}

@Composable
private fun DetailInfo(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}