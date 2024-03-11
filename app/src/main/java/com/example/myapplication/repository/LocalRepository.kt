package com.example.myapplication.repository

import com.example.myapplication.model.Wheater

interface LocalRepository {
    fun getAllHistory():List<Wheater>
    fun saveEntity(wheater: Wheater)
}