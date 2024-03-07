package com.example.myapplication.model

class RepositoryImpl:Repository {
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