package com.example.myapplication.repository

import com.example.myapplication.model.WheatherDTO
import retrofit2.Callback

class DetailsRepositoryIplm(private val dataSourse: RemoteDataSourse) : DetailsRepository {
    override fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: Callback<WheatherDTO>
    ) {
dataSourse.getWeatherDetails(lat, lon, callback)
    }
}