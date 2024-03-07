package com.example.myapplication.model

interface Repository {
    fun getWheatherFromServer():Wheater
    fun getWheatherFromLocaleStorageWorld():List<Wheater>
    fun getWheatherFromLocaleStorageGeo():List<Wheater>
}