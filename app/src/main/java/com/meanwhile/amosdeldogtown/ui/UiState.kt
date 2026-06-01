package com.meanwhile.amosdeldogtown.ui

import com.meanwhile.amosdeldogtown.data.Pet

data class MainUiState(
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val favoriteIds: Set<String> = emptySet(),
    val showOnlyFavorites: Boolean = false
)
