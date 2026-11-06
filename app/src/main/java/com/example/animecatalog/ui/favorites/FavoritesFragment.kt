package com.example.animecatalog.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.animecatalog.databinding.FragmentFavoritesBinding
import com.example.animecatalog.ui.anime.AnimeAdapter
import com.example.animecatalog.ui.detail.AnimeDetailActivity
import com.example.animecatalog.viewmodel.AnimeViewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var animeAdapter: AnimeAdapter
    private val viewModel: AnimeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeViewModel()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavorites()
    }

    private fun setupRecyclerView() {
        animeAdapter = AnimeAdapter(
            onFavoriteClick = { anime ->
                viewModel.toggleFavorite(anime)
            },
            onAnimeClick = { anime ->
                val intent = Intent(requireContext(), AnimeDetailActivity::class.java)
                intent.putExtra("animeId", anime.id)
                intent.putExtra("fromFavorites", true)
                startActivity(intent)            }
        )

        binding.recyclerViewFavorites.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerViewFavorites.adapter = animeAdapter
    }

    private fun observeViewModel() {
        viewModel.favoritesList.observe(viewLifecycleOwner) { favorites ->
            animeAdapter.submitList(favorites)

            binding.tvEmptyFavorites.visibility =
                if (favorites.isNullOrEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

            binding.tvEmptyFavoritesDescription.visibility =
                if (favorites.isNullOrEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}