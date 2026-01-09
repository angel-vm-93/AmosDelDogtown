package com.meanwhile.amosdeldogtown.help

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meanwhile.amosdeldogtown.ui.theme.AmosDelDogtownTheme

/* Inside Scaffold
PetList(
pets = listOf("Amos", "Rex", "Buddy", "Max", "Bella"),
modifier = Modifier.padding(innerPadding)
)
*/

@Composable
fun PetItem(name: String, modifier: Modifier = Modifier) {
    Surface( // Dines the surface, like color and rounded corners
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        color = Color.LightGray,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box( // Defines how the content aligns inside (Center)
            modifier = Modifier.padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = name)
        }
    }
}

@Composable
fun PetList(pets: List<String>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        pets.forEach { pet ->
            PetItem(name = pet)
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
