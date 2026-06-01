package com.meanwhile.amosdeldogtown.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.meanwhile.amosdeldogtown.data.PetRepository
import com.meanwhile.amosdeldogtown.data.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val petRepository = PetRepository()
    private val favoritesRepository = FavoritesRepository(application)

    // This mutable flow will allow us to post UiState to the ui
    private val _uiState = MutableStateFlow(MainUiState())

    // The Ui shouldn't see a mutable flow (the ui should only observe) so we expose the flow a Non mutable
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    // This is executed when the class is created
    // Perfect for the api call we want to do at the start
    init {
        fetchPets()
    }

    public fun onRefresh() {
        fetchPets()
    }

    fun toggleFavorite(petId: String) {
        favoritesRepository.toggleFavorite(petId)
        _uiState.value = _uiState.value.copy(
            favoriteIds = favoritesRepository.getFavorites()
        )
    }

    fun toggleFilter() {
        _uiState.value = _uiState.value.copy(
            showOnlyFavorites = !_uiState.value.showOnlyFavorites
        )
    }

    private fun fetchPets() {
        viewModelScope.launch {
            // post the UI that we are loading
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // Call the service
                val response = petRepository.getPets()

                // Post to the Ui our results
                _uiState.value = _uiState.value.copy(
                    pets = response,
                    isLoading = false,
                    favoriteIds = favoritesRepository.getFavorites()
                )
            } catch (e: Exception) {
                // post to the ui the error
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}
