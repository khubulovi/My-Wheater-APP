package com.example.myapplication.repository

import com.example.myapplication.model.Wheater
import com.example.myapplication.model.getGeoCities
import com.example.myapplication.model.getWorldCities

class MainRepositoryImpl: MainRepository {
    override fun getWheatherFromServer(): Wheater {
        return Wheater()
    }

    override fun getWheatherFromLocaleStorageWorld(): List<Wheater> {
        return getWorldCities()
    }

    override fun getWheatherFromLocaleStorageGeo(): List<Wheater> {
        return getGeoCities()

    }
}