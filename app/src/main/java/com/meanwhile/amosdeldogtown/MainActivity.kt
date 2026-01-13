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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AmosDelDogtownTheme {

                // Create an state to hold the list of pest
                // Compose is clever and everytime this state changes, the composables that uses this state
                // will be called again with the new parameters
                val petsState = remember { mutableStateOf(listOf<String>()) }

                // Launch Effect is is used to call coroutines (things that executed in a different thread)
                // A coroutine is a method with the "suspend" keyword
                LaunchedEffect(Unit) {
                    // Execute the request call
                    val petResponse = petApiService.getPets()

                    // From the response, for now we only want the names of the pets
                    val names = petResponse.result.map { pet ->
                        pet.name
                    }

                    // Store the new list of names in the state
                    petsState.value = names
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

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
fun PetList(pets: List<String>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
    ) {
        items(pets) { pet ->
            PetItem(name = pet)
        }
    }
}

@Composable
fun PetItem(name: String, modifier: Modifier = Modifier) {
    Surface( // Dines the surface, like color and rounded corners
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        color = Color.LightGray,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box( // Defines how the content aligns inside
            modifier = Modifier
                .size(150.dp)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = name)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PetListPreview() {
    AmosDelDogtownTheme {
        PetList(pets = listOf("Amos", "Rex", "Buddy"))
    }
}
