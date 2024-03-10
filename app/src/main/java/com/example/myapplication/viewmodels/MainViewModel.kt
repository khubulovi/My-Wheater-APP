package com.example.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.repository.MainRepository
import com.example.myapplication.repository.MainRepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataObserve: MutableLiveData<Any> = MutableLiveData(),
    private val mainRepositoryImpl: MainRepository = MainRepositoryImpl()
) :
    ViewModel() {

    private val _icon: MutableLiveData<Int> = MutableLiveData()
    val icon: LiveData<Int> = _icon
    private var isDataSetGeo: Boolean = true

    init {
        getWeatherFromLocalSourceGeo()
    }


    fun getLiveData() = liveDataObserve

    fun getWeatherFromLocalSourceGeo() {
        getDataFromLocalSource(isGeo = true)
        _icon.value = R.drawable.ic_launcher_georgia_foreground
    }

    private fun getWeatherFromLocalSourceWorld() {
        getDataFromLocalSource(isGeo = false)
        _icon.value = R.drawable.ic_launcher_world_foreground
    }

    private fun getDataFromLocalSource(isGeo: Boolean) {
        liveDataObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataObserve.postValue(
                AppState.Sucsess(
                    if (isGeo) mainRepositoryImpl.getWheatherFromLocaleStorageGeo()
                    else mainRepositoryImpl.getWheatherFromLocaleStorageWorld()
                )
            )
        }.start()
    }
    fun changeGeoToWorld(){
        if(isDataSetGeo)
        {getWeatherFromLocalSourceGeo()}
        else{
            getWeatherFromLocalSourceWorld()
        }.also { isDataSetGeo = !isDataSetGeo }
    }
}
