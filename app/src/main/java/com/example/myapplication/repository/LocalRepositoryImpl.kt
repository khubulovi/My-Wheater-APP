package com.example.myapplication.repository

import com.example.myapplication.model.City
import com.example.myapplication.model.Wheater
import com.example.myapplication.room.HistoryDao
import com.example.myapplication.room.HistoryEntity

class LocalRepositoryImpl(private val localDataSourse: HistoryDao) : LocalRepository {
    override fun getAllHistory(): List<Wheater> =
        convertHistoryEntityToWeather(localDataSourse.all())


    override fun saveEntity(wheater: Wheater) {
        convertWeatherToEntity(wheater)?.let { localDataSourse.insert(it) }
    }

    private fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Wheater> {
        return entityList.map {
            Wheater(City(it.city, 0.0, 0.0), it.temperature, 0, it.condition)
        }
    }

    private fun convertWeatherToEntity(weather: Wheater): HistoryEntity? {
        return weather.city.city?.let {
            HistoryEntity(
                0,
                it, weather.temperature, weather.condition
            )
        }
    }
}