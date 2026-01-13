# Step 2: Displaying Remote Images with Coil

Learn how to load and display images from a URL using the Coil library.

### 1. Add Coil Dependency
Coil is the modern image loading library for Android that integrates seamlessly with Jetpack Compose.

Add to `gradle/libs.versions.toml`:
```toml
[versions]
coil = "2.7.0"

[libraries]
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }
```

Add to `app/build.gradle.kts`:
```kotlin
dependencies {
    implementation(libs.coil.compose)
}
```

### 2. Update State to hold Pet objects
To access the image URL, we must store the full data objects in our state instead of just a list of names.

In `MainActivity.kt`:
```kotlin
// Define the state as a list of Pet objects
val petsState = remember { mutableStateOf(listOf<Pet>()) }

LaunchedEffect(Unit) {
    val petResponse = petApiService.getPets()
    // Update the state with the full objects from the API
    petsState.value = petResponse.result
}
```

### 3. Display the Image using AsyncImage
The `AsyncImage` composable handles the complexity of downloading, caching, and rendering remote images.

In `MainActivity.kt`:
```kotlin
@Composable
fun PetItem(pet: Pet, modifier: Modifier = Modifier) {
    Box(modifier = Modifier.size(150.dp)) {
        AsyncImage(
            model = "https://" + pet.imageUrl, // FIXME this is pretty hacky, but will work for this codelab
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Crops the image to fill the square area
        )
        // Add a background to the text for better contrast
        Surface(
            color = Color.Black.copy(alpha = 0.5f),
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
        ) {
            Text(text = pet.name, color = Color.White, modifier = Modifier.padding(8.dp))
        }
    }
}
```

### 4. Add an TopAppBar
Scaffold has a dedicated parameter where to add a TopAppbar composable, which is the perfect element to display a top bar. 
We use it onl with a title, but it has more parameters to display action icons, scroll at the same time that the content 
and things like this.
```kotlin
Scaffold(
    modifier = Modifier.fillMaxSize(),
     topBar = {
         TopAppBar(
             title = {
                 Text(text = "Amos Del Dogtown")
             }
         )
     }
)
```

