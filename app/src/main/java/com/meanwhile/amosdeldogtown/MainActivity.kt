package com.meanwhile.amosdeldogtown

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.meanwhile.amosdeldogtown.data.Pet
import com.meanwhile.amosdeldogtown.data.PetApiService
import com.meanwhile.amosdeldogtown.ui.theme.AmosDelDogtownTheme

class MainActivity : ComponentActivity() {

    /** Lazy: Calls the lambda the first time we access this val, the next times will reuse
    * whatever was returned in the lambda
    * Our lambda is creating a instance of PetApiService, which we'll use to call the API.
    * Creating objects is always a "heavy" operations, so you only want to do it once and later reuse.
    */
    private val petApiService: PetApiService by lazy {
        Injection.providePetApiService()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AmosDelDogtownTheme {

                // Create an state to hold the list of pets
                // Compose is clever and everytime this state changes, the composables that uses this state
                // will be called again with the new parameters
                val petsState = remember { mutableStateOf(listOf<Pet>()) }

                // Launch Effect is is used to call coroutines (things that executed in a different thread)
                // A coroutine is a method with the "suspend" keyword
                LaunchedEffect(Unit) {
                    // Execute the request call
                    val petResponse = petApiService.getPets()

                    // Store the new list of pets in the state
                    petsState.value = petResponse.result
                }

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

                    // By using the state, everytime the list change, PetList will be "recomposed" to display new content
                    PetList(
                        pets = petsState.value,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PetList(pets: List<Pet>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
    ) {
        items(pets) { pet ->
            PetItem(pet = pet)
        }
    }
}

@Composable
fun PetItem(pet: Pet, modifier: Modifier = Modifier) {
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
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
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
fun PetListPreview() {
    AmosDelDogtownTheme {
        PetList(pets = listOf(
            Pet("1", "Amos", "Description", "https://example.com/image.jpg"),
            Pet("2", "Rex", "Description", "https://example.com/image.jpg")
        ))
    }
}
