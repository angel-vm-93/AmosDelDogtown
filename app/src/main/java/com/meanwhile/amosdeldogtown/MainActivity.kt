@file:OptIn(ExperimentalMaterial3Api::class)

package com.meanwhile.amosdeldogtown

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.meanwhile.amosdeldogtown.data.Pet
import com.meanwhile.amosdeldogtown.ui.DetailView
import com.meanwhile.amosdeldogtown.ui.MainUiState
import com.meanwhile.amosdeldogtown.ui.MainViewModel
import com.meanwhile.amosdeldogtown.ui.theme.AmosDelDogtownTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AmosDelDogtownTheme {

                // State is only exposed by the ViewModel
                val uiState = viewModel.uiState.collectAsState()
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            topBar = {
                                TopAppBar(
                                    title = { Text("Amos Del Dogtown") },
                                    actions = {
                                        IconButton(onClick = { viewModel.toggleFilter() }) {
                                            Icon(
                                                imageVector = if (uiState.value.showOnlyFavorites)
                                                    Icons.Filled.Favorite
                                                else
                                                    Icons.Outlined.FavoriteBorder,
                                                contentDescription = "Filtrar favoritos"
                                            )
                                        }
                                    }
                                )
                            }
                        ) { innerPadding ->
                            MainContent(
                                modifier = Modifier.padding(innerPadding),
                                uiState = uiState.value,
                                onRefresh = { viewModel.onRefresh() },
                                onPetClick = { pet -> navController.navigate("detail/${pet.id}") },
                                onFavoriteClick = { petId -> viewModel.toggleFavorite(petId) }
                            )
                        }
                    }
                    composable("detail/{petId}") { backStackEntry ->
                        val petId = backStackEntry.arguments?.getString("petId")
                        val pet = uiState.value.pets.find { it.id == petId }
                        if (pet != null) {
                            DetailView(
                                pet = pet,
                                onBack = { navController.popBackStack() },
                                isFavorite = pet.id in uiState.value.favoriteIds,
                                onFavoriteClick = { petId -> viewModel.toggleFavorite(petId) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    uiState: MainUiState,
    onRefresh: () -> Unit,
    onPetClick: (Pet) -> Unit,
    onFavoriteClick: (String) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(uiState.error != null) {
            Text(
                modifier = Modifier
                    .background(color = Color.Red)
                    .padding(32.dp),
                text = stringResource(R.string.error_message, uiState.error.orEmpty()),
                color = Color.White,
            )
        }

        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh,
            modifier = Modifier,
        ) {
            PetList(
                pets = if (uiState.showOnlyFavorites) {
                    uiState.pets.filter { it.id in uiState.favoriteIds }
                } else {
                    uiState.pets
                },
                onPetClick = onPetClick,
                onFavoriteClick = onFavoriteClick,
                favoriteIds = uiState.favoriteIds,
            )
        }
    }
}

@Composable
private fun PetList(pets: List<Pet>,
                    onPetClick: (Pet) -> Unit,
                    onFavoriteClick: (String) -> Unit,
                    favoriteIds: Set<String>,
                    modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize()
    ) {
        items(pets) { pet ->
            PetItem(
                pet = pet,
                onPetClick = onPetClick,
                onFavoriteClick = onFavoriteClick,
                isFavorite = pet.id in favoriteIds
            )
        }
    }
}

@Composable
private fun PetItem(
    pet: Pet,
    onPetClick: (Pet) -> Unit,
    onFavoriteClick: (String) -> Unit,
    isFavorite: Boolean,
    modifier: Modifier = Modifier
) {
    Surface( // Defines the surface, like color and rounded corners
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onPetClick(pet) },
        color = Color.LightGray,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box( // Defines how the content aligns inside
            modifier = Modifier
                .size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = "https://" + pet.imageUrl, // FIXME this is pretty hacky, but will work for this tutorial
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = { onFavoriteClick(pet.id) },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
            Surface(
                color = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Text(
                    text = pet.name,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PetListPreview() {
    AmosDelDogtownTheme {
        PetList(
            pets = listOf(
                Pet("1", "Amos", null, null, null, null, false, false, null, null, "Description", "https://example.com/image.jpg"),
                Pet("2", "Rex", null, null, null, null, false, false, null, null, "Description", "https://example.com/image.jpg")
            ),
            onPetClick = {},
            onFavoriteClick = {},
            favoriteIds = emptySet()
        )
    }
}
