package com.meanwhile.amosdeldogtown.data

import com.meanwhile.amosdeldogtown.Injection

class PetRepository(
    private val petApiService: PetApiService = Injection.providePetApiService()
) {
    suspend fun getPets(): List<Pet> {
        return petApiService.getPets().result
    }
}
