package com.example.animecatalog.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import com.example.animecatalog.data.repository.AnimeRepository
import com.example.animecatalog.data.model.Anime
import com.example.animecatalog.data.local.AnimeDbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnimeViewModel(application: Application) : AndroidViewModel(application){
    private val repository = AnimeRepository(
        AnimeDbHelper(application)
    )

    private val _animeList = MutableLiveData<List<Anime>>()
    val animeList: LiveData<List<Anime>> = _animeList

    private val _favoritesList = MutableLiveData<List<Anime>>()
    val favoritesList: LiveData<List<Anime>> = _favoritesList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _selectedAnime = MutableLiveData<Anime?>()
    val selectedAnime: LiveData<Anime?> = _selectedAnime

    fun loadAnime() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            _errorMessage.postValue(null)

            try {
                val list = repository.getAnimeList()
                _animeList.postValue(list)
            } catch (e: Exception) {
                _errorMessage.postValue(
                    "Unable to load anime. Check your internet connection."
                )
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun searchAnime(query: String) {
        viewModelScope.launch (Dispatchers.IO) {
            _isLoading.postValue(true)
            _errorMessage.postValue(null)

            try {
                val list = repository.searchAnime(query)
                _animeList.postValue(list)
            } catch (e: Exception) {
                _errorMessage.postValue(
                    "Unable to search anime."
                )
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch (Dispatchers.IO) {
            _favoritesList.postValue(
                repository.getFavorites()
            )
        }
    }

    fun toggleFavorite(anime: Anime) {
        viewModelScope.launch (Dispatchers.IO) {
            val newStatus = repository.toggleFavorite(anime)

            val updatedList = _animeList.value?.map { currentAnime ->
                if (currentAnime.id == anime.id) {
                    currentAnime.copy(isFavorite = newStatus)
                } else {
                    currentAnime
                }
            } ?: emptyList()

            _animeList.postValue(updatedList)
            _selectedAnime.postValue(
                anime.copy(isFavorite = newStatus)
            )

            loadFavorites()
        }
    }

    fun loadAnimeDetail(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            _errorMessage.postValue(null)

            try{
                val anime = repository.getAnimeDetail(id)
                _selectedAnime.postValue(anime)
            } catch (e: Exception) {
                _errorMessage.postValue(
                    "Unable to load anime details."
                )
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun refreshFavoriteState() {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedList = _animeList.value?.map { anime ->
                anime.copy(
                    isFavorite = repository.isFavorite(anime.id)
                )
            } ?: emptyList()

            _animeList.postValue(updatedList)
        }
    }

    fun loadFavoriteDetail(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val anime = repository.getFavoriteById(id)
            _selectedAnime.postValue(anime)
        }
    }
}