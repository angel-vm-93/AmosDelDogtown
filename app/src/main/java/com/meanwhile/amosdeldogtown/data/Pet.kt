package com.meanwhile.amosdeldogtown.data

import com.google.gson.annotations.SerializedName

data class PetResponse(
    val result: List<Pet>
)

data class Pet(
    val id: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("raza") val race: String?,
    @SerializedName("sexo") val sex: String?,
    @SerializedName("fechaNac") val birthday: String?,
    @SerializedName("tamagno") val size: String?,
    @SerializedName("peligroso") val dangerousDog: Boolean,
    @SerializedName("esterilizado") val sterilized: Boolean,
    @SerializedName("caracter") val nature: String?,
    @SerializedName("fechaIngreso") val entryDate: String?,
    @SerializedName("observaciones") val description: String?,
    @SerializedName("foto") val imageUrl: String?
)
