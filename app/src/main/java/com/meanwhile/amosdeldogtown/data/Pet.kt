package com.meanwhile.amosdeldogtown.data

import com.google.gson.annotations.SerializedName

data class PetResponse(
    val result: List<Pet>
)

data class Pet(
    val id: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("observations") val description: String?,
    @SerializedName("foto") val imageUrl: String?
)
