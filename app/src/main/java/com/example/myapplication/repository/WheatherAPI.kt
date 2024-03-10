package com.example.myapplication.repository

import com.example.myapplication.model.WheatherDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WheatherAPI {
    @GET("v2/informers")
    fun getWheater(
        @Header("X-Yandex-API-Key") token: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<WheatherDTO>
}