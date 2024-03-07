package com.example.myapplication.viewmodels

import com.example.myapplication.model.Wheater

sealed class AppState{
    data class Sucsess(val wheateherData : List<Wheater>) :AppState()
    data class Eror(val eror:Throwable): AppState()
    object Loading : AppState()
    }

