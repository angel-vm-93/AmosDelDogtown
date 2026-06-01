package com.meanwhile.amosdeldogtown.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.meanwhile.amosdeldogtown.data.Pet
import androidx.compose.ui.tooling.preview.Preview
import com.meanwhile.amosdeldogtown.ui.theme.AmosDelDogtownTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailView(
    pet: Pet,
    onBack: () -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pet.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onFavoriteClick(pet.id) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = "https://" + pet.imageUrl,
                contentDescription = pet.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = pet.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))

                PetField("Raza", pet.race)
                PetField("Sexo", pet.sex)
                PetField("Fecha de nacimiento", formatDate(pet.birthday))
                PetField("Tamaño", pet.size)
                PetField("Carácter", pet.nature)
                PetField("Fecha de ingreso", formatDate(pet.entryDate))
                PetField("Peligroso", booleanToYesNo(pet.dangerousDog))
                PetField("Esterilizado", booleanToYesNo(pet.sterilized))

                pet.description?.takeIf { it.isNotBlank() }?.let { desc ->
                    Spacer(modifier = Modifier.height(8.dp))
                    ExpandableDescription("Descripción", desc)
                }
            }
        }
    }
}

@Composable
private fun PetField(label: String, value: String?) {
    if (!value.isNullOrBlank()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = "$label: ",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun formatDate(dateString: String?): String? {
    if (dateString.isNullOrBlank()) return null
    return try {
        val parts = dateString.substring(0, 10).split("-")
        "${parts[2]}/${parts[1]}/${parts[0]}"
    } catch (e: Exception) {
        null
    }
}

private fun booleanToYesNo(value: Boolean): String = if (value) "Sí" else "No"

@Preview(showBackground = true)
@Composable
private fun DetailViewPreview() {
    AmosDelDogtownTheme {
        DetailView(
            pet = Pet(
                id = "1",
                name = "CHIPIRON",
                race = "MESTIZO",
                sex = "Macho",
                birthday = "2017-04-04T00:00:00",
                size = "Mediano (11-25 kg)",
                dangerousDog = false,
                sterilized = false,
                nature = "Positivo",
                entryDate = "2018-08-14T00:00:00",
                description = "Perro muy tranquilo y amigable de tamaño mediano. Ven a conocerlo.",
                imageUrl = null
            ),
            onBack = {},
            isFavorite = false,
            onFavoriteClick = {}
        )
    }
}