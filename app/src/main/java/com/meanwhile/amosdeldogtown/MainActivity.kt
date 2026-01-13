@file:OptIn(ExperimentalMaterial3Api::class)

package com.meanwhile.amosdeldogtown

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import coil.compose.AsyncImage
import com.meanwhile.amosdeldogtown.data.Pet
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

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Amos Del Dogtown")
                            }
                        )
                    }
                ) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding),
                        uiState = uiState.value,
                        onRefresh = {
                            viewModel.onRefresh()
                        },
                    )
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
                pets = uiState.pets,
            )
        }
    }
}

@Composable
private fun PetList(pets: List<Pet>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize()
    ) {
        items(pets) { pet ->
            PetItem(pet = pet)
        }
    }
}

@Composable
private fun PetItem(pet: Pet, modifier: Modifier = Modifier) {
    Surface( // Defines the surface, like color and rounded corners
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
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
                Pet("1", "Amos", "Description", "https://example.com/image.jpg"),
                Pet("2", "Rex", "Description", "https://example.com/image.jpg")
            )
        )
    }
}
