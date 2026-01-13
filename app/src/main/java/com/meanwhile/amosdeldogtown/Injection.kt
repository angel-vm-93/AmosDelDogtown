package com.meanwhile.amosdeldogtown

import com.meanwhile.amosdeldogtown.data.PetApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Injection {

    private const val BASE_URL = "https://www.zaragoza.es/sede/servicio/"

    fun providePetApiService() : PetApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // Take care of converting Json to Kotlin classes
            .build()
            .create(PetApiService::class.java)
    }

}
