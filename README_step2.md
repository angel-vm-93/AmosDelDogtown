# Retrofit Implementation with Logging

Follow these steps to implement a Retrofit client with GSON conversion and HTTP logging.

### 1. Add Dependencies
Add to `gradle/libs.versions.toml`:
```toml
[versions]
retrofit = "2.11.0"
okhttp = "4.12.0"

[libraries]
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-converter-gson = { group = "com.squareup.retrofit2", name = "converter-gson", version.ref = "retrofit" }
okhttp-logging-interceptor = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp" }
```

Add to `app/build.gradle.kts`:
```kotlin
dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)
}
```

### 2. Add Internet Permission
In `app/src/main/AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### 3. Define Data Models
The API return the data in JSON format. We will create two kotlin classes that represent the same json structure.
The json will be automagically converted to these classes by the library used to do the request.
Create `Pet.kt`:
```kotlin
data class PetResponse(val result: List<Pet>)
data class Pet(
    val id: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("observations") val description: String?,
    @SerializedName("foto") val imageUrl: String?
)
```

### 4. Create API Service
This interface defines which endpoint is called and what response is expected. 
In the next step we will use Retrofit library to create an instance of this interface. 
This is how retrofit works, we create an interface and then the library creates all the inside logic so 
we don't have to worry about it. 
Create `PetApiService.kt`:
```kotlin
interface PetApiService {
    @Headers("Accept: application/json")
    @GET("mascotas")
    suspend fun getPets(): PetResponse
}
```

### 5. Setup Retrofit Instance
This is how to create a instance for our PetApiService.
We create a separated class Injection so we can reuse this code in other parts of the app.
In `Injection.kt`:
```kotlin
val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
val client = OkHttpClient.Builder().addInterceptor(logging).build()

val apiService = Retrofit.Builder()
    .baseUrl("https://www.zaragoza.es/sede/servicio/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(PetApiService::class.java)
```

### 6. Add changes in MainActivity
Create a new field that will hold an instance of the api service. We'll us eit to call the api
```kotlin
private val petApiService: PetApiService by lazy {
   Injection.providePetApiService()
}
```
Create a new Compose state that will store the pet names we receive from the api request.
Create inside the Theme (AmosDelDogTownTheme)
```kotlin
val petsState = remember { mutableStateOf(listOf<String>()) }
```

Change PetList to use our new state
```kotlin
PetList(
    pets = petsState.value,
    modifier = Modifier.padding(innerPadding)
)
```

Let's get real, call the api:
```kotlin
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
```



