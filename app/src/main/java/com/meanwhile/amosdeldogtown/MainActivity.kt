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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meanwhile.amosdeldogtown.ui.theme.AmosDelDogtownTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AmosDelDogtownTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val pets = mutableListOf<String>()
                    for (i in 1..100) {
                        pets.add("Pet clone $i")
                    }
                    PetList(
                        pets = pets,
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
