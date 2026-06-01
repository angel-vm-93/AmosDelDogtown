package com.meanwhile.amosdeldogtown.data

import android.content.Context

class FavoritesRepository(context: Context) {

    private val prefs = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)

    fun getFavorites(): Set<String> {
        return prefs.getStringSet("favorite_ids", emptySet())
            ?.mapNotNull { it }
            ?.toSet() ?: emptySet()
    }

    fun toggleFavorite(id: String) {
        val current = getFavorites().toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        prefs.edit().putStringSet("favorite_ids", current.map { it }.toSet()).apply()
    }

    fun isFavorite(id: String): Boolean = getFavorites().contains(id)
}