package com.example.myapplication.repository

import com.example.myapplication.model.Wheater

interface MainRepository {
    fun getWheatherFromServer(): Wheater
    fun getWheatherFromLocaleStorageWorld():List<Wheater>
    fun getWheatherFromLocaleStorageGeo():List<Wheater>
}