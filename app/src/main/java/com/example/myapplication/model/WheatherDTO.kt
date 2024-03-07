package com.example.myapplication.model


data class WheatherDTO(val fact: FactDTO?)


data class FactDTO(
    val temp: Int?,
    val feelsLike: Int?,
    val condition: String?
)