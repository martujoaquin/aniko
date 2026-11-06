package com.example.animecatalog.ui.anime

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.animecatalog.databinding.ItemAnimeBinding
import com.example.animecatalog.data.model.Anime
import com.example.animecatalog.R
import com.squareup.picasso.Picasso


class AnimeAdapter (
    private val onFavoriteClick: (Anime) -> Unit,
    private val onAnimeClick: (Anime) -> Unit
): androidx.recyclerview.widget.ListAdapter<Anime, AnimeAdapter.AnimeViewHolder>(DiffCallBack) {
    class AnimeViewHolder(
        val binding: ItemAnimeBinding
    ) : RecyclerView.ViewHolder(binding.root)

    companion object DiffCallBack : androidx.recyclerview.widget.DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = getItem(position)

        holder.binding.tvTitle.text = anime.title
        holder.binding.tvScore.text = holder.itemView.context.getString(
            R.string.score_value,
            anime.score ?: "N/A"
        )

        Picasso.get()
            .load(anime.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_gallery)
            .into(holder.binding.ivAnime)

        val favoriteIcon = if (anime.isFavorite) {
            R.drawable.ic_favorite_filled
        } else {
            R.drawable.ic_favorite_outline
        }

        holder.binding.btnFavorite.setImageResource(favoriteIcon)
        holder.binding.btnFavorite.setOnClickListener {
            onFavoriteClick(anime)
        }
        holder.binding.root.setOnClickListener {
            onAnimeClick(anime)
        }
    }
}