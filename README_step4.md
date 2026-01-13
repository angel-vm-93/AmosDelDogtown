# Adding good architecture principles

Learn how to load and display images from a URL using the Coil library.

### 1. Add ViewModel compose dependency

Add to `gradle/libs.versions.toml`:
```toml
[libraries]
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntimeKtx" }
```

Add to `app/build.gradle.kts`:
```kotlin
dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
```

### 2. Create a UiStat class
the ViewModel is gonna be in charge of passing all the required information that the Ui needs.
To do that, we'll create a class containing exactly what the ui needs

Create class `UiState.kt`:
```kotlin
data class MainUiState(
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = false, // TODO not needed, but we'll use them later
    val error: String? = null // TODO not needed, but will use them later
)
```

### 3. Create the ViewModel
The ViewModel will have an instance to the service and exposes data to the ui through kotlin Flows.
Flows are a class perfect for observing changes. 
Create `MainViewModel.kt`:
```kotlin
class MainViewModel(
    private val petApiService: PetApiService = Injection.providePetApiService()
) : ViewModel() {

    // This mutable flow will allow us to post UiState to the ui
    private val _uiState = MutableStateFlow(MainUiState())

    // The Ui shouldn't see a mutable flow (the ui should only observe) so we expose the flow a Non mutable
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    // This is executed when the class is created
    // Perfect for the api call we want to do at the start
    init {
        fetchPets()
    }

    fun fetchPets() {
        viewModelScope.launch {
            // post the UI that we are loading
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // Call the service
                val response = petApiService.getPets()

                // Post to the Ui our results
                _uiState.value = _uiState.value.copy(
                    pets = response.result,
                    isLoading = false
                )
            } catch (e: Exception) {
                // post to the ui the error
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}
```

### 4. Add repository
Repositories are in charge of communicating with network or databases.
The ViewModel request information to the repo, and the repo is in charge of getting it where it considers more apropiate.
I.e: Is might get if from a database first, and then trigger a request in the background to get fresh data.

Create a new data package and a file `PetRepository.kt`:
```kotlin
class PetRepository(
    private val petApiService: PetApiService = Injection.providePetApiService()
) {
    suspend fun getPets(): List<Pet> {
        return petApiService.getPets().result
    }
}
```

Change MainViewModel to use the repo instead:
```kotlin
class MainViewModel(
    private val petRepository: PetRepository = PetRepository()
) : ViewModel()
```

