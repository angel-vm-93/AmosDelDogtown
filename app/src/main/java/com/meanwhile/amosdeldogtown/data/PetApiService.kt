package com.meanwhile.amosdeldogtown.data

import retrofit2.http.GET
import retrofit2.http.Headers

interface PetApiService {

    @Headers("Accept: application/json")
    @GET("mascotas")
    suspend fun getPets(): PetResponse
}

