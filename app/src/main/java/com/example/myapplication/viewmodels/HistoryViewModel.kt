package com.example.myapplication.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.app.App.Companion.getHistoryDao
import com.example.myapplication.app.AppState
import com.example.myapplication.repository.LocalRepositoryImpl

class HistoryViewModel(
    val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepositoryImpl: LocalRepositoryImpl = LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {
    fun getAllHistory() {
        historyLiveData.value = AppState.Sucsess(historyRepositoryImpl.getAllHistory())
        historyLiveData.value = AppState.Loading
    }
}