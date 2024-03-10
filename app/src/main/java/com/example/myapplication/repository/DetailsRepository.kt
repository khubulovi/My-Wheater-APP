package com.example.myapplication.repository

import com.example.myapplication.model.WheatherDTO

interface DetailsRepository {
    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WheatherDTO>
    )
}