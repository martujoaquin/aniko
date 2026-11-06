package com.example.animecatalog.ui.anime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.activityViewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.animecatalog.data.model.Anime
import com.example.animecatalog.databinding.FragmentAnimeListBinding
import com.example.animecatalog.viewmodel.AnimeViewModel
import com.example.animecatalog.ui.detail.AnimeDetailActivity

class AnimeListFragment : Fragment() {

    private var _binding: FragmentAnimeListBinding? = null
    private val binding get() = _binding!!

    private lateinit var animeAdapter: AnimeAdapter
    private var searchJob: Job? = null
    private val viewModel: AnimeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        _binding = FragmentAnimeListBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupSearch()
        observeViewModel()

        viewModel.loadAnime()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFavoriteState()
    }

    private fun setupRecyclerView() {
        animeAdapter = AnimeAdapter(
            onFavoriteClick = { anime ->
                viewModel.toggleFavorite(anime)
            },
            onAnimeClick = { anime ->
                val intent = Intent(requireContext(), AnimeDetailActivity::class.java)
                intent.putExtra("animeId", anime.id)
                startActivity(intent)
            }
        )

        binding.recyclerViewAnimes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAnimes.adapter = animeAdapter
    }

    private fun showAnimes(animes: List<Anime>) {
        animeAdapter.submitList(animes)
    }

    private fun observeViewModel() {
        viewModel.animeList.observe(viewLifecycleOwner) { animeList ->
            showAnimes(animeList)

            binding.tvEmptyOrError.visibility =
                if (animeList.isNullOrEmpty()) View.VISIBLE
                else View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility =
                if (isLoading && animeAdapter.currentList.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                binding.tvEmptyOrError.text = error
                binding.tvEmptyOrError.visibility = View.VISIBLE
            }
        }
    }

    private fun setupSearch() {
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            searchJob?.cancel()
            val query = text?.toString()?.trim().orEmpty()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(500)

                if (query.isEmpty()) {
                    viewModel.loadAnime()
                } else {
                    viewModel.searchAnime(query)
                }
            }
        }

        binding.etSearch.setOnEditorActionListener { _, _, _ ->
            val imm = requireContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)

            binding.etSearch.clearFocus()

            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}